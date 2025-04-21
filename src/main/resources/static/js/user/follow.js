window.addEventListener('DOMContentLoaded', async () => {
    const params = new URLSearchParams(location.search);
    const type = params.get('type');
    let url = '';
    if (type === 'follower') {
        url = '/api/user/mypage/follower'
    } else if (type === follwing) {
        url = '/api/user/mypage/following'
    }
    try {
        const response = await secureFetch(url, {method: 'get'});
        const result = await response.json();
        if (!result.success) {
            alert('서버와의 연결을 실패했습니다.')
            return
        }
        const listBox = document.getElementById('follow-list');
        const list = result.data || [];

    if (list.length == 0) {
        listBox.textContent = `<p>데이터가 없습니다.</p>`
    } else {
        listBox.innerHTML = user
            .map(user => {
                const buttonText = user.isFollowing ? "팔로잉" : "팔로우";
                return `<div class="user-box">
                    <img src="${user.image}" alt="프로필 이미지"/>
                    <p>${user.nickname}</p>
                    <button id="follow-btn" data-user-id="${user.id}">
                    ${buttonText}
                    </button>
                    </div>`;
            })
            .json('')
    }
    } catch (error) {
        errorEvent(error)
    }
})