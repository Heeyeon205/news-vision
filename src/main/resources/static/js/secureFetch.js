async function secureFetch(url, options = {}) {
    const accessToken = localStorage.getItem("accessToken");
    const refreshToken = localStorage.getItem("refreshToken");

    options.headers = {
        ...options.headers,
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "application/json"
    };

    let response = await fetch(url, options);

    if (response.status === 401 && refreshToken) {
        const refreshResponse = await fetch("/api/auth/refresh", {
            method: "post",
            headers: { "refreshToken": refreshToken }
        });

        const refreshData = await refreshResponse.json();

        if (refreshResponse.ok && refreshData.success) {
            const newAccessToken = refreshData.data;
            localStorage.setItem("accessToken", newAccessToken);

            options.headers.Authorization = `Bearer ${newAccessToken}`;
            response = await fetch(url, options);
        } else {
            alert("로그인이 만료되었습니다. 다시 로그인해 주세요.");
            location.href = "/user/login";
        }
    }
    return response;
}
