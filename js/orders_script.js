// 處理數量增加和減少功能
function increaseQuantity(id) {
    var quantityInput = document.getElementById(id);
    var currentValue = parseInt(quantityInput.value);
    quantityInput.value = currentValue + 1;
}

function decreaseQuantity(id) {
    var quantityInput = document.getElementById(id);
    var currentValue = parseInt(quantityInput.value);
    if (currentValue > 1) {
        quantityInput.value = currentValue - 1;
    }
}

// 使用 jQuery UI Datepicker 來選擇日期
$(document).ready(function() {
    $("#pickup-date").datepicker({
        dateFormat: "yy-mm-dd" // 設置日期格式
    });
});

document.getElementById("backButton").addEventListener("click", function() {
    window.location.href = "browseMenu.html";
});

document.querySelector(".submit-btn button").addEventListener("click", function() {
    window.location.href = "state.html";
});
