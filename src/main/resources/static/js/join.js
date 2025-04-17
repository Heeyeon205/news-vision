const join_form = document.getElementById('join-form');
join_form.addEventListener('submit', (e) => {
    e.preventDefault()

    const formData = {
        username: e.target.username.value,
        password: e.target.password.value,
        nickname: e.target.nickname.value
    }

    fetch('/api/user/join', {
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            if(response.status !== 200) { throw new Error('회원가입 실패') }
            alert('회원가입 성공!')
            return window.location.href = "/";
        })
        .catch(error => {
            alert(error.message)
        })
})