$(document).ready(function () {
    const userId = sessionStorage.getItem("userId"); // 假設 userId 是儲存在 sessionStorage 中

    function fetchOrderHistory() {
        $.ajax({
            url: `/api/orderHistory/${userId}`,
            method: 'GET',
            success: function (data) {
                renderOrderHistory(data);
            },
            error: function (error) {
                console.error('Error fetching order history:', error);
            }
        });
    }

    function renderOrderHistory(orders) {
        const container = $('#order-history-container');
        container.empty();

        orders.forEach(order => {
            const orderItem = $(`
                <div class="order-item">
                    <div class="order-details">
                        <div class="order-number">訂單號碼：${order.orderNumber}</div>
                        <div class="order-date">訂單日期：${new Date(order.orderedAt).toLocaleString()}</div>
                        <div class="order-status">訂單狀態：${order.orderStatus}</div>
                        <div class="total-price">總金額：${order.totalPrice.toFixed(2)}</div>
                        <div class="order-items">
                            ${order.items.map(item => `
                                <div class="order-item-name">${item.name} (${item.price.toFixed(2)}) x ${item.quantity}</div>
                                ${item.setContents ? `<div class="set-content">${item.setContents}</div>` : ''}
                            `).join('')}
                        </div>
                    </div>
                    <a href="/MealRating.html?orderId=${order.orderId}" class="rating-btn">評價</a>
                </div>
            `);
            container.append(orderItem);
        });
    }

    $('#backButton').click(function () {
        window.history.back();
    });

    fetchOrderHistory();
});
