var stompClient = null;
function connect() {
    // var socket = new SockJS('http://localhost:8888/myhome/websocket');
    var socket = new SockJS('http://slj.avada-media-dev1.od.ua:9002/myhome/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/messages', function (messageItem) {

            $.ajax({
                url: '/myhomecab/cabinet/get-unread-messages',
                async: false,
                type: 'GET',
                success: function(response) {
                    unreadMessId = response;
                },
            })

            var mewMessId = JSON.parse(messageItem.body).id
            for (let i = 0; i < unreadMessId.length; i++) {
                if (unreadMessId[i] === mewMessId) {
                    if (typeof globalTableName !== 'undefined' && globalTableName != null && globalTableName === 'messagesCabinet') processReceivedMessageItem(messageItem);
                    // processReceivedMessageItem(messageItem);
                    var element = document.getElementById("bell");
                    element.style.color = "orange";
                    updateNewMessage();
                }
            }
        });
    });
}

function updateNewMessage() {
    var newMsgElement = document.getElementById("new_msg");

    var newMessageQuantity = parseInt(unreadMessageQuantity) +1;

    var currentLocale = navigator.language;

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
    drawNewMessageRow(JSON.parse(item.body));

}
function drawNewMessageRow(msg) {

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

    newTableRow.innerHTML =
        '<td><input type="checkbox" name="" id="" value="' + msg.id + '"></td>' +
        '<td>' + msg.sender.full_name + '</td>' +
        '<td><strong>' + msg.subject + '</strong> - ' + newText + '</td>' +
        '<td><p><strong>Новое сообщение</strong></p></td>';
    let row_children = newTableRow.children;
    for (let j = 1; j < row_children.length - 1; j++) {
        row_children[j].addEventListener('click', function () {
            window.location.href = '/myhomecab/admin/messages/' + msg.id;
        });
    }

    $("#messageTable tbody").prepend(newTableRow);

}

$(function () {
    connect();
});