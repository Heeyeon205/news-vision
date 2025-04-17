const token = localStorage.getItem("access_token");
const authArea = document.getElementById("auth-area");
if (token) {
    authArea.innerHTML =`
         <a href="/admin/manage">관리자 페이지</a>
        <a href="/user/mypage">마이페이지</a>
         <button onclick="logout()">로그아웃</button>`;
} else {
    authArea.innerHTML = `
            <a href="/news/main">메인</a>
            <a href="/news/article">아티클</a>
            <a href="/board/boards">커뮤니티</a>
            <a href="/user/login">로그인</a>
            <a href="/user/join">회원가입</a> 
`;
}

function logout() {
    localStorage.removeItem("access_token");
    alert('로그아웃 완료')
    location.href = "/";
}