document.addEventListener("DOMContentLoaded", () => {
    fetch('/api/news/main')
        .then(res => res.json())
        .then(result => {
            if (!result.success) {
                throw new Error("뉴스를 불러오지 못했습니다.");
            }

            const list = document.querySelector('.news-list');
            list.innerHTML = ''; // 기존 내용 제거

            result.data.forEach(news => {
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
        })
        .catch(err => {
            alert("⚠️ 메인 뉴스 로딩 실패: " + err.message);
        });
});
