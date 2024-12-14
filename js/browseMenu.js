function setActive(element, category) {
    const categories = document.querySelectorAll('.categories span');
    categories.forEach(cat => cat.classList.remove('active'));
    element.classList.add('active');
    const menus = document.querySelectorAll('.menu-list');
    menus.forEach(menu => menu.classList.remove('active'));
    document.getElementById(category).classList.add('active');
}

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
    window.location.href = "orders.html";
}

document.getElementById('ordersBtn').addEventListener('click', redirectToOrderPage);
