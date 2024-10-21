// 切換分類標籤功能
function setActive(element, category) {
    // 移除所有標籤的active類
    let categories = document.querySelectorAll('.categories span');
    categories.forEach(function(cat) {
        cat.classList.remove('active');
    });
    
    // 設置當前點擊的標籤為active
    element.classList.add('active');

    // 隱藏所有的菜單項目
    let menuLists = document.querySelectorAll('.menu-list');
    menuLists.forEach(function(menu) {
        menu.classList.remove('active');
    });

    // 顯示當前分類的菜單項目
    let selectedMenu = document.getElementById(category);
    if (selectedMenu) {
        selectedMenu.classList.add('active');
    }
}