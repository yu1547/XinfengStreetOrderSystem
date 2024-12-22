let currentCategory = ""; // 當前分類
let fullMenuData = []; // 全部菜單資料，用於搜尋功能

let userId = "";

// 初始載入
document.addEventListener("DOMContentLoaded", async () => {
    try {
        await fetchUserId(); // 等待 fetchUserId 完成
        fetchCategories(); // 獲取分類
        fetchMenuData(currentCategory); // 預設加載全部菜單
    } catch (error) {
        console.error("初始化時出錯:", error);
    }
});


async function fetchUserId() {
    try {
        const response = await fetch("/api/users/current-user");
        if (!response.ok) {
            throw new Error("Failed to fetch user ID");
        }
        const data = await response.json(); // 假設 response.json() 返回的是 userId
        userId = data.userId;
    } catch (error) {
        console.error("無法獲取用戶 ID:", error);
    }
}


async function fetchCategories() {
    try {
        const response = await fetch(`/api/menu/categories`);
        const categories = await response.json();
        console.log(categories);
        console.log(userId);

        // 檢查用戶的收藏清單
        const favoritesResponse = await fetch(`/api/users/${userId}/favorites`);
        console.log(favoritesResponse);
        const favorites = await favoritesResponse.json();

        // 預設分類為空字串
        currentCategory = "";

        // 構建分類標籤
        const categoriesContainer = document.getElementById("categoriesContainer");
        let categoryHTML = "";

        // 如果有收藏清單且 "favorites" 不在分類中，則加入
        if (favorites.length > 0) {
            if (!categories.includes("favorites")) {
                categories.push("favorites"); // 如果收藏清單不存在，加入 "favorites" 到分類列表
            }
            categoryHTML += `<span class="${currentCategory === "favorites" ? "active" : ""}" onclick="setActive(this, 'favorites')">收藏清單</span>`;
            currentCategory = "favorites"; // 預設為收藏清單
        }

        // 如果有「套餐」分類則顯示
        if (categories.includes("套餐")) {
            categoryHTML += `<span class="${currentCategory === "套餐" ? "active" : ""}" onclick="setActive(this, '套餐')">套餐</span>`;
            if (!currentCategory) currentCategory = "套餐"; // 如果還沒選中分類，預設為「套餐」
        }

        // 加載其他分類（過濾掉「套餐」和「favorites」）
        categoryHTML += categories
            .filter(category => category !== "套餐" && category !== "favorites")
            .map(
                category =>
                    `<span class="${currentCategory === category ? "active" : ""}" onclick="setActive(this, '${category}')">${category}</span>`
            )
            .join("");

        // 更新分類容器
        categoriesContainer.innerHTML = categoryHTML;
        console.log(currentCategory);

        // 如果沒有任何分類預設一個
        if (!currentCategory && categories.length > 0) {
            currentCategory = categories[0];
        }

        // 根據當前分類加載菜單
        if (currentCategory === "favorites") {
            fetchFavoriteItems();
        } else {
            fetchMenuData(currentCategory);
        }
    } catch (error) {
        console.error("無法獲取分類:", error);
    }
}




// 獲取用戶收藏的菜單項目並渲染
async function fetchFavoriteItems() {
    try {
        // 獲取用戶的收藏項目 IDs
        const response = await fetch(`/api/users/${userId}/favorites`);
        const favoriteItems = await response.json();  // 假設返回的是 [{ menuItemId: 'id1' }, { menuItemId: 'id2' }, ...]
        console.log(favoriteItems);
        if (favoriteItems.length > 0) {
            // 根據每個 menuItemId 獲取對應的菜單項目
            const menuItems = await Promise.all(favoriteItems.map(async (favoriteItem) => {
                const menuItemResponse = await fetch(`/api/menu/id/${favoriteItem.menuItemId}`);
                const menuItem = await menuItemResponse.json();
                return menuItem;
            }));

            // 渲染菜單項目
            renderMenu(menuItems);
            await checkFavorites(menuItems); // 檢查收藏狀態
        } else {
            console.log("沒有收藏的菜單");
        }
    } catch (error) {
        console.error("獲取收藏菜單失敗:", error);
    }
}

// 獲取菜單資料並渲染
async function fetchMenuData(category) {
    try {
        let menuData;
        console.log(category);
        if (category === "favorites") {
            // 如果是收藏清單，使用用戶的收藏資料
            const response = await fetch(`/api/users/${userId}/favorites`);
            menuData = await response.json();
        } else {
            // 否則根據分類獲取菜單資料
            const response = await fetch(`/api/menu/${category}`);
            menuData = await response.json();
        }

        fullMenuData = menuData; // 儲存完整菜單資料
        renderMenu(menuData);
        await checkFavorites(menuData); // 檢查收藏狀態
    } catch (error) {
        console.error("無法獲取菜單資料:", error);
    }
}

