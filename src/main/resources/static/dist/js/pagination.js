//Получение информации о текущем состоянии страницы - текущий номер, размер и фильтры
//Если информация уже сохранялась в history.state (через функцию saveState), она берется оттуда, иначе дефолтная
let currentPageNumber, currentPageSize, pageFiltersString;
if (history.state != null) {
    currentPageNumber = history.state.page;
    currentPageSize = history.state.size;
    pageFiltersString = history.state.filters;

    setFilters(pageFiltersString);
} else {
    currentPageNumber = 1;
    currentPageSize = 10;
    pageFiltersString = '';
}

//сбор фильтров со страницы
function gatherFilters() {

    let active = $("#active").val();
    let account = $("#account").val();
    let accountId = $("#accountId").val();
    let address = $("#address").val();
    let apartment_number = $("#apartment").val();
    let building_id = $("#building").val();
    let completed = $("#completed").val();
    let date = $("#date").val();
    let datetime = $("#datetime").val();
    let debt = $("#debt").val();
    let debtSting = $("#debtSting").val();
    let description = $("#description").val();
    let email = $("#email").val();
    let floor = $("#floor").val();
    let id = $("#id").val();
    let month = $("#month").val();
    let master_id = $("#master").val();
    let master_type = $("#master_type").val();
    let name = $("#name").val();
    let number = $("#number").val();
    let owner_id = $("#ownerId").val();
    let phone = $("#phone").val();
    let section_name = $("#section").val();
    let service_id = $("#service").val();
    let status = $("#status").val();
    let role = $("#role").val();
    let ownerName = $("#ownerName").val();
    let buildingName = $("#buildingName").val();
    let isCompleted = $("#isCompleted").val();
    let incomeExpenseItem = $("#incomeExpenseItem").val();
    let incomeExpenseType = $("#incomeExpenseType").val();

    let filterForm = {
        active: (active != null) ? active : null,
        building: (building_id) ? building_id : null,
        service: (service_id) ? service_id : null,
        apartment: (apartment_number) ? apartment_number : null,
        section: (section_name) ? section_name : null,
        id: (id) ? id : null,
        datetime: (datetime) ? datetime : null,
        date: (date) ? date : null,
        month: (month) ? month : null,
        description: (description) ? description : null,
        master_type: (master_type) ? master_type : null,
        master: (master_id) ? master_id : null,
        owner: (owner_id) ? owner_id : null,
        phone: (phone) ? phone : null,
        status: (status) ? status : null,
        completed: (completed != null) ? completed : null,
        debt: (debt != null) ? debt : null,
        debtSting: (debtSting != null) ? debtSting : null,
        name: (name) ? name : null,
        role: (role) ? role : null,
        email: (email) ? email : null,
        address: (address) ? address : null,
        number: (number) ? number : null,
        floor: (floor) ? floor : null,
        account: (account) ? account : null,
        accountId: (accountId) ? accountId : null,
        ownerName: (ownerName) ? ownerName : null,
        buildingName: (buildingName) ? buildingName : null,
        isCompleted: (isCompleted) ? isCompleted : null,
        incomeExpenseItem: (incomeExpenseItem) ? incomeExpenseItem : null,
        incomeExpenseType: (incomeExpenseType) ? incomeExpenseType : null
    };


    return filterForm;
}

