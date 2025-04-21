window.addEventListener("DOMContentLoaded", async () => {
    try {
        const response = await secureFetch('/api/mypage', {method: 'get'});
        const result = await response.json();
        if (!result.success) {
            alert('서버와 통신 중 문제가 발생했습니다.')
            return
        }
        const data = result.data;
        if (data) {
            document.getElementById('profile-image').src = data.image;
            document.getElementById('nickname').textContent = data.nickname;
            const introduce = data.introduce;
            document.getElementById('introduce').textContent = introduce ? introduce : '';
            document.getElementById('icon').src = data.icon;
            document.getElementById('icon-title').textContent = data.title;
            document.getElementById('follower-count').textContent = data.followerCount;
            document.getElementById('following-count').textContent = data.followingCount;
        }
    } catch (error) {
        errorEvent(error);
    }
});

const editBtn = document.getElementById('edit-btn');
editBtn.addEventListener('click', async () => {
    try {
        const response = await secureFetch('/api/auth/check', {method: 'get'});
        const result = await response.json();
        if (!result.success) {
            alert('서버와 통신 중 문제가 발생했습니다.')
        }
        window.location.href = '/user/update';
    } catch (error) {
        errorEvent(error);
    }
})

const deleteBtn = document.getElementById('delete-btn');
deleteBtn.addEventListener('click', async (e) => {
    const check = confirm('회원을 탈퇴하시겠습니까?');
    if (!check) {
        return
    }
    try {
        const response = await secureFetch('/api/user/delete', {method: 'delete'});
        const result = await response.json();
        if (!result.success) {
            alert('서버와 통신 중 문제가 발생했습니다.')
            return
        }
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        alert('회원 탈퇴 완료');
        window.location.href = "/"
    } catch (error) {
        errorEvent(error);
    }
})

document.getElementById('follower-count').addEventListener('click', async () => {
    try {
        const response = await secureFetch('/api/auth/check', {method: 'get'});
        const result = await response.json();
        if (!result.success) {
            alert('서버와 통신 중 문제가 발생했습니다.')
            return
        }
        window.location.href = "/user/follow?type=foollower"
    } catch (error) {
        errorEvent(error);
    }
})

document.getElementById('following-count').addEventListener('click', async () => {
    try {
        const response = await secureFetch('/api/auth/check', {method: 'get'});
        const result = await response.json();
        if (!result.success) {
            alert('서버와 통신 중 문제가 발생했습니다.')
            return
        }
        window.location.href = "/user/follow?type=foollowing"
    } catch (error) {
        errorEvent(error);
    }
})