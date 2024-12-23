document.addEventListener('DOMContentLoaded', function() {
    const registerForm = document.getElementById('register-form');
    const verificationSection = document.getElementById('verification-section');
    const verificationCodeInput = document.getElementById('verification-code');
    const verifyCodeBtn = document.getElementById('verify-code-btn');
    const resendCodeBtn = document.getElementById('resend-code-btn');
    const registerBtn = document.getElementById('register-btn');
    const messageDisplay = document.getElementById('message');
    let email;

    // 表單提交事件 - 發送驗證碼
    registerForm.addEventListener('submit', async function(event) {
        event.preventDefault();

        // 獲取欄位資料
        email = document.getElementById('email').value.trim();
        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value.trim();
        const confirmPassword = document.getElementById('confirm-password').value.trim();
        const name = document.getElementById('name').value.trim();
        const age = document.getElementById('age').value.trim();
        const gender = document.querySelector('input[name="gender"]:checked');

        // 驗證欄位
        if (!email || !username || !password || !confirmPassword || !name || !age || !gender) {
            alert('請填寫所有欄位！');
            return;
        }

        if (!validateEmail(email)) {
            alert('請輸入正確的信箱格式！');
            return;
        }

        if (!validatePassword(password)) {
            alert('密碼至少需包含8個字元，且必須包含字母和數字！');
            return;
        }

        if (password !== confirmPassword) {
            alert('密碼與確認密碼不一致！');
            return;
        }

        try {
            // 檢查帳號是否存在
            const checkResponse = await fetch(`/api/users/check-username?username=${encodeURIComponent(username)}`, {
                method: 'GET',
            });

            const isUsernameTaken = await checkResponse.json();

            if (isUsernameTaken) {
                alert('帳號已存在，請使用其他帳號！');
                return;
            }

            // 禁用按鈕並顯示提示
            registerBtn.disabled = true;
            messageDisplay.textContent = '寄送驗證碼至您的信箱中，請稍後...';

            // 發送驗證碼
            const response = await fetch(`/api/mail/sendVerificationCode?email=${encodeURIComponent(email)}`, {
                method: 'POST'
            });

            if (response.ok) {
                messageDisplay.textContent = '驗證碼已寄送至您的信箱，請檢查郵件並輸入驗證碼';
                verificationSection.style.display = 'block';
                resendCodeBtn.style.display = 'inline-block';
            } else {
                messageDisplay.textContent = '驗證碼發送失敗，請檢查郵件地址是否正確。';
                registerBtn.disabled = false;
            }
        } catch (error) {
            console.error('Error checking username or sending verification code:', error);
            messageDisplay.textContent = '發送驗證碼時發生錯誤，請稍後再試。';
            registerBtn.disabled = false;
        }
    });

    // 驗證碼驗證事件
    verifyCodeBtn.addEventListener('click', async function() {
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
    resendCodeBtn.addEventListener('click', async function() {
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
    
    // 驗證密碼
    function validatePassword(password) {
        const regex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
        return regex.test(password);
    }

    // 驗證信箱
    function validateEmail(email) {
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return regex.test(email);
    }
});
