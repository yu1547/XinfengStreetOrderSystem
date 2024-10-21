document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('register-form').addEventListener('submit', function(event) {
        event.preventDefault(); // 阻止表單的預設提交行為

        // 假設註冊成功後直接跳轉到登入頁面
        alert('註冊成功，將跳轉到登入頁面！');
        window.location.href = 'login.html'; // 跳轉到登入頁面
    });
});
