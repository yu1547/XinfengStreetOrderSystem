const orders = [
    { id: 5, item: '原味蛋餅*1<br>原味蛋餅套餐*1', status: '製作中', amount: 80 },
    { id: 4, item: '原味蛋餅*1<br>原味蛋餅套餐*1', status: '已完成', amount: 80 },
    { id: 3, item: '薯餅*1', status: '已拒絕', amount: 20 },
];

function renderOrders() {
    const orderList = document.getElementById('orders');
    orderList.innerHTML = '';
    let totalRevenue = 0; // 營業額變數
    orders.forEach(order => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${order.id}</td>
            <td>${order.item}</td>
            <td>${order.status}</td>
            <td>${order.amount}元</td>
            <td>
                ${order.status === '製作中' ? `<button onclick="completeOrder(${order.id})">完成</button>` : ''}
                ${order.status === '待接受' ? `
                    <button onclick="acceptOrder(${order.id})">接受</button>
                    <button onclick="rejectOrder(${order.id})">拒絕</button>` : ''}
            </td>
        `;
        orderList.appendChild(row);

        // 如果訂單已完成，將金額加到營業額
        if (order.status === '已完成') {
            totalRevenue += order.amount;
        }
    });
    // 更新營業額顯示
    document.getElementById('revenue').innerText = `本日營業額: ${totalRevenue} 元`;
}

function completeOrder(id) {
    const orderIndex = orders.findIndex(order => order.id === id);
    if (orderIndex !== -1) {
        const order = orders[orderIndex];
        order.status = '已完成';
        orders.splice(orderIndex, 1); // 移除該訂單
        const rejectIndex = orders.findIndex(order => order.status === '已拒絕');
        if (rejectIndex === -1) {
            orders.push(order);
        } else {
            orders.splice(rejectIndex, 0, order);
        }
        renderOrders(); // 更新顯示
    }
}

function acceptOrder(id) {
    const order = orders.find(order => order.id === id);
    if (order) {
        order.status = '製作中';
        renderOrders();
    }
}

function rejectOrder(id) {
    const order = orders.find(order => order.id === id);
    if (order) {
        order.status = '已拒絕';
        orders.splice(orders.indexOf(order), 1);
        orders.push(order);
        renderOrders();
    }
}

// 模擬新增訂單的函數
function addNewOrder(item, amount) {
    const newOrder = { id: Math.max(...orders.map(o => o.id)) + 1, item: item, status: '待接受', amount: amount };
    orders.unshift(newOrder);
    renderOrders();
}

// 測試：添加幾個新的訂單
addNewOrder('原味蛋餅*1<br>原味蛋餅套餐*1', 80);
addNewOrder('原味蛋餅*1<br>原味蛋餅套餐*1', 80);
addNewOrder('原味蛋餅*1<br>原味蛋餅套餐*1', 80);

// 初始渲染訂單
renderOrders();
