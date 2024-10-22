document.addEventListener('DOMContentLoaded', function() {
    document.querySelector('.login-form').addEventListener('submit', function(event) {
        event.preventDefault(); 

        // 登入邏輯驗證 
        //
        alert('登入成功，將跳轉到菜單頁面');
        window.location.href = 'browseMenu.html'; // 跳轉到菜單頁面
    });
});
