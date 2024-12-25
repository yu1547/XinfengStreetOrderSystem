// 定義全域變數 businessHours
let businessHours = null;
$(document).ready(function() {
    // // 測試用代碼：將測試的 cart 存入 sessionStorage，日後刪除
    // // ---------測試用代碼 (開始)--------
    // const testCart = {
    //     cart: [
    //     {
    //         menuItemId: "675dec118a90d51a011f6308",
    //         quantity: 2
    //     },
    //     {
    //         menuItemId: "675dec798a90d51a011f6309",
    //         quantity: 1
    //     }
    //     ]
    // };
    // sessionStorage.setItem('cart', JSON.stringify(testCart));
    // console.log('測試用 cart 已存入 sessionStorage：', testCart);
    // // ---------測試用代碼 (結束)--------
    
    // 從後端取得營業時間並儲存到全域變數
    $.ajax({
        url: '/api/business-hours',  // 確保這個 URL 是正確的
        method: 'GET',
        success: function(data) {
            businessHours = data;  // 將營業時間資料儲存到全域變數
            console.log('營業時間資料：', businessHours); // 可以檢查一下返回的資料
        },
        error: function(error) {
            if (error.status === 401) { // 如果收到 401 未授權，跳轉到登入頁面 
                window.location.href = 'login.html'; 
            } 
            else { 
                alert("無法獲取營業時間資料");
            }
        }
    });


    $("#pickup-date").datepicker({
        dateFormat: "yy-mm-dd",
        minDate: 0 // 限制最小日期為今天
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
    const cart = JSON.parse(sessionStorage.getItem('cart'));
    if (!cart || !cart.cart || cart.cart.length === 0) {
        alert("購物車是空的！");
        window.location.href = 'browseMenu.html';
        return;
    }

    $.ajax({
        url: "/api/menu/menuItems",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify({ menuItemIds: cart.cart.map(item => item.menuItemId) }),  // 使用物件包裝 menuItemIds
        success: function(menuItems) {
            const updatedCart = menuItems.map(menuItem => {
                const cartItem = cart.cart.find(item => item.menuItemId === menuItem.id);
                return {
                    ...menuItem,
                    quantity: cartItem ? cartItem.quantity : 0
                };
            });
            
            // 在加載菜單項目後計算總金額
            const totalAmount = updatedCart.reduce((sum, item) => sum + item.quantity * item.price, 0);

            // 顯示菜單項目和計算出的總金額
            displayOrderItems(updatedCart);
            updateTotalAmount(totalAmount);  // 傳遞總金額
        },
        error: function() {
            alert("無法加載菜單項目！");
        }
    });
}

  

  function displayOrderItems(items) {
    const container = $("#order-items-container");
    container.empty();
  
    items.forEach(item => {
      const itemHTML = `
        <div class="order-item" data-id="${item.id}" data-price="${item.price}">
          <p class="item-name">${item.name}</p>
          <p class="item-price">單價：$${item.price}</p>
          <div class="quantity-controls">
            <button class="decrease-btn">-</button>
            <input type="text" class="quantity-input" value="${item.quantity}" readonly>
            <button class="increase-btn">+</button>
          </div>
        </div>
      `;
      container.append(itemHTML);
    });
  
    $(".increase-btn").click(function() {
      updateQuantity(this, 1);
    });
  
    $(".decrease-btn").click(function() {
      updateQuantity(this, -1);
    });
    
}
function removeCartItem(itemId) {
    // 從 sessionStorage 獲取購物車
    const cart = JSON.parse(sessionStorage.getItem('cart'));

    if (!cart || !cart.cart) {
        console.error("購物車為空或格式不正確！");
        return;
    }

    // 過濾出不包含該 itemId 的項目
    const updatedCart = cart.cart.filter(item => item.menuItemId !== itemId);

    // 如果購物車變空，提示並清空 sessionStorage
    if (updatedCart.length === 0) {
        alert("購物車已清空！");
        sessionStorage.removeItem('cart');
    } else {
        // 更新購物車
        sessionStorage.setItem('cart', JSON.stringify({ cart: updatedCart }));
    }

    console.log("移除後的購物車：", updatedCart);
}

function updateQuantity(button, change) {
    const itemElement = $(button).closest(".order-item");
    const itemId = itemElement.data("id");
    const itemPrice = itemElement.data("price");
    let quantity = parseInt(itemElement.find(".quantity-input").val());

    // 計算變更後的數量
    if (change === -1 && quantity === 1) {
        if (confirm("數量已是最小值，是否移除該餐點？")) {
            // 移除餐點
            removeCartItem(itemId);
            itemElement.remove();

            // 重新計算總金額
            const totalChange = -itemPrice;
            const currentTotal = parseFloat($("#total-amount").text().replace('總金額：$', '').replace(',', ''));
            const newTotal = currentTotal + totalChange;
            $("#total-amount").text(`總金額：$${newTotal.toFixed(2)}`);
        }
        return;
    }

    quantity += change;
    itemElement.find(".quantity-input").val(quantity);

    // 更新 sessionStorage 中的購物車數量
    const cart = JSON.parse(sessionStorage.getItem('cart'));
    const cartItem = cart.cart.find(item => item.menuItemId === itemId);
    if (cartItem) {
        cartItem.quantity = quantity;
        sessionStorage.setItem('cart', JSON.stringify(cart));
    }

    // 計算總金額的變動
    const totalChange = itemPrice * change;
    const currentTotal = parseFloat($("#total-amount").text().replace('總金額：$', '').replace(',', ''));
    const newTotal = currentTotal + totalChange;

    // 更新顯示的總金額
    $("#total-amount").text(`總金額：$${newTotal.toFixed(2)}`);
}



function updateTotalAmount(totalAmount) {
    $("#total-amount").text(`總金額：$${totalAmount}`);  // 顯示金額保留兩位小數
}


function validateOrder() {
    const note = $("#note").val();
    const pickupDate = $("#pickup-date").val();
    const pickupTime = $("#pickup-time").val();
    const currentDate = new Date();
    const selectedDate = new Date(pickupDate);

    // 檢查備註長度
    if (note.length > 20) {
        alert("備註超過20字！");
        return false;
    }

    // 檢查取餐日期是否為空
    if (!pickupDate) {
        alert("請選擇取餐日期！");
        return false;
    }

    // 檢查取餐時間是否為空
    if (!pickupTime) {
        alert("請選擇取餐時間！");
        return false;
    }

    // 防止選擇過去的日期
    if (selectedDate < currentDate.setHours(0, 0, 0, 0)) {
        alert("取餐日期不能是過去的日期！");
        return false;
    }

    // 如果選擇的是今天，檢查取餐時間是否為過去
    if (selectedDate.toDateString() === new Date().toDateString()) {
        const selectedTime = new Date(`${pickupDate}T${pickupTime}`);
        if (selectedTime < currentDate) {
            alert("取餐時間不能是過去的時間！");
            return false;
        }
    }

    // 檢查是否選擇三天後的日期
    if (selectedDate - currentDate > 3 * 24 * 60 * 60 * 1000) {
        alert("取餐日期只能選擇當前日期的三天內！");
        return false;
    }

    // 取餐時間驗證（營業時間檢查）
    if (!validatePickupTime(businessHours, pickupDate, pickupTime)) {
        return false;
    }

    // 檢查最短提前時間（來自 businessHours）
    const minimumOrderLeadTime = businessHours.minimumOrderLeadTime;
    const selectedTime = new Date(`${pickupDate}T${pickupTime}`);
    const timeDiff = selectedTime - currentDate;

    // 如果是今天，檢查取餐時間是否滿足 minimumOrderLeadTime 的提前時間
    if (selectedDate.toDateString() === currentDate.toDateString() && timeDiff < minimumOrderLeadTime * 60 * 1000) {
        alert(`取餐時間必須為當前時間的 ${minimumOrderLeadTime} 分鐘後！`);
        return false;
    }

    return true;
}

// 取餐時間驗證
function validatePickupTime(businessHours, pickupDate, pickupTime) {
    const selectedDate = new Date(pickupDate + ' ' + pickupTime);
    const dayOfWeek = selectedDate.toLocaleString('en-us', { weekday: 'long' }).toLowerCase();

    // 檢查是否在營業日內
    if (!businessHours.dayOfWeek.includes(dayOfWeek)) {
        alert("選擇的日期不在營業時間內！");
        return false;
    }

    // 檢查時間是否在開店時間內
    const openTime = businessHours.openTime.split(":");
    const closeTime = businessHours.closeTime.split(":");
    const selectedTime = selectedDate.getHours() * 60 + selectedDate.getMinutes();

    const openTimeInMinutes = parseInt(openTime[0]) * 60 + parseInt(openTime[1]);
    const closeTimeInMinutes = parseInt(closeTime[0]) * 60 + parseInt(closeTime[1]);

    if (selectedTime < openTimeInMinutes || selectedTime > closeTimeInMinutes) {
        alert("選擇的時間不在營業時間內！");
        return false;
    }

    return true;
}




function submitOrder() {
    if (!validateOrder()) return;

    const cart = JSON.parse(sessionStorage.getItem('cart'));
    const note = $("#note").val();
    const pickupDate = $("#pickup-date").val();
    const pickupTime = $("#pickup-time").val();

    // 合併日期和時間，並將其格式化為 ISO 格式
    const formattedPickupTime = new Date(`${pickupDate}T${pickupTime}`).toISOString();

    // 計算總金額
    const totalPrice = parseFloat($("#total-amount").text().replace('總金額：$', '').replace(',', ''));

    const order = {
        //如果要前端抓userID的話這裡要加
        items: cart.cart,  // 將 cart 改為 items
        notes: note,
        pickupTime: formattedPickupTime,  // 使用格式化的時間
        totalPrice: totalPrice  // 加入 totalPrice
    };

    console.log("發送的訂單資料：", order);  // 在這裡檢查要發送的資料

    $.ajax({
        url: "/orders",
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(order),
        success: function(response) {
            console.log("後端回應：", response); // 確認後端返回的內容
            alert("訂單提交成功！");
            window.location.href = `state.html?id=${response}`;
        },
        error: function(xhr, status, error) {
            console.error("提交訂單錯誤：", xhr.responseText);  // 查看錯誤訊息
            alert("訂單提交失敗！");
        }
    });
}