function setFilters(filters) {

    if (filters === null) {
        $("#address").val(null);
        $("#account").val(null);
        $("#accountId").val(null);
        $("#building").val('').trigger('change');
        $("#service").val('').trigger('change');
        $("#apartment").val(null);
        $("#section").val('').trigger('change');
        $("#id").val(null);
        $("#month").val(null);
        $("#datetime").val(null);
        $("#date").val(null);
        $("#debt").val('').trigger('change');
        $("#debtSting").val(null);
        $("#email").val(null);
        $("#floor").val(null);
        $("#name").val(null);
        $("#number").val(null);
        $("#role").val(null);
        $("#description").val(null);
        $("#master_type").val('').trigger('change');
        $("#master").val('').trigger('change');
        $("#ownerId").val('').trigger('change');
        $("#phone").val(null);
        $("#status").val('').trigger('change');
        $("#completed").val('').trigger('change');
        $("#active").val('').trigger('change');
        $("#ownerName").val(null);
        $("#buildingName").val(null);
        $("#isCompleted").val('-').trigger('change');
        $("#incomeExpenseItem").val('-').trigger('change');
        $("#incomeExpenseType").val('-').trigger('change');
        return;
    } else {
        $("#address").val(filters.address);
        $("#account").val(filters.account);
        $("#accountId").val(filters.accountId);
        $("#date").val(filters.date);
        $("#debt").val(filters.debt);
        $("#debtSting").val(filters.debtSting);
        $("#email").val(filters.email);
        $("#floor").val(filters.floor);
        $("#name").val(filters.name);
        $("#number").val(filters.number);
        $("#role").val(filters.role);
        $("#building").val(filters.building);
        $("#service").val(filters.service);
        $("#apartment").val(filters.apartment);
        $("#section").val(filters.section);
        $("#id").val(filters.id);
        $("#datetime").val(filters.datetime);
        $("#month").val(filters.month);
        $("#description").val(filters.description);
        $("#master_type").val(filters.master_type);
        $("#master").val(filters.master);
        $("#ownerId").val(filters.owner);
        $("#phone").val(filters.phone);
        $("#status").val(filters.status);
        $("#completed").val(filters.completed);
        $("#active").val(filters.active);
        $("#ownerName").val(filters.ownerName);
        $("#buildingName").val(filters.buildingName);
        $("#isCompleted").val(filters.isCompleted);
        $("#incomeExpenseItem").val(filters.incomeExpenseItem);
        $("#incomeExpenseType").val(filters.incomeExpenseType);

    }

}

//AJAX-вызов и получение данных по url


function drawMessagesTableCabinet() {
    let pageFiltersString = JSON.stringify(gatherFilters());
    let data = getTableData('/myhomecab/cabinet/get-messages', currentPageNumber, currentPageSize, pageFiltersString);
    let $invoicesTableBody = $("#messageTable tbody");
    $invoicesTableBody.html('');
    for (const msg of data.content) {
        let dateString = msg.date.toString();
        let dateParts = dateString.split(",");
        let year = parseInt(dateParts[0]);
        let month = parseInt(dateParts[1]) - 1;
        let day = parseInt(dateParts[2]);
        let hours = parseInt(dateParts[3]);
        let minutes = parseInt(dateParts[4]);
        let date = new Date(year, month, day, hours, minutes);
        let formattedDate = ("0" + date.getDate()).slice(-2) + "." + ("0" + (date.getMonth() + 1)).slice(-2) + "." + date.getFullYear() + " " + ("0" + date.getHours()).slice(-2) + ":" + ("0" + date.getMinutes()).slice(-2);

        let newTableRow = document.createElement('tr');
        if (unreadMessId.includes(msg.id)) {
            newTableRow.style.color = '#191970';
            newTableRow.style.fontWeight = 'bold';
        }
        newTableRow.style.cursor = 'pointer';
        newTableRow.class = 'invoice_row';
        var text = msg.text;
        let strippedStr = text.replace(/(<([^>]+)>)/gi, "");
        var newText = strippedStr.substring(0, 70);
        if (newText.length == 70) {
            newText += '...'
        }
        newTableRow.innerHTML =
            '<td><input type="checkbox" name="" id="" value="' + msg.id + '"></td>' +
            '<td>' + msg.sender.first_name + ' ' + msg.sender.last_name + '</td>' +
            '<td><strong>' + msg.subject + '</strong> - ' + newText + '</td>' +
            '<td>' + formattedDate + '</td>';
        let row_children = newTableRow.children;
        for (let j = 1; j < row_children.length - 1; j++) {
            row_children[j].addEventListener('click', function () {
                window.location.href = '/cabinet/messages/' + msg.id;
            });
        }
        $invoicesTableBody.append(newTableRow);
    }
    if (data.content.length === 0) {
        let newTableRow = document.createElement('tr');
        newTableRow.innerHTML = '<td colspan=10>Ничего не найдено...</td>';
        $invoicesTableBody.append(newTableRow);
    }

    if (totalPagesCount > 1) drawPagination();

}


