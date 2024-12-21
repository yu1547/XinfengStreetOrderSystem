document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('login-form');

    if (loginForm) {
        loginForm.addEventListener('submit', async function(event) {
            event.preventDefault();

            const username = document.getElementById('username').value.trim();
            const password = document.getElementById('password').value.trim();

            if (!username || !password) {
                alert('請輸入帳號與密碼！');
                return;
            }

            try {
                const response = await fetch('/api/users/login', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ username, password })
                });

                if (response.ok) {
                    const data = await response.json();
                    localStorage.setItem('loggedInUser', data.userId); // 將 Session 同步到 Local Storage
                    alert(`登入成功！角色：${data.role}`);
                    window.location.href = `${data.redirect}.html`; // 跳轉頁面
                } else {
                    const error = await response.json();
                    alert(`登入失敗：${error.message}`);
                }
            } catch (error) {
                console.error('登入發生錯誤:', error);
                alert('伺服器錯誤，請稍後再試。');
            }
        });
    }
});