// 渲染菜單內容
async function renderMenu(menuData) {
    const menuContainer = document.getElementById("menuContainer");

    // 等待資料載入並且確認資料格式
    if (!Array.isArray(menuData)) {
        // 清空菜單容器，準備渲染正確的菜單
        menuContainer.innerHTML = "";
        menuContainer.innerHTML = "<p>正在載入菜單...</p>";
        return;
    }

    // 使用 Promise.all 等待所有菜單項目的平均評分都被獲取
    const menuItems = await Promise.all(
        menuData.map(async (item, index) => {
            // 獲取當前菜單項目的平均評分
            const averageRating = await fetchAverageRating(item.id);
            console.log(averageRating);
            let packageDetailsHtml = "";
            return `
            <div class="menu-item" data-id="${item.id}">
                <!-- 基本資訊 -->
                <div class="item-info">
                    <strong>${item.name}</strong>
                    <img src="${item.image}" alt="${item.name}" class="item-image">
                </div>
                <!-- 顯示套餐內容 -->
                ${
                    item.category === '套餐'
                        ? `
                            <div class="package-group">
                                <button class="package-arrow" data-expanded="false" onclick="togglePackageDetails('${item.id}')">
                                    <span class="arrow-icon"></span>
                                </button>
                                <div class="package-details" id="package-details-${item.id}">
                                    <ul>
                                        ${item.setContents
                                            .split(/[\s,]+/)
                                            .map(content => `<li>${content}</li>`)
                                            .join('')}
                                    </ul>
                                </div>
                            </div>
                        `
                        : ''
                }
        
                <!-- 詳細內容 -->
                <div class="item-details">
                    <div class="item-description-box">
                        <p class="item-description">${item.description}</p>
                    </div>
                    <p class="item-price">$${item.price}</p>
                </div>
        
                <!-- 數量與加入購物車按鈕 -->
                <div class="item-quantity-add-container">
                    <div class="quantity-control">
                        <button onclick="changeQuantity(-1, 'quantity${index}')">-</button>
                        <input type="text" value="0" id="quantity${index}" readonly>
                        <button onclick="changeQuantity(1, 'quantity${index}')">+</button>
                        <span class="error-message" id="error-quantity${index}">請選擇數量！</span>
                        <span class="success-message" id="success-quantity${index}" style="display: none;">成功加入！</span>
                    </div>
                    <button class="add-btn" onclick="addToCart('${item.id}', 'quantity${index}')">加入</button>
                </div>
        
                <!-- 收藏與評分 -->
                <div class="favorite-rating-group">
                    <button class="favorite-btn" id="favorite-btn-${item.id}" data-favorited="${item.isFavorited}" onclick="toggleFavorite(this, '${item.id}', '${item.name}')">
                        <i class="heart-icon"></i>
                    </button>
                    <div class="rating-info">
                        <span id="rating-number-${item.id}">${averageRating === "N/A" ? "none" : averageRating}</span>
                        <div id="star-${item.id}" class="star-icon"></div>
                    </div>
                </div>
            </div>
        `;

        })
    );

    // 當所有菜單項目處理完畢後，更新 menuContainer 的 innerHTML
    menuContainer.innerHTML = menuItems.join("");

    // 渲染完畢後，檢查並更新收藏狀態
    await checkFavorites(menuData);

    //console.log("菜單內容已更新", menuContainer.innerHTML);
}

// 控制套餐內容顯示/隱藏
function togglePackageDetails(itemId) {
    const packageDetails = document.getElementById(`package-details-${itemId}`);
    const arrowButton = document.querySelector(`.menu-item[data-id="${itemId}"] .package-arrow`);
    console.log(arrowButton);
    // 切換顯示/隱藏套餐內容
    const isExpanded = arrowButton.getAttribute('data-expanded') === 'true';
    if (isExpanded) {
        packageDetails.style.display = 'none';
        arrowButton.setAttribute('data-expanded', 'false');
    } else {
        packageDetails.style.display = 'block';
        arrowButton.setAttribute('data-expanded', 'true');
    }
}

