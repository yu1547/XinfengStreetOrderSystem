document.addEventListener('DOMContentLoaded', function() {
    const registerForm = document.getElementById('register-form');
    const verificationSection = document.getElementById('verification-section');
    const verificationCodeInput = document.getElementById('verification-code');
    const verifyCodeBtn = document.getElementById('verify-code-btn');
    const resendCodeBtn = document.getElementById('resend-code-btn'); // 重新寄送按鈕
    const registerBtn = document.getElementById('register-btn');
    const messageDisplay = document.getElementById('message'); // 顯示提示訊息

    let email; // 保存使用者輸入的 email

    // 表單提交事件 - 發送驗證碼
    registerForm.addEventListener('submit', async function(event) {
        event.preventDefault();

        // 檢查所有必填欄位
        email = document.getElementById('email').value.trim();
        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value.trim();
        const name = document.getElementById('name').value.trim();
        const age = document.getElementById('age').value.trim();
        const gender = document.querySelector('input[name="gender"]:checked');

        if (!email || !username || !password || !name || !age || !gender) {
            alert('請填寫所有欄位！');
            return;
        }

        try {
            // 禁用註冊按鈕並顯示提示訊息
            registerBtn.disabled = true;
            messageDisplay.textContent = '寄送驗證碼至您的信箱中，請稍後...';

            // 發送驗證碼
            const response = await fetch(`/api/mail/sendVerificationCode?email=${encodeURIComponent(email)}`, {
                method: 'POST'
            });

            if (response.ok) {
                messageDisplay.textContent = '驗證碼已寄送至您的信箱，請檢查郵件並輸入驗證碼';
                verificationSection.style.display = 'block';
                resendCodeBtn.style.display = 'inline-block'; // 顯示重新寄送按鈕
            } else {
                messageDisplay.textContent = '驗證碼發送失敗，請檢查郵件地址是否正確。';
                registerBtn.disabled = false; // 啟用註冊按鈕
            }
        } catch (error) {
            console.error('Error sending verification code:', error);
            messageDisplay.textContent = '發送驗證碼時發生錯誤，請稍後再試。';
            registerBtn.disabled = false; // 啟用註冊按鈕
        }
    });

    // 驗證碼驗證事件
    verifyCodeBtn.addEventListener('click', async function () {
        const verificationCode = verificationCodeInput.value.trim();

        if (!verificationCode) {
            alert('請輸入驗證碼');
            return;
        }

        try {
            const verifyResponse = await fetch('/api/mail/verifyCode', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, verificationCode })
            });

            if (verifyResponse.ok) {
                messageDisplay.textContent = '驗證成功，正在完成註冊...';

                // 提交註冊資訊
                const username = document.getElementById('username').value.trim();
                const password = document.getElementById('password').value.trim();
                const name = document.getElementById('name').value.trim();
                const age = document.getElementById('age').value.trim();
                const gender = document.querySelector('input[name="gender"]:checked').value;

                const registerResponse = await fetch('/api/users/register', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        email,
                        username,
                        password,
                        name,
                        age,
                        gender
                    })
                });

                if (registerResponse.ok) {
                    alert('註冊成功！即將跳轉到登入頁面...');
                    window.location.href = '/login.html';
                } else {
                    messageDisplay.textContent = '註冊失敗，請稍後再試。';
                }
            } else {
                messageDisplay.textContent = '驗證碼錯誤，請重新輸入';
            }
        } catch (error) {
            console.error('Error verifying code or registering:', error);
            messageDisplay.textContent = '發生錯誤，請稍後再試。';
        }
    });

    // 重新寄送驗證碼事件
    resendCodeBtn.addEventListener('click', async function () {
        try {
            messageDisplay.textContent = '重新寄送驗證碼中，請稍後...';

            const response = await fetch(`/api/mail/sendVerificationCode?email=${encodeURIComponent(email)}`, {
                method: 'POST'
            });

            if (response.ok) {
                messageDisplay.textContent = '驗證碼已重新寄送至您的信箱，請檢查郵件。';
            } else {
                messageDisplay.textContent = '重新寄送驗證碼失敗，請稍後再試。';
            }
        } catch (error) {
            console.error('Error resending verification code:', error);
            messageDisplay.textContent = '發送錯誤，請稍後再試。';
        }
    });
});
