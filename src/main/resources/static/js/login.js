document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('login-form');

    loginForm.addEventListener('submit', async function(event) {
        event.preventDefault();

        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value.trim();

        try {
            const response = await fetch('/api/users/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password })
            });

            if (response.ok) {
                const data = await response.json();
                alert(`登入成功！角色：${data.role}`);
                window.location.href = `${data.redirect}.html`; // 根據後端返回的頁面進行跳轉
            } else {
                const error = await response.json();
                alert(`登入失敗：${error.message}`);
            }
        } catch (error) {
            console.error('Unexpected error:', error);
            alert('登入時發生錯誤，請稍後再試。');
        }
    });
});
