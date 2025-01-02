document.addEventListener("DOMContentLoaded", function() {
  const urlParams = new URLSearchParams(window.location.search);
  const orderId = urlParams.get('orderId'); // 取得 orderId 查詢參數

  if (orderId) {
    fetchOrderDetails(orderId); // 根據 orderId 獲取訂單詳細資料
  }
});

function fetchOrderDetails(orderId) {
  fetch(`/orders/${orderId}`)
    .then(response => {
      if (!response.ok) {
        throw new Error('獲取訂單詳細資料失敗');
      }
      return response.json();
    })
    .then(data => {
      if (data && Array.isArray(data.items)) {
        renderMeals(data.items); // 確保 items 是一個陣列
      } else {
        throw new Error('訂單資料格式錯誤');
      }
    })
    .catch(error => {
      console.error("獲取訂單詳細資料錯誤:", error);
      alert("無法載入訂單資料，請稍後再試！");
    });
}

function renderMeals(items) {
console.log(items);  // 查看返回的 order 物件
const mealsContainer = document.getElementById('meals');
mealsContainer.innerHTML = ''; // 清空現有餐點

items.forEach(item => {
  const row = document.createElement('tr');

  // 呼叫後端 API 來獲取餐點名稱
  fetchMenuItemById(item.menuItemId)
      .then(menuItemName => {
          row.innerHTML = `
              <td>${menuItemName}</td>
              <td>${item.quantity}</td>
              <td>
                  <div class="rating">
                      <input type="radio" id="star5-${item.menuItemId}" name="rating-${item.menuItemId}" value="5"><label for="star5-${item.menuItemId}">★</label>
                      <input type="radio" id="star4-${item.menuItemId}" name="rating-${item.menuItemId}" value="4"><label for="star4-${item.menuItemId}">★</label>
                      <input type="radio" id="star3-${item.menuItemId}" name="rating-${item.menuItemId}" value="3"><label for="star3-${item.menuItemId}">★</label>
                      <input type="radio" id="star2-${item.menuItemId}" name="rating-${item.menuItemId}" value="2"><label for="star2-${item.menuItemId}">★</label>
                      <input type="radio" id="star1-${item.menuItemId}" name="rating-${item.menuItemId}" value="1"><label for="star1-${item.menuItemId}">★</label>
                  </div>
              </td>
              <td style="display: none;" class="menu-item-id">${item.menuItemId}</td>  <!-- 隱藏的 menuItemId 欄位 -->
          `;
          mealsContainer.appendChild(row);  // 將新行添加到表格中
      })
      .catch(error => {
          console.error("獲取餐點名稱錯誤:", error);
      });
});
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
// 提交評分函式
// 提交評分函式
let isSubmitted = false; // 狀態變數，初始值為 false

function submitRatings() {
if (isSubmitted) {
  alert("您已提交評價，無法重複提交！");
  return;
}

const ratings = document.querySelectorAll(".rating");
const mealRatings = [];

ratings.forEach((rating, index) => {
  const selectedInput = rating.querySelector("input:checked");
  const menuItemId = document.querySelectorAll("tbody tr td.menu-item-id")[index].textContent;

  mealRatings.push({
      menuItemId: menuItemId,
      rating: selectedInput ? selectedInput.value : "未評分",
  });
});

console.log("提交的評分資料：", mealRatings);

// 發送評分資料
fetch('/rating/api/ratings', {
  method: 'POST',
  headers: {
      'Content-Type': 'application/json',
  },
  body: JSON.stringify(mealRatings),
})
.then(response => {
  if (!response.ok) {
      throw new Error('Network response was not ok ' + response.statusText);
  }
  return response.json();
})
.then(data => {
  console.log("回應：", data);
  alert("評分已提交，感謝您的回饋！");
  isSubmitted = true; // 設定狀態為已提交
  window.location.href = "OrderHistory.html";
})
.catch(error => {
  console.error('錯誤:', error);
  alert("提交失敗，請稍後再試！");
});
}

