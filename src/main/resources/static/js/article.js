let page = 0;
let isLoading = false;
let currentType = 'recent';
let currentCategoryId = null;

function loadArticles() {
    if (isLoading) return;
    isLoading = true;

    let url = `/api/news/article?type=${currentType}&page=${page}`;
    if (currentType === 'category' && currentCategoryId) {
        url += `&id=${currentCategoryId}`;
    }

    fetch(url)
        .then(res => res.json())
        .then(result => {
            console.log("🚀 응답 확인:", result.data);

            const list = document.getElementById("article-list");

            if (result.success && result.data.content.length > 0) {
                if (page === 0) {
                    list.innerHTML = ""; // 초기 로딩 시만 비움
                }

                result.data.content.forEach(news => {
                    const item = document.createElement("div");
                    item.classList.add("news-item");
                    item.innerHTML = `
                        <img src="${news.image}" alt="썸네일">
                        <div>
                            <span>${news.category}</span>
                            <h3>${news.title}</h3>
                            <div>
                                <span>${news.author}</span>
                                <span>${new Date(news.createdAt).toLocaleDateString()}</span>
                            </div>
                        </div>
                    `;
                    list.appendChild(item);
                });

                if (!result.data.last) {
                    page++;
                    isLoading = false;
                }
            } else {
                if (page === 0) {
                    list.innerHTML = "<p>표시할 뉴스가 없습니다.</p>";
                }
            }
        });
}

function changeType(type) {
    page = 0;
    currentType = type;
    currentCategoryId = null;
    document.getElementById("article-list").innerHTML = "";
    loadArticles();
}

function changeCategory(id) {
    page = 0;
    currentType = 'category';
    currentCategoryId = id;
    document.getElementById("article-list").innerHTML = "";
    loadArticles();
}

window.addEventListener('scroll', () => {
    if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 200) {
        loadArticles();
    }
});

document.addEventListener('DOMContentLoaded', () => {
    loadArticles();
    checkRoleAndShowButton();  // ✅ 로그인한 유저의 권한에 따라 버튼 노출
});

// ✅ JWT 기반 유저 권한 체크 → 뉴스 작성 버튼 노출
async function checkRoleAndShowButton() {
    try {
        const res = await secureFetch("/api/users/me");
        const result = await res.json();

        if (result.success) {
            const role = result.data.role;
            if (role === 'ROLE_ADMIN' || role === 'ROLE_CREATOR') {
                const btn = document.createElement("button");
                btn.innerText = "뉴스 작성";
                btn.onclick = () => location.href = "/news/write";
                document.getElementById("write-btn-container").appendChild(btn);
            }
        }
    } catch (e) {
        console.error("유저 권한 확인 실패", e);
    }
}

