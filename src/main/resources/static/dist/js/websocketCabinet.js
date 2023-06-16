var stompClient = null;

function connect() {
    var socket = new SockJS('http://localhost:8880/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected CAB: ' + frame);

        stompClient.subscribe('/topic/messages', function (messageItem) {
            console.log("поймано изменение")
            console.log(messageItem)
            drawMessagesTableCabinet();
            var element = document.getElementById("bell");
            element.style.color = "red";
        });

    });

    // url = 'ws://localhost:8880';
    // const socket = new WebSocket(url);

    // socket.onopen = function(event) {
    //     console.log('WebSocket connection established.');
    // };
    //
    // socket.onmessage = function(event) {
    //     const data = JSON.parse(event.data);
    //     console.log('!!!777z!!!')
    //     // const element = document.getElementById('data-display');
    //     // element.innerText = data.text;
    // };
}



function processReceivedMessageItem(item) {
    console.log('msg 2'+JSON.parse(item.body));
    drawNewMessageRow(JSON.parse(item.body));

}
function drawNewMessageRow(msg) {
    console.log("draw msg table with web socket")
    console.log('msg success');

    let newTableRow = document.createElement('tr');
    newTableRow.style.cursor = 'pointer';
    newTableRow.class = 'invoice_row';
    var index = msg.text.indexOf("<br>");
    var text = msg.text.substring(3, index);
    console.log("text" + text);
    var newText = text.substring(0, 70);
    if (newText.length == 70) {
        newText += '...'
    }
    console.log("newText" + newText)
    newTableRow.innerHTML =
        '<td><input type="checkbox" name="" id="" value="' + msg.id + '"></td>' +
        '<td>' + msg.receiversName + '</td>' +
        '<td><strong>' + msg.subject + '</strong> - ' + newText + '</td>' +
        '<td><p><strong>Новое сообщение</strong></p></td>';
    let row_children = newTableRow.children;
    for (let j = 1; j < row_children.length - 1; j++) {
        row_children[j].addEventListener('click', function () {
            window.location.href = '/admin/messages/' + msg.id;
        });
    }
    console.log('msg drawed');
    $("#messageTable tbody").prepend(newTableRow);

}

$(function () {
    connect();
});