//檢查常用菜單收藏狀況
async function checkFavorites(menuData) {
    try {
        // 假設有一個API可以獲取用戶的收藏
        console.log(userId);
        const response = await fetch(`/api/users/${userId}/favorites`);
        const favorites = await response.json();
        console.log("用戶收藏項目：", favorites);

        // 對每個菜單項目檢查是否已收藏
        menuData.forEach(item => {
            //console.log('Item ID:', item.id);
            //console.log('Favorites:', favorites);
            //console.log(typeof favorites);
            //console.log(typeof item.id);
            const isFavorited = favorites.some(favorite => favorite.menuItemId === item.id);
            console.log(isFavorited);
            const favoriteButton = document.querySelector(`#favorite-btn-${item.id}`);
            console.log(favoriteButton);
            if (favoriteButton) {
                favoriteButton.setAttribute('data-favorited', isFavorited ? 'true' : 'false');
            }
        });
    } catch (error) {
        console.error("無法檢查收藏狀態:", error);
    }
}

//切換
function toggleFavorite(button, itemId, itemName) {
    //console.log("切換前的 favoriteButton 資料:");
    console.log(button); // 查看 button 元素本身
    //console.log("切換前的 data-favorited:", button.getAttribute("data-favorited")); // 查看 data-favorited 的值

    const isFavorited = button.getAttribute("data-favorited") === "true";
    //console.log(button);
    button.setAttribute("data-favorited", !isFavorited);

    if (!isFavorited) {
        // 收藏
        fetch(`/api/users/${userId}/favorites/add`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ menuItemId: itemId, itemName: itemName })
        })
        .then(response => {
            if (response.ok) {
                console.log(`已收藏 ${itemName}`);
            } else {
                console.error(`無法收藏 ${itemName}`);
            }
        })
        .catch(err => console.error('Error:', err));
    } else {
        // 取消收藏
        fetch(`/api/users/${userId}/favorites/${itemId}/delete`, {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' },
        })
        .then(response => {
            if (response.ok) {
                console.log(`已取消收藏 ${itemName}`);
            } else {
                console.error(`無法取消收藏 ${itemName}`);
            }
        })
        .catch(err => console.error('Error:', err));
    }
}

// 獲取平均評分
async function fetchAverageRating(menuItemId) {
    try {
        const response = await fetch(`/rating/menu/${menuItemId}/average`);  // API 來獲取評分
        const data = await response.json();
        console.log(menuItemId);
        console.log(data);
        console.log(typeof data);
        if (data) {
            return data.toFixed(1);  // 四捨五入到小數點第一位
        } else {
            return "N/A";  // 如果沒有評分資料，顯示 "N/A"
        }
    } catch (error) {
        console.error("獲取評分失敗:", error);
        return "N/A";
    }
}

async function fetchFavoriteItems() {
    try {
        // 獲取用戶的收藏項目 IDs
        const response = await fetch(`/api/users/${userId}/favorites`);
        const favoriteItems = await response.json();  // 假設返回的是 [{ menuItemId: 'id1' }, { menuItemId: 'id2' }, ...]
        
        if (favoriteItems.length === 0) {
            alert("目前沒有收藏的餐點！");
            // 清空菜單容器，顯示空收藏的訊息
            const menuContainer = document.getElementById("menuContainer");
            menuContainer.innerHTML = "<p>目前沒有收藏的餐點。</p>";
            return; // 如果收藏清單為空，則不加載任何菜單
        }

        // 根據每個 menuItemId 獲取對應的菜單項目
        const menuItems = await Promise.all(favoriteItems.map(async (favoriteItem) => {
            const menuItemResponse = await fetch(`/api/menu/id/${favoriteItem.menuItemId}`);
            const menuItem = await menuItemResponse.json();
            return menuItem;
        }));

        // 渲染菜單項目
        renderMenu(menuItems);
        await checkFavorites(menuItems); // 檢查收藏狀態
    } catch (error) {
        console.error("獲取收藏菜單失敗:", error);
    }
}

function setActive(element, category) {
    const categories = document.querySelectorAll('.categories span');
    categories.forEach(cat => cat.classList.remove('active'));
    element.classList.add('active');
    currentCategory = category;

    if (category === "favorites") {
        // 點擊收藏清單時，檢查收藏清單是否為空
        fetch(`/api/users/${userId}/favorites`)
            .then(response => response.json())
            .then(favoriteItems => {
                if (favoriteItems.length === 0) {
                    alert("目前沒有收藏的餐點！");
                    return; // 如果收藏清單為空，阻止進一步加載菜單
                } else {
                    fetchFavoriteItems(); // 加載收藏菜單
                }
            })
            .catch(err => console.error('獲取收藏清單時發生錯誤:', err));
    } else {
        // 其他分類
        fetchMenuData(category);
    }
}