function drawInvoicesInCabinetTable() {

    let pageFiltersString = JSON.stringify(gatherFilters());
    let data = getTableData('/myhomecab/cabinet/get-invoices-cabinet', currentPageNumber, currentPageSize, pageFiltersString);
    let $invoicesTableBody = $("#invoicesTable tbody");
    $invoicesTableBody.html('');

    for (const invoice of data.content) {
        let date = new Date(invoice.date);
        date.setDate(date.getDate() + 1);
        let newTableRow = document.createElement('tr');
        newTableRow.style.cursor = 'pointer';
        newTableRow.class = 'invoice_row';
        newTableRow.innerHTML =
            '<td>' + invoice.id.toString().padStart(10, '0') + '</td>' +
            '<td>' + date.toISOString().split('T')[0] + '</td>' +
            '<td>' +
            '<small class="label ' + (invoice.status == 'PAID' ? 'label-success' : invoice.status == 'UNPAID' ? 'label-danger' : 'label-warning') + '">' +
            invoice.status +
            '</small>' +
            '</td>' +
            '<td><span>' + invoice.total_price.toString() + '</span></td>';
        let row_children = newTableRow.children;
        for (let j = 1; j < row_children.length - 1; j++) {
            row_children[j].addEventListener('click', function () {
                window.location.href = '/cabinet/invoice/' + invoice.id;
            });
        }

        $invoicesTableBody.append(newTableRow);
    }
    if (data.content.length === 0) {
        let newTableRow = document.createElement('tr');
        newTableRow.innerHTML = '<td colspan=10>Ничего не найдено...</td>';
        $invoicesTableBody.append(newTableRow);
    }

    if (totalPagesCount > 1) drawPagination();

}



function drawRequestsTableCabinet() {

    let pageFiltersString = '';
    let data = getTableData('/myhomecab/cabinet/get-requests', currentPageNumber, currentPageSize, pageFiltersString);

    let $requestsTable = $("#requestsTable tbody");
    $requestsTable.html('');
    for (const request of data.content) {
        //
        // let date = new Date(request.best_time_request.split('T')[0]);
        // let day = date.getDate().toString().padStart(2, '0'); // получаем день месяца, преобразуем в строку и добавляем нуль в начало, если число состоит из одной цифры
        // let month = (date.getMonth() + 1).toString().padStart(2, '0'); // получаем номер месяца (от 0 до 11), прибавляем 1, преобразуем в строку и добавляем нуль в начало, если число состоит из одной цифры
        // let year = date.getFullYear().toString(); // получаем год в виде четырехзначного числа
        // let hours = date.getHours().toString().padStart(2, '0'); // получаем часы, преобразуем в строку и добавляем нуль в начало, если число состоит из одной цифры
        // let minutes = date.getMinutes().toString().padStart(2, '0'); // получаем минуты, преобразуем в строку и добавляем нуль в начало, если число состоит из одной цифры
        // let formattedDate = `${day}.${month}.${year} ${hours}:${minutes}`;
        let newTableRow = document.createElement('tr');
        newTableRow.style.cursor = 'pointer';
        newTableRow.class = 'request_row';

        newTableRow.innerHTML = '<td>' + request.id + '</td>' +
            '<td>' + request.masterTypeName + '</td>' +
            '<td style="max-width: 200px; text-overflow: ellipsis; white-space: nowrap; overflow:hidden">' + request.description + '</td>' +
            '<td>' + request.best_time + '</td>' +
            '<td><small class="label ' + ((request.status === 'Новое') ? 'label-primary' : (request.status === 'В работе') ? 'label-warning' : 'label-success') + '">' + request.status + '</small></td>' +
            '<td>' +
            '<div class="btn-group pull-right">' +
            '<a class="btn btn-default btn-sm" href="/cabinet/request/delete/' + request.id + '"><i class="fa fa-trash" aria-hidden="true"></i></a>' +
            '</div>' +
            '</td>';

        $requestsTable.append(newTableRow);
    }
    if (data.content.length === 0) {
        let newTableRow = document.createElement('tr');
        newTableRow.innerHTML = '<td colspan=10>Ничего не найдено...</td>';
        $requestsTable.append(newTableRow);
    }
    console.log('drawPagination totalPagesCount ' + totalPagesCount);
    if (totalPagesCount > 1) drawPagination();

}

