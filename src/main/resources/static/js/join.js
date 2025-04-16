const join_form = document.getElementById('join-form');
join_form.addEventListener('submit', (e) => {
    e.preventDefault()

    const formData = {
        username: e.target.username.value,
        password: e.target.password.value,
        nickname: e.target.nickname.value
    };

    fetch('api/join', {
        method: 'post',
        header: {
        'Content-Type': 'application/json'
        },
     body: JSON.stringify(formData)
    })
        .then(response => {
            if(!response.ok) { throw new Error('회원가입 실패')}
        })
        })