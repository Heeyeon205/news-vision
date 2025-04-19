window.addEventListener('DOMContentLoaded', async () => {
    try {
        const response = await secureFetch('/api/user/update', {method: 'get'});
        const result = await response.json();
        if (!result.success) {
            alert('서버와 통신 중 데이터를 불러오지 못 했습니다.')
        }
        const data = result.data;
        if (data) {
            const imageDom = document.getElementById('profile-preview');
            const nicknameDom = document.getElementById('nickname');
            const introduceDom = document.getElementById('introduce');

            imageDom.src = data.image;
            nicknameDom.value = data.nickname;
            nicknameDom.defaultValue = data.nickname;
            if (data.introduce) {
                introduceDom.value = data.introduce;
                introduceDom.defaultValue = data.introduce;
            }
        }
    } catch (error) {
        errorEvent(error);
    }
})

const nicknameInput = document.getElementById('nickname');
const nicknameMsg = document.getElementById('nickname-msg');
nicknameInput.addEventListener('input', (e) => {
    nicknameMsg.textContent = ''
    nicknameMsg.style.color = ''
})
nicknameInput.addEventListener('blur', async (e) => {
    const nickname = e.target.value.trim()
    try {
        const response = await fetch(`/api/user/check-nickname?nickname=${encodeURIComponent(nickname)}`);
        const result = await response.json();
        if (result.data.exists) {
            nicknameMsg.textContent = '이미 사용중인 닉네임입니다.'
            nicknameMsg.style.color = 'red'
        } else {
            nicknameMsg.textContent = '사용 가능한 닉네임입니다.'
            nicknameMsg.style.color = 'green'
        }
    } catch (error) {
        errorEvent(error);
    }
})

const introduceInput = document.getElementById('introduce');
document.getElementById('update-btn').addEventListener('click', async (e) => {
    const imageInput = document.querySelector('input[name="image"]');
    const selectedImageFile = imageInput.files[0];
    const curNickname = nicknameInput.value.trim();
    const curIntroduce = introduceInput.value.trim();

    const isImageChanged = !!selectedImageFile;
    const isNicknameChanged = curNickname !== nicknameInput.defaultValue;
    const isIntroduceChanged = curIntroduce !== introduceInput.defaultValue;

    if (!isNicknameChanged && !isIntroduceChanged && !isImageChanged) {
        alert('변경사항이 없습니다.')
        window.location.href = "/user/mypage";
        return
    }

    const formData = new FormData();
    if (isImageChanged) formData.append("image", selectedImageFile);
    if (isNicknameChanged) formData.append("nickname", curNickname);
    if (isIntroduceChanged) formData.append("introduce", curIntroduce);

    try {
        const accessToken = localStorage.getItem('accessToken');
        const response = await fetch("/api/user/update", {
            method: 'post',
            headers: { Authorization: `Bearer ${accessToken}` },
            body: formData
        });
        const result = await response.json();
        if(!result.success){
            alert('서버 오류로 프로필을 편집하지 못 했습니다.')
            return
        }
        alert('프로필 편집 완료!')
        window.location.href="/user/mypage"
    } catch (error) {
        errorEvent(error);
    }
})

