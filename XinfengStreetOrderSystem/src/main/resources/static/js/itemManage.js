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
        const categoriesContainer = document.getElementById("categoriesContainer");

        categoriesContainer.innerHTML = categories
            .map(
                (category, index) =>
                    `<span class="${index === 0 ? "active" : ""}" onclick="setActive(this, '${category}')">${category}</span>`
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
            
        <div class="menu-item" data-id="${item.id}">
            <div class="item-info">
                <div class="item-name-image">
                    <strong>${item.name}</strong>
                    <img src="${item.image}" alt="${item.name}" class="item-image">
                </div>
                <div class="item-details">
                    <div class="item-description-box">
                        <p class="item-description">${item.description}</p>
                    </div>
                    <p class="item-price">$${item.price}</p>
                </div>
            </div>
                <button class="edit-btn" onclick="editMenuItem('${item.id}')">編輯</button>
                <button class="delete-btn" onclick="deleteMenuItem('${item.id}')">刪除</button>
        </div>`
        )
        .join("");
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
            fetchMenuData(currentCategory).then(menuItems => {
                // 如果當前分類已經沒有菜單項目
                if (!menuItems || menuItems.length === 0) {
                    alert("該分類已無任何菜單，重新載入分類列表！");
                    // 重新載入分類並切換到第一個分類
                    fetchCategories();
                }
            });

        })
        .catch(error => {
            console.error("無法刪除菜單項目:", error);
        });
    }
}

// 跳轉到餐點製作清單
function redirectToProductionPage() {
    window.location.href = "Meal Production List.html";
}

document.getElementById('List-btn').addEventListener('click', redirectToProductionPage);
