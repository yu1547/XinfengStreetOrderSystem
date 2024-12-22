document.addEventListener('DOMContentLoaded', function () {
    const logoutButton = document.getElementById('logout-button'); // 所有頁面上的登出按鈕有相同的ID

    if (logoutButton) {
        logoutButton.addEventListener('click', async function () {
            // 確認步驟
            const confirmLogout = confirm('確定要登出嗎？');
            if (!confirmLogout) {
                return; // 取消登出
            }

            try {
                const response = await fetch('/api/users/logout', { method: 'POST' }); // 呼叫登出 API
                if (response.ok) {
                    alert('您已成功登出！');
                    localStorage.removeItem('loggedInUser'); // 清除本地儲存的使用者資料
                    window.location.href = 'login.html'; // 跳轉到登入頁面
                } else {
                    alert('登出失敗，請稍後再試。');
                }
            } catch (error) {
                console.error('登出發生錯誤:', error);
                alert('系統錯誤，請稍後再試。');
            }
        });
    }
});
