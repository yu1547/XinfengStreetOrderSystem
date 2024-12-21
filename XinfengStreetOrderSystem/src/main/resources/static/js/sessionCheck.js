document.addEventListener('DOMContentLoaded', async function () {
    try {
        const response = await fetch('/api/users/current-user', { method: 'GET' });
        if (response.ok) {
            const data = await response.json();
            console.log('當前登入使用者:', data);
        } else {
            alert('尚未登入或登入過期，請重新登入');
            localStorage.removeItem('loggedInUser');
            window.location.href = 'login.html';
        }
    } catch (error) {
        console.error('Session 檢查錯誤:', error);
        localStorage.removeItem('loggedInUser');
        alert('系統錯誤，請重新登入');
        window.location.href = 'login.html';
    }
});
