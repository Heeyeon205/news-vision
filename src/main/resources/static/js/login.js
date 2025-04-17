const loginBtn = document.getElementById("login-btn");

loginBtn.addEventListener('click', async () => {
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value.trim();
    const formData = { username, password };

    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });

        if (response.status !== 200) {
            throw new Error('로그인 실패' + (response.status));
        }

        const token = await response.text();
        localStorage.setItem("access_token", token);
        alert('로그인 성공: ' + (token));
        window.location.href = "/";
    } catch (error) {
        alert(error.message);
    }
});
