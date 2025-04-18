const loginBtn = document.getElementById("login-btn");

loginBtn.addEventListener('click', async () => {
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value.trim();
    const formData = { username, password };

    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(formData)
        });

        if (response.status !== 200) {
            throw new Error('로그인 실패' + (response.status));
        }

        const result = await response.json();
        const accessToken = result.data.accessToken;
        console.log(accessToken)
        const refreshToken = result.data.refreshToken;
        console.log(refreshToken)
        localStorage.setItem("accessToken", accessToken);
        localStorage.setItem("refreshToken", refreshToken)
        alert('로그인 성공');
        window.location.href = "/";

    } catch (error) {
        alert(error.message);
    }
});
