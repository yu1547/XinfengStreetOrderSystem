        // 渲染訂單清單
    async function renderOrders(orders) {
            const orderList = document.getElementById('orders');
    orderList.innerHTML = '';
    let totalRevenue = 0;

    const today = new Date();
    today.setHours(today.getHours() + 8); // 將當前時間加 8 小時
    const todayStr = today.toISOString().split('T')[0]; // 獲取加時後的日期

    const todaysOrders = orders.filter(order => {
        const orderPickupDate = new Date(order.pickupTime);
        orderPickupDate.setHours(orderPickupDate.getHours() + 8); // 訂單時間加 8 小時
        const orderPickupDateStr = orderPickupDate.toISOString().split('T')[0];
        return orderPickupDateStr === todayStr;
    });

    todaysOrders.sort((a, b) => new Date(a.pickupTime) - new Date(b.pickupTime));

    for (const order of todaysOrders) {
        const statusClass = 
        order.orderStatus === '已拒絕' ? 'rejected-status' : 
        order.orderStatus === '已完成' ? 'completed-status' : '';
        const row = document.createElement('tr');
        const notesContent = order.notes ? order.notes : '無';
        const pickupTimeContent = order.pickupTime ? new Date(order.pickupTime).toLocaleString() : '未設定';
        
       /* let statusContent = '';
        switch (order.orderStatus) {
            case 'pending':
                statusContent = '待接受';
                break;
            case 'accepted':
                statusContent = '已接受';
                break;
            case 'rejected':
                statusContent = '已拒絕';
                break;
            case 'completed':
                statusContent = '已完成';
                break;
            default:
                statusContent = order.orderStatus;
        }*/

        let itemsContent = '';
        for (const item of order.items) {
            const menuItemName = await fetchMenuItemById(item.menuItemId);
            itemsContent += `${menuItemName} x ${item.quantity}<br>`;
        }

        row.innerHTML = `
            <td>${order.orderNumber}</td>
            <td>${itemsContent}</td>
            <td class="${statusClass}">${order.orderStatus}</td>
            <td>${order.totalPrice}元</td>
            <td>${notesContent}</td>
            <td>${pickupTimeContent}</td>
            <td>
                ${order.orderStatus === '已接受' ? 
                    `<button class="accept-button" onclick="completeOrder('${order.id}')">完成</button>
                     <button class="reject-button" onclick="rejectOrder('${order.id}')">拒絕</button>` : ''}
                ${order.orderStatus === '待接受' ? 
                    `<button class="accept-button" onclick="acceptOrder('${order.id}')">接受</button>
                     <button class="reject-button" onclick="rejectOrder('${order.id}')">拒絕</button>` : ''}
            </td>
        `;
        orderList.appendChild(row);

        if (order.orderStatus === '已完成') {
            totalRevenue += order.totalPrice;
        }
    }

    document.getElementById('revenue').innerText = `本日營業額: ${totalRevenue} 元`;
}



function fetchMenuItemById(menuItemId) {
    return fetch(`/api/menu/id/${menuItemId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('無法獲取餐點資料');
            }
            return response.json();
        })
        .then(data => data.name)  // 返回菜單項目的名稱
        .catch(error => {
            console.error("獲取餐點名稱錯誤:", error);
            return "未知餐點";  // 如果有錯誤，返回預設值
        });
}

        // 取得所有訂單並顯示
        function fetchOrders() {
            fetch('/orders')
                .then(response => response.json())
                .then(data => renderOrders(data))
                .catch(error => console.error('錯誤:', error));
        }

        // 完成訂單
        function completeOrder(orderId) {
            fetch(`/orders/complete?orderId=${orderId}`, { method: 'POST' })
                .then(response => response.json())
                .then(data => {
                    alert(data.message || '訂單已完成');
                    fetchOrders(); // 完成後重新獲取訂單數據
                })
                .catch(error => console.error('錯誤:', error));
        }

        // 接受訂單
        function acceptOrder(orderId) {
            fetch(`/orders/accept?orderId=${orderId}`, { method: 'POST' })
                .then(response => response.json())
                .then(data => {
                    alert(data.message || '訂單已接受');
                    fetchOrders(); // 接受後重新獲取訂單數據
                })
                .catch(error => console.error('錯誤:', error));
        }


      
        // 拒絕訂單
        function rejectOrder(orderId) {
            fetch(`/orders/reject?orderId=${orderId}`, { method: 'POST' })
                .then(response => response.json())
                .then(data => {
                    alert(data.message || '訂單已拒絕');
                    fetchOrders(); // 拒絕後重新獲取訂單數據
                })
                .catch(error => console.error('錯誤:', error));
        }

        // 頁面加載時，獲取訂單列表
        window.onload = fetchOrders;