function drawTable() {

    let tableType = globalTableName;
    if (tableType === 'invoices') drawInvoicesTable();
    else if (tableType === 'invoicesInCabinet') drawInvoicesInCabinetTable();
    else if (tableType === 'apartments') drawApartmentsTable();
    else if (tableType === 'accounts') drawAccountsTable();
    else if (tableType === 'meters') drawMetersTable();
    else if (tableType === 'meter_data') drawMeterDataTable();
    else if (tableType === 'requests') drawRequestsTable();
    else if (tableType === 'requestsCabinet') drawRequestsTableCabinet();
    else if (tableType === 'owners') drawOwnersTable();
    else if (tableType === 'buildings') drawBuildingsTable();
    else if (tableType === 'transactions') drawTransactionsTable();
    else if (tableType === 'messagesCabinet') drawMessagesTableCabinet();
    else if (tableType === 'messagesAdmin') drawMessagesTableAdmin();
    else if (tableType === 'admins') drawAdminsTable();
}

//Функции, рисующие таблицы в зависимости от выбранной страницы

//Функции, перерисовывающие таблицы после изменений фильтров/номера страниц/размера страниц
function changeFilterData() {
    pageFiltersString = gatherFilters();
    setFilters(pageFiltersString);
    saveState();
    drawTable();
}

function changePageNumber(newPageNumber) {
    currentPageNumber = newPageNumber;
    saveState();
    drawTable();
}

function increasePageByOne() {
    if (currentPageNumber < totalPagesCount) currentPageNumber++;
    saveState();
    drawTable();
}

function decreasePageByOne() {
    if (currentPageNumber > 0) currentPageNumber--;
    saveState();
    drawTable();
}

function changePageSize(newPageSize) {
    currentPageSize = newPageSize;
    currentPageNumber = 1;
    $(".page-size-display").text(newPageSize);
    drawPagination();

    saveState();
    drawTable();
}

function saveState() {
    let pageState = {
        page: currentPageNumber,
        size: currentPageSize,
        filters: pageFiltersString
    };

    history.replaceState(pageState, null, window.location.href);
    console.log(pageState);
}

function reset() {
    setFilters(null);
    currentPageNumber = 1;
    currentPageSize = 10;
    saveState();
    drawTable();
}

//Функции, перерисовывающие таблицы после изменений фильтров/номера страниц/размера страниц

