const login_form = document.getElementById("login-form");
login_form.addEventListener('submit',(e) => {
    e.preventDefault();

    const formData = {
        username: e.target.username.value,
        password: e.target.password.value
    };

    fetch('/api/auth/login', {
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })

        .then(response => {
            if(!response.ok) { throw new Error('로그인 실패') }
            return response.text();
        })

        .then(token => {
            localStorage.setItem("access_token", token);
            window.location.href = "/main";
        })

        .catch(error => alert(error.message));
});