const eventSource = new EventSource("/api/notice/subscribe");

eventSource.addEventListener("notification", function(event) {
    const data = JSON.parse(event.data);
    console.log(data.type);
    console.log(data.title);
    console.log(data.url);
});