// 搜尋功能：只在按下 Enter 時觸發
function searchMenu() {
    const query = document.getElementById("searchInput").value.trim();
    const menuContainer = document.getElementById("menuContainer");
    if (query) {
        fetch(`/api/menu/search?query=${query}`)
            .then(response => response.json())
            .then(menuData => {
                if (menuData.length === 0) {
                    menuContainer.innerHTML = "<p>未搜尋到餐點。</p>"; // 顯示未搜尋到餐點的提示
                } else {
                    renderMenu(menuData); // 顯示搜尋到的餐點
                }
            })
            .catch(error => {
                console.error("搜尋菜單失敗:", error);
                menuContainer.innerHTML = "<p>未搜尋到餐點。</p>"; // 顯示搜尋錯誤的提示
            });
    } else {
        menuContainer.innerHTML = ""; // 當搜尋框為空時，清空顯示
    }
}

// 監聽 Enter 鍵
document.getElementById("searchInput").addEventListener("keydown", function(event) {
    if (event.key === "Enter") { // 檢查是否按下 Enter 鍵
        event.preventDefault(); // 防止表單提交
        searchMenu(); // 執行搜尋
    }
});

// 修改數量
function changeQuantity(amount, quantityId) {
    const quantityInput = document.getElementById(quantityId);
    let currentQuantity = parseInt(quantityInput.value);
    currentQuantity += amount;
    if (currentQuantity < 0) {
        currentQuantity = 0;
    }
    quantityInput.value = currentQuantity;
}

function redirectToOrderPage() {
    const cart = getCart(); // 獲取購物車資料
    if (cart.items.length === 0) {
        // 如果購物車是空的，顯示警告提示
        alert("未加入任何餐點入點菜單！");
    } else {
        // 購物車有商品，跳轉到 orders.html
        window.location.href = "orders.html";
    }
}

document.getElementById('ordersBtn').addEventListener('click', redirectToOrderPage);

document.addEventListener('DOMContentLoaded', function () {
    const orderHistoryButton = document.getElementById('order-history-button'); // 取得訂單歷史按鈕
    console.log(orderHistoryButton);
    if (orderHistoryButton) {
        orderHistoryButton.addEventListener('click', function () {
            // 點擊後跳轉到 orderHistory.html
            window.location.href = "OrderHistory.html";
        });
    }
});


const CART_SESSION_KEY = "cart";

// 獲取購物車資料，若沒有則創建一個
function getCart() {
    let cart = JSON.parse(sessionStorage.getItem("cart"));

    if (!cart) {
        // 如果沒有購物車，創建新的購物車物件
        cart = {
            items: [],  // 初始化為空的購物車項目
        };
        sessionStorage.setItem("cart", JSON.stringify(cart));  // 存儲到 sessionStorage
        console.log(JSON.parse(sessionStorage.getItem("cart")));
    }
    return cart;
}

// 合併加入購物車與更新數量的邏輯
function addToCart(itemId, quantityId) {
    const quantityInput = document.getElementById(quantityId);
    const quantity = parseInt(quantityInput.value);  // 獲取數量
    const errorElement = document.getElementById(`error-${quantityId}`);
    const successElement = document.getElementById(`success-${quantityId}`);

    if (errorElement) {
        errorElement.style.display = "none";
    }

    if (quantity > 0) {
        errorElement.style.display = "none";
        // 獲取當前用戶的購物車，若無則初始化為空購物車
        const cart = JSON.parse(sessionStorage.getItem("cart")) || { items: [] };

        // 檢查這個菜品是否已經在購物車中
        const existingItem = cart.items.find(cartItem => cartItem.menuItemId === itemId);
        
        if (existingItem) {
            // 如果已經存在，更新數量
            existingItem.quantity += quantity;
        } else {
            // 如果不存在，將品項新增到購物車
            cart.items.push({
                menuItemId: itemId,
                quantity: quantity,
            });
        }

        // 更新購物車至 sessionStorage
        sessionStorage.setItem("cart", JSON.stringify(cart));
        quantityInput.value = 0;

        // 顯示成功訊息
        successElement.style.display = "block";

        // 兩秒後隱藏成功訊息
        setTimeout(() => {
            successElement.style.display = "none";
        }, 2000);
    } else {
        if (errorElement) {
            errorElement.style.display = "block";
        }
    }
    console.log(JSON.parse(sessionStorage.getItem("cart")));
}





