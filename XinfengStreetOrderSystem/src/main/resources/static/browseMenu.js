let currentCategory = ""; // 當前分類
let fullMenuData = []; // 全部菜單資料，用於搜尋功能
let customerId = "customer123"; // 假設這是當前用戶的ID，可以從session中獲取

// 初始載入
document.addEventListener("DOMContentLoaded", () => {
    fetchCategories(); // 獲取分類
    fetchMenuData(""); // 預設加載全部菜單
});

// 獲取分類並渲染按鈕
async function fetchCategories() {
    try {
        const response = await fetch(`/api/menu/categories`);
        const categories = await response.json();
        const categoriesContainer = document.getElementById("categoriesContainer");

        categoriesContainer.innerHTML = categories
            .map(
                (category, index) =>
                    `<span class="${index === 0 ? "active" : ""}" 
                    onclick="setActive(this, '${category}')">${category}</span>`
            )
            .join("");
        currentCategory = categories[0]; // 設定第一個分類為預設
        fetchMenuData(currentCategory); // 立即加載第一個分類的菜單
    } catch (error) {
        console.error("無法獲取分類:", error);
    }
}

// 獲取菜單資料並渲染
async function fetchMenuData(category) {
    try {
        const response = await fetch(`/api/menu/${category}`);
        const menuData = await response.json();
        fullMenuData = menuData; // 儲存完整菜單資料
        renderMenu(menuData);
    } catch (error) {
        console.error("無法獲取菜單資料:", error);
    }
}

// 渲染菜單內容
function renderMenu(menuData) {
    const menuContainer = document.getElementById("menuContainer");
    menuContainer.innerHTML = menuData
        .map(
            (item, index) => `
            
        <div class="menu-item">
            <div class="item-info">
                <strong>${item.name}</strong>
                <img src="${item.image}" alt="${item.name}" class="item-image">
            </div>
            <div class="item-details">
                <div class="item-description-box">
                    <p class="item-description">${item.description}</p>
                </div>
                <p class="item-price">$${item.price}</p>
            </div>
            <div class="quantity-control">
                <button onclick="changeQuantity(-1, 'quantity${index}')">-</button>
                <input type="text" value="0" id="quantity${index}" readonly>
                <button onclick="changeQuantity(1, 'quantity${index}')">+</button>
                <span class="error-message" id="error-quantity${index}">請選擇數量！</span>
            </div>
            <button class="add-btn" onclick="addToCart('${item.id}', 'quantity${index}')">加入</button>
        </div>
        `
        )
        .join("");
}

function setActive(element, category) {
    const categories = document.querySelectorAll('.categories span');
    categories.forEach(cat => cat.classList.remove('active'));
    element.classList.add('active');
    currentCategory = category;
    fetchMenuData(category); // 載入新分類的菜單
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
    updateTotalPrice();
}

// 修改加入購物車函數，顯示錯誤提示
function addToCart(itemId, quantityId) {
    const quantityInput = document.getElementById(quantityId);
    const quantity = parseInt(quantityInput.value);

    // 取得錯誤訊息顯示區域
    const errorMessageElement = document.getElementById(`error-${quantityId}`);

    // 清除先前的錯誤訊息
    if (errorMessageElement) {
        errorMessageElement.style.display = "none";
    }

    if (quantity > 0) {
        // 發送請求將商品加入購物車並更新後端資料
        const orderItem = {
            menuItemId: itemId,  // 商品的 id
            quantity: quantity   // 選擇的數量
        };
        fetch(`/api/cart/${customerId}/add`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(orderItem)
        })
        .then(response => {
            if (response.ok) {
                alert(`已加入 ${quantity} 件商品至購物車 (商品ID: ${itemId})`);
                quantityInput.value = 0;
                updateTotalPrice(); // 重新計算並顯示總金額
            } else {
                alert("加入購物車失敗！");
            }
        })
        .catch(error => {
            console.error("無法加入購物車:", error);
        });
    } else {
        // 顯示錯誤訊息
        if (errorMessageElement) {
            errorMessageElement.style.display = "block";
        }
    }
}


// 即時計算總金額
function updateTotalPrice() {
    let total = 0;
    cartItems.forEach(item => {
        total += item.quantity * item.price;
    });

    // 顯示總金額
    document.getElementById("totalPrice").innerText = `總金額: $${total.toFixed(2)}`;

    // 更新後端總金額（每次改變後端資料都同步更新）
    fetch(`/api/cart/${customerId}/total`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ totalPrice: total })
    })
    .then(response => {
        if (!response.ok) {
            console.error("無法更新總金額至後端");
        }
    })
    .catch(error => {
        console.error("無法同步總金額至後端:", error);
    });
}

function redirectToOrderPage() {
    window.location.href = "orders.html";
}

document.getElementById('ordersBtn').addEventListener('click', redirectToOrderPage);