//Отрисовка пагинации
function drawPagination() {

    let pageOffset = 2; // 1 ... 3 4 -5- 6 7 ... 10 -  current page 5 , offset 2


    let $pagination = $(".pagination_container");
    $pagination.html('');
//    if(totalPagesCount < 1) return;

    let ul = document.createElement('ul');
    ul.classList.add('pagination', 'justify-content-center', 'font-weight-medium');

    //backward navigation buttons (to first page)
    let li = document.createElement('li');
    li.classList.add('page-item');
    if (currentPageNumber === 1) li.classList.add('disabled');
    li.innerHTML = '<a class="page-link" href="#" onclick="changePageNumber(1)" aria-label="Previous"> <span aria-hidden="true">«</span></a>';
    ul.appendChild(li);

    //backward navigation buttons (-1)
    li = document.createElement('li');
    li.classList.add('page-item');
    if (currentPageNumber === 1) li.classList.add('disabled');
    li.innerHTML = '<a class="page-link" href="#" onclick="decreasePageByOne()" aria-label="Previous"> <span aria-hidden="true">‹</span></a>';
    ul.appendChild(li);

    //first page
    li = document.createElement('li');
    li.classList.add('page-item');
    if (currentPageNumber === 1) li.classList.add('active');
    li.innerHTML = '<a class="page-link" href="#" onclick="changePageNumber(1)" aria-label="Previous"> <span aria-hidden="true">1</span></a>';
    ul.appendChild(li);

    // ... block
    if (totalPagesCount > 2 * pageOffset + 5 && currentPageNumber - pageOffset - 1 > 2) {
        li = document.createElement('li');
        li.classList.add('page-item');
        li.classList.add('disabled');
        li.innerHTML = '<a class="page-link" href="#">...</a>';
        ul.appendChild(li);

    }

    //main pages
    let startOfSequence;
    let endOfSequence;

    if (totalPagesCount <= 2 * pageOffset + 5) {
        startOfSequence = 2;
        endOfSequence = totalPagesCount - 1;
    } else {
        startOfSequence = (currentPageNumber > totalPagesCount - 3 - pageOffset + 1) ? totalPagesCount - 3 - 2 * pageOffset + 1 :
            (currentPageNumber - pageOffset - 1 > 2) ? currentPageNumber - pageOffset : 2;
        endOfSequence = (currentPageNumber < 3 + pageOffset) ? 3 + 2 * pageOffset :
            (totalPagesCount - currentPageNumber - pageOffset > 2) ? currentPageNumber + pageOffset : totalPagesCount - 1
    }

    for (let page = startOfSequence; page <= endOfSequence; page++) {
        li = document.createElement('li');
        li.classList.add('page-item');
        if (currentPageNumber === page) li.classList.add('active');
        li.innerHTML = '<a class="page-link" href="#" onclick="changePageNumber(' + page + ')" aria-label="Previous"> <span aria-hidden="true">' + page + '</span></a>';
        ul.appendChild(li);
    }

    // ... block
    if (totalPagesCount > 2 * pageOffset + 5 && totalPagesCount - currentPageNumber - pageOffset > 2) {
        li = document.createElement('li');
        li.classList.add('page-item');
        li.classList.add('disabled');
        li.innerHTML = '<a class="page-link" href="#">...</a>';
        ul.appendChild(li);
    }

    //last page
    if (totalPagesCount > 1) {
        li = document.createElement('li');
        li.classList.add('page-item');
        if (currentPageNumber === totalPagesCount) li.classList.add('active');
        li.innerHTML = '<a class="page-link" href="#" onclick="changePageNumber(' + totalPagesCount + '); drawTable()" aria-label="Previous"> <span aria-hidden="true">' + totalPagesCount + '</span></a>';
        ul.appendChild(li);

    }

    //forward navigation buttons (+1)
    li = document.createElement('li');
    li.classList.add('page-item');
    if (currentPageNumber === totalPagesCount || totalPagesCount === 0) li.classList.add('disabled');
    li.innerHTML = '<a class="page-link" href="#" onclick="increasePageByOne()" aria-label="Previous"> <span aria-hidden="true">›</span></a>';
    ul.appendChild(li);

    //forward navigation buttons (to last page)
    li = document.createElement('li');
    li.classList.add('page-item');
    if (currentPageNumber === totalPagesCount || totalPagesCount === 0) li.classList.add('disabled');
    li.innerHTML = '<a class="page-link" href="#" onclick="changePageNumber(totalPagesCount)" aria-label="Previous"> <span aria-hidden="true">››</span></a>';
    ul.appendChild(li);

    //page size changer
    let sizeChanger = document.createElement('div');
    sizeChanger.classList.add('btn-group');
    sizeChanger.innerHTML = '<button type="button"' +
        'class="btn btn-primary btn-sm dropdown-toggle page-size-display" data-bs-toggle="dropdown"' +
        'aria-expanded="false">' + currentPageSize +
        '</button>' +
        '<ul class="dropdown-menu dropdown-menu-end">' +
        '<li><a class="dropdown-item" href="#" onclick="changePageSize(1)">1</a></li>' +
        '<li><a class="dropdown-item" href="#" onclick="changePageSize(10)">10</a></li>' +
        '<li><a class="dropdown-item" href="#" onclick="changePageSize(25)">25</a></li>' +
        '<li><a class="dropdown-item" href="#" onclick="changePageSize(50)">50</a></li>' +
        '<li><a class="dropdown-item" href="#" onclick="changePageSize(100)">100</a></li>' +
        '</ul>'
    ul.appendChild(sizeChanger);

    $pagination.append(ul);
}

//Установка слушателей на фильтры
$(document).ready(function () {

    $(".my_filters").change(() => changeFilterData());
    $(".datetime_filter").change(function () {
        let datetime = this.value;
        if (datetime.split(' to ').length > 1) changeFilterData();
    });

});
