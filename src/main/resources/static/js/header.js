const accessToken = localStorage.getItem("accessToken");
const authArea = document.getElementById("auth-area");
if (accessToken) {
    authArea.innerHTML = `
          <a href="/news/article">아티클</a>
          <a href="/news/ai">3분요약</a>
          <a href="/board/boards">커뮤니티</a>
          <input type="text"  value="검색" />
          <a href="/notice">알림</a>
          <a href="/admin/manage">관리자 페이지</a>
           <button onclick="moveToMypage()">마이페이지</button>
           <button onclick="logout()">로그아웃</button>
 `;
} else {
    authArea.innerHTML = `
            <a href="/news/article">아티클</a>
            <a href="/board/boards">커뮤니티</a>
            <a href="/user/login">로그인</a>
            <a href="/user/join">회원가입</a> 
`;
}

function logout() {
    const accessToken = localStorage.getItem("accessToken");
    const refreshToken = localStorage.getItem("refreshToken");
    localStorage.removeItem("refreshToken");

    fetch("/api/auth/logout", {
        method: "POST",
        headers: { Authorization: `bearer ${accessToken}` }
    })
        .then(() => {
            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");
            alert('로그아웃 성공!')
            location.href = "/"
        });
}

async function moveToMypage()  {
    try {
        const response = await secureFetch('/api/auth/check', {method: 'get'});
        const result = await response.json();
        if (result.success) {
            window.location.href = "/user/mypage";
        } else {
            alert('로그인 후 이용해 주세요.')
        }
    }catch(error){
        console.log('에러: ', error)
        alert('서버와 통신 중 문제가 발생했습니다.')
    }
}
