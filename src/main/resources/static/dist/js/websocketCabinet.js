var stompClient = null;

function connect() {
    // var socket = new SockJS('http://localhost:8888/myhome/websocket');
    var socket = new SockJS('http://slj.avada-media-dev1.od.ua:9002/myhome/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected CAB: ' + frame);
        stompClient.subscribe('/topic/messages', function (messageItem) {
            console.log("поймано изменение")
            console.log(messageItem)
            if (typeof globalTableName !== 'undefined' && globalTableName != null && globalTableName === 'messagesCabinet') processReceivedMessageItem(messageItem);
            // processReceivedMessageItem(messageItem);
            var element = document.getElementById("bell");
            element.style.color = "orange";
            updateNewMessage();
            console.log('globalTableName' + globalTableName);
        });
    });
}
function updateNewMessage() {
    var newMsgElement = document.getElementById("new_msg");
console.log('unreadMessageQuantity '+unreadMessageQuantity);



    var newMessageQuantity = parseInt(unreadMessageQuantity) +1;

    console.log('newMessageQuantity '+newMessageQuantity);

    var currentLocale = navigator.language;

    console.log('currentLocale' + currentLocale);

    if (currentLocale === 'uk-UA') {
        if (newMessageQuantity > 0) {
            newMsgElement.innerHTML = "Нових повідомленнь " + newMessageQuantity;
        } else {
            newMsgElement.innerHTML = "Немає нових повідомленнь";
        }
    } else if (currentLocale === 'ru-UA') {
        if (newMessageQuantity > 0) {
            newMsgElement.innerHTML = "Новых сообщений " + newMessageQuantity;
        } else {
            newMsgElement.innerHTML = "Нет новых сообщений";
        }
    }
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
    newTableRow.style.color = '#191970';
    newTableRow.style.fontWeight = 'bold';
    var text = msg.text;
    let strippedStr = text.replace(/(<([^>]+)>)/gi, "");
    var newText = strippedStr.substring(0, 70);
    if (newText.length == 70) {
        newText += '...'
    }
    console.log("msg.subject " + msg.subject)
    console.log("newText " + newText)
    newTableRow.innerHTML =
        '<td><input type="checkbox" name="" id="" value="' + msg.id + '"></td>' +
        '<td>' + msg.sender.full_name + '</td>' +
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