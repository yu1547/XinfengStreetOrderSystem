let currentCategory = ""; // 當前分類
let fullMenuData = []; // 全部菜單資料，用於搜尋功能

// 初始載入
document.addEventListener("DOMContentLoaded", () => {
    fetchCategories(); // 獲取分類
    fetchMenuData(""); // 預設加載全部菜單
});

// 獲取分類並渲染
async function fetchCategories() {
    try {
        const response = await fetch(`/api/menu/categories`);
        const categories = await response.json();

        // 確保 "套餐" 始終是第一項，並將其放到其他分類之前
        const sortedCategories = ["套餐", ...categories.filter(category => category !== "套餐")];

        const categoriesContainer = document.getElementById("categoriesContainer");

        categoriesContainer.innerHTML = sortedCategories
            .map(
                (category, index) =>
                    `<span class="${index === 0 ? "active" : ""}" onclick="setActive(this, '${category}')">${category}</span>`
            )
            .join("");

        currentCategory = sortedCategories[0]; // 設定第一個分類為預設，這裡是 "套餐"
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
        console.log(menuData);
        if (Array.isArray(menuData) && menuData.length > 0) {
            fullMenuData = menuData;  // 儲存完整菜單資料
            renderMenu(menuData);  // 渲染菜單
        } else {
            console.log("該分類下沒有菜單項目");
            fetchCategories(); // 重新加載分類列表
        }
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
        <div class="menu-item ${item.category === '套餐' ? 'menu-item-set' : ''}" data-id="${item.id}">
            <div class="item-info">
                <strong>${item.name}</strong>
                <img src="${item.image}" alt="${item.name}" class="item-image">
            </div>
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
                <div class="item-details">
                    <div class="item-description-box">
                        <p class="item-description">${item.description}</p>
                    </div>
                    <p class="item-price">$${item.price}</p>
                </div>
                <div class="btn-group">
                    <button class="edit-btn" onclick="editMenuItem('${item.id}')">編輯</button>
                    <button class="delete-btn" onclick="deleteMenuItem('${item.id}')">刪除</button>
                </div>         
        </div>`
        )
        .join("");
}


// 控制套餐內容顯示/隱藏
function togglePackageDetails(itemId) {
    const packageDetails = document.getElementById(`package-details-${itemId}`);
    const arrowButton = document.querySelector(`.menu-item[data-id="${itemId}"] .package-arrow`);

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


// 切換分類標籤功能
function setActive(element, category) {
    // 移除所有標籤的active類
    let categories = document.querySelectorAll('.categories span');
    categories.forEach(function(cat) {
        cat.classList.remove('active');
    });

    // 設置當前點擊的標籤為active
    element.classList.add('active');
    currentCategory = category;

    // 顯示當前分類的菜單項目
    fetchMenuData(category); // 重新獲取並顯示該分類的菜單
}

// 新增餐點按鈕點擊事件
document.querySelector(".new-item-btn").addEventListener("click", redirectToFixMenu);

// 新增餐點並跳轉到 fixmenu 頁面
function redirectToFixMenu() {
    window.location.href = "fixmenu.html"; // 這裡可以傳遞必要的參數，例如空的 id 來表示是新增餐點
}

// 編輯菜單項目
function editMenuItem(itemId) {
    // 假設編輯菜單項目會跳轉到fixmenu頁面
    window.location.href = `fixmenu.html?id=${itemId}`;
}

// 刪除菜單項目
function deleteMenuItem(itemId) {
    if (confirm("確定要刪除這個餐點嗎？\n(點選確定後請重新整理!!!)")) {
        fetch(`/api/menu/${itemId}`, {
            method: 'DELETE',
        })
        .then(response => response.json())
        .then(data => {
            alert("菜單項目已刪除！");
            // 刪除後重新獲取當前分類的菜單項目
            fetchMenuData(currentCategory); // 這裡不要再傳回menuItems，只需重新獲取
        })
        .catch(error => {
            console.error("無法刪除菜單項目:", error);
        });
    }
}

// 跳轉到餐點製作清單
function redirectToProductionPage() {
    window.location.href = "MealProductionList.html";
}

document.getElementById('List-btn').addEventListener('click', redirectToProductionPage);
