var stompClient = null;

function connect() {
    var socket = new SockJS('/cashbox-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/cashbox', function (cashboxItem) {
            processReceivedItem(cashboxItem);
        });
    });
}

function processReceivedItem(item) {
    console.log(JSON.parse(item.body));
    drawNewCashboxRow(JSON.parse(item.body));
    recountCashboxBalance();
    recountAccountBalance();
    recountAccountDebts();
}

function drawNewCashboxRow(item) {
    let newTableRow = document.createElement('tr');
    newTableRow.style.cursor = 'pointer';
    newTableRow.class = 'cashbox_row';
    newTableRow.innerHTML =   '<td>' + item.id + '</td>' +
                              '<td>' + item.date + '</td>' +
                              '<td>' + ((item.completed) ? 'Проведен' : 'Не проведен') + '</td>' +
                              '<td>' + item.transactionItemName + '</td>' +
                              '<td>' + item.ownerFullName + '</td>' +
                              '<td>' + item.accountNumber + '</td>' +
                              '<td>' + item.transactionType + '</td>' +
                              '<td>' + item.amount + '</td>' +
                              '<td>' +
                                  '<div class="btn-group" role="group" aria-label="Basic outlined button group">' +
                                      '<a href="/admin/cashbox/edit/' + item.id + '" class="btn btn-default btn-sm"><i class="fa fa-pencil" aria-hidden="true"></i></a>' +
                                      '<a href="/admin/cashbox/delete/' + item.id + '" class="btn btn-default btn-sm"><i class="fa fa-pencil" aria-hidden="true"></i></a>' +
                                  '</div>' +
                              '</td>';
   let row_children = newTableRow.children;
   for(let j = 0; j < row_children.length - 1; j++) {
      row_children[j].addEventListener('click', function(){
          window.location.href = '/admin/cashbox/' + cashbox.id;
      });
   }

    $("#cashboxTable tbody").append(newTableRow);
}

function recountCashboxBalance() {
    $.get("/admin/cashbox/get-cashbox-balance", function(data) {
        $("#cashboxBalance").text(data.toString() + ' грн.');
    });
}

function recountAccountBalance() {
    $.get("/admin/cashbox/get-account-balance", function(data) {
        $("#accountBalance").text(data.toString() + ' грн.');
    });
}

function recountAccountDebts() {
    $.get("/admin/cashbox/get-account-debts", function(data) {
        $("#accountDebts").text(data.toString() + ' грн.');
    });
}

//function disconnect() {
//    if (stompClient !== null) {
//        stompClient.disconnect();
//    }
//    setConnected(false);
//    console.log("Disconnected");
//}

$(function () {
    connect();
});