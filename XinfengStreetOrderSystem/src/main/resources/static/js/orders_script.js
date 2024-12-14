// 獲取客戶 ID
const customerId = "TestID"; // 替換為實際的客戶 ID

$(document).ready(function() {
    $("#pickup-date").datepicker({
        dateFormat: "yy-mm-dd"
    });

    // 加載點菜單
    loadOrderItems();

    // 返回按鈕事件
    $("#backButton").click(function() {
        window.location.href = "browseMenu.html";
    });

    // 送出按鈕事件
    $("#submit-button").click(function() {
        submitOrder();
    });
});

function loadOrderItems() {
    $.ajax({
        url: `/api/cart/${customerId}`,
        method: "GET",
        success: function(data) {
            console.log("Cart data retrieved:", data); // 日誌輸出確認
            displayOrderItems(data.items);
            updateTotalAmount(data.totalPrice);
        },
        error: function() {
            alert("無法加載點菜單。");
        }
    });
}

function displayOrderItems(items) {
    const container = $("#order-items-container");
    container.empty();
    items.forEach(function(item, index) {
        const itemHTML = `
            <div class="order-item" data-index="${index}">
                <div class="item-image">
                    <img src="images/${item.menuItemId}.jfif" alt="${item.menuItemId} 圖片" />
                </div>
                <div class="item-details">
                    <p class="item-name">${item.menuItemId}</p>
                    <p class="item-price">$${item.price}</p>
                    <div class="item-quantity">
                        <label for="quantity${index}">數量：</label>
                        <button class="quantity-btn" onclick="decreaseQuantity('quantity${index}')">-</button>
                        <input type="text" id="quantity${index}" value="${item.quantity}" readonly>
                        <button class="quantity-btn" onclick="increaseQuantity('quantity${index}')">+</button>
                    </div>
                </div>
            </div>
        `;
        container.append(itemHTML);
    });
}

function updateTotalAmount(total) {
    $("#total-amount").text(`總金額：$${total}`);
}

function submitOrder() {
    const updatedCart = {
        items: []
    };
    $("#order-items-container .order-item").each(function() {
        const index = $(this).data("index");
        const quantity = parseInt($(`#quantity${index}`).val());
        const menuItemId = $(this).find(".item-name").text();
        updatedCart.items.push({ menuItemId, quantity });
    });

    $.ajax({
        url: `/api/cart/${customerId}/submit`,
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(updatedCart),
        success: function() {
            alert("訂單提交成功！");
            window.location.href = "state.html";
        },
        error: function() {
            alert("訂單提交失敗。");
        }
    });
}
