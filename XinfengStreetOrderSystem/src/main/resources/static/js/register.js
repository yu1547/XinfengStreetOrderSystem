document.addEventListener('DOMContentLoaded', function() {
document.addEventListener('DOMContentLoaded', function() {
const registerForm = document.getElementById('register-form');
const verificationSection = document.getElementById('verification-section');
const verificationCodeInput = document.getElementById('verification-code');
const verifyCodeBtn = document.getElementById('verify-code-btn');
const registerBtn = document.getElementById('register-btn');
    
        registerForm.addEventListener('submit', async function(event) {
            event.preventDefault();
    
            // 檢查所有必填欄位是否填寫
            const username = document.getElementById('username').value.trim();
    
            if (!username) {
                alert('請填寫帳號！');
                return;
            }
    
            try {
                // 檢查帳號是否已存在
                const checkResponse = await fetch(`/api/users/check-username?username=${encodeURIComponent(username)}`, {
                    method: 'GET'
                });
    
                if (checkResponse.ok) {
                    const isTaken = await checkResponse.json();
                    if (isTaken) {
                        alert('帳號已存在，請選擇其他帳號');
                        return;
                    }
                } else {
                    alert('檢查帳號時發生錯誤，請稍後再試。');
                    return;
                }
    
                // 以下是發送驗證碼的邏輯（如果帳號未被使用）
                const email = document.getElementById('email').value.trim();
                const password = document.getElementById('password').value.trim();
                const name = document.getElementById('name').value.trim();
                const age = document.getElementById('age').value.trim();
                const gender = document.querySelector('input[name="gender"]:checked');
    
                if (!email || !password || !name || !age || !gender) {
                    alert('請填寫所有欄位！');
                    return;
                }
    
                const response = await fetch(`/api/mail/sendVerificationCode?email=${encodeURIComponent(email)}`, {
                    method: 'POST'
                });
    
                if (response.ok) {
                    alert('驗證碼已寄送至您的信箱，請檢查郵件並輸入驗證碼');
                    verificationSection.style.display = 'block';
                    registerBtn.disabled = true;
                } else {
                    alert('驗證碼發送失敗，請檢查郵件地址是否正確。');
                }
            } catch (error) {
                console.error('Unexpected error:', error);
                alert('發送驗證碼時發生錯誤，請稍後再試。');
            }
        });
    });
    

    // 點擊驗證碼驗證按鈕的行為
    verifyCodeBtn.addEventListener('click', async function() {
        const email = document.getElementById('email').value.trim();
        const verificationCode = verificationCodeInput.value.trim();

        if (!verificationCode) {
            alert('請輸入驗證碼！');
            return;
        }

        try {
            // 驗證碼驗證請求
            const response = await fetch(`/api/mail/verifyCode?email=${encodeURIComponent(email)}&code=${verificationCode}`, {
                method: 'POST'
            });

            if (response.ok) {
                alert('驗證成功，註冊完成！即將跳轉至登入頁面。');
                window.location.href = 'login.html'; // 跳轉到登入頁面
            } else {
                alert('驗證碼錯誤，請重新嘗試。');
            }
        } catch (error) {
            console.error('Unexpected error:', error);
            alert('驗證過程中發生錯誤，請稍後再試。');
        }
    });
});
