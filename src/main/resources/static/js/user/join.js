const usernameInput = document.getElementById('username');
const usernameMsg = document.getElementById("username-msg");

usernameInput.addEventListener('input', () => {
    usernameMsg.textContent = '';
    usernameMsg.style.color = '';
})

usernameInput.addEventListener("blur", async (e) => {
    const username = e.target.value.trim();

    try {
        const response = await fetch(`/api/user/check-username?username=${encodeURIComponent(username)}`)
        const result = await response.json();

        if (result.data.exists) {
            usernameMsg.textContent = '이미 사용중인 아이디입니다.'
            usernameMsg.style.color = 'red'
        } else {
            usernameMsg.textContent = '사용 가능한 아이디입니다.'
            usernameMsg.style.color = 'green'
        }

    } catch (error) {
        usernameMsg.textContent = '서버 오류 발생'
        usernameMsg.style.color = 'red';
    }
});

const nicknameInput = document.getElementById('nickname');
const nicknameMsg = document.getElementById("nickname-msg");

nicknameInput.addEventListener("input", (e) => {
    nicknameMsg.textContent = '';
    nicknameMsg.style.color = '';
});

nicknameInput.addEventListener("blur", async (e) => {
    const nickname = e.target.value.trim();
    try {
        const response = await fetch(`/api/user/check-nickname?nickname=${encodeURIComponent(nickname)}`)
        const result = await response.json();
        if (result.data.exists) {
            nicknameMsg.textContent = '이미 사용중인 닉네임입니다.'
            nicknameMsg.style.color = 'red'
        } else {
            nicknameMsg.textContent = '사용 가능한 닉네임입니다.'
            nicknameMsg.style.color = 'green'
        }
    } catch (error) {
        nicknameMsg.textContent = '예상할 수 없는 서버 오류가 발생했습니다.'
        nicknameMsg.style.color = 'red';
    }
});

const emailInput = document.getElementById('email');
const sendCodeBtn = document.getElementById('send-code-btn');
const emailMsg = document.getElementById('email-msg');

emailInput.addEventListener('input', () => {
    emailMsg.textContent = '';
    emailMsg.color = '';
})

sendCodeBtn.addEventListener('click', async () => {
    const email = document.getElementById('email').value.trim();
    if (!email) {
        emailMsg.textContent = "이메일을 입력해 주세요."
        emailMsg.style.color = 'red'
        return;
    }
    const response = await fetch(`/email/send-code?email=${email}`, {
        method: 'post'
    })
    if(!response.ok) { alert('서버 오류 발생'); return }
    const result = await response.json();
    if (!result.success) {
        alert('알 수 없는 오류로 메일 발송에 실패했습니다.')
        return;
    }
    alert('메일 발송 완료!')
})

const verifyCodeBtn = document.getElementById('verify-code-btn');
verifyCodeBtn.addEventListener('click', async () => {
    const code = document.getElementById('code').value.trim();
    const email = document.getElementById('email').value.trim();
    const data = {code, email}
    const response = await fetch(`/email/verify`, {
        method: 'post',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    })
    if(!response.ok) { alert('서버 오류 발생'); return }
    const result = await response.json();
    if (!result.success) {
        alert('알 수 없는 오류로 메일 인증에 실패했습니다.')
        return;
    }
    alert('메일 인증 성공')
    window.emailVerified = true;
})

const joinBtn = document.getElementById('join-btn');
joinBtn.addEventListener('click', async () => {
    const username = usernameInput.value.trim();
    const password = document.getElementById('password').value.trim();
    const email = document.getElementById('email').value.trim();
    const nickname = nicknameInput.value.trim();

    if (!username || !password || !email || !nickname) {
        alert("모든 입력 폼을 입력해주세요.");
        return;
    }

    if (!window.emailVerified) {
        alert('이메일 인증을 완료해 주세요!')
        return;
    }

    const formdata = {username, password, email, nickname}
    try {
        const response = await fetch('/api/user/join', {
            method: 'post',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(formdata)
        })

        if (response.status != 200) {
            throw new Error("회원가입실패")
        }

        alert('회원가입 완료')
        window.location.href = '/user/login'
    } catch (error) {
        alert("회원가입 중 예상할 수 없는 서버 오류가 발생했습니다.")
    }
})