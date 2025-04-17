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
            alert('로그인 성공')
            window.location.href = "/main";
        })

        .catch(error => alert(error.message));
});


const token = localStorage.getItem("access_token");
const authArea = document.getElementById("auth-area");
if (token) {
    authArea.innerHTML =
        `<a href="/mypage">마이페이지</a>
         <button onclick="logout()">로그아웃</button>`;
} else {
    authArea.innerHTML = `
            <a href="/login-form">로그인</a>
            <a href="/join-form">회원가입</a> `;
}

function logout() {
    localStorage.removeItem("access_token");
    alert('로그아웃 완료')
    location.href = "/";
}

