$(document).ready(function () {

    // //  //--------------測試用，後續刪除-------------
    // // // 直接將測試的 userId 設置在 sessionStorage 中
    // sessionStorage.setItem("userId", "673d00e1bfc8a66630f7e513"); // 設置測試用的 userId
    // // //--------------測試用，後續刪除-------------

    // const userId = sessionStorage.getItem("userId"); // 假設 userId 是儲存在 sessionStorage 中

    function fetchOrderHistory() {
        $.ajax({
            url: '/api/orderHistory',  // 不再傳入 userId
            method: 'GET',
            success: function (data) {
                renderOrderHistory(data);
            },
            error: function (error) {
                console.error('Error fetching order history:', error);
    
                if (error.status === 401) {
                    // 如果收到 401 未授權，跳轉到登入頁面
                    window.location.href = 'login.html';
                }
            }
        });
    }

    
    function renderOrderHistory(orders) {
        const container = $('#order-history-container');
        container.empty();
    
        // 先將訂單倒序排列
        orders.reverse();
    
        orders.forEach(order => {
            // 如果備註為 null 或空字串，顯示 "無"
            const notes = order.notes ? order.notes : "無";
    
            const orderItem = $(`
                <div class="order-item">
                    <div class="order-details">
                        <div class="order-number">訂單號碼：${order.orderNumber}</div>
                        <div class="order-date">下單時間：${new Date(order.orderedAt).toLocaleString()}</div>
                        <div class="pickup-date">預計取餐時間：${new Date(order.pickupTime).toLocaleString()}</div>
                        <div class="order-status">訂單狀態：${order.orderStatus}</div>
                        <div class="order-note">備註：${notes}</div>
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
        window.location.href =`browseMenu.html`;
    });

    fetchOrderHistory();
});
