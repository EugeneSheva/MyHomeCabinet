const queryParams = window.location.search;
console.log(queryParams);
const urlParams = new URLSearchParams(queryParams);
let pageNumber = parseInt(urlParams.get('page'));
console.log('page number ' +pageNumber);



    function checkFilters() {
//        let building_id = $("#building").val();
//        let service_id = $("#service").val();
//        let apartment_number = $("#apartment").val();
//        let section_name = $("#section").val();
//
//        let id = $("#id").val();
//        let datetime = $("#datetime").val();
//        let description = $("#description").val();
//        let master_type = $("#master_type").val();
//        let master_id = $("#master").val();
//        let owner_id = $("#owner").val();
//        let phone = $("#phone").val();
//        let status = $("#status").val();
//
//        const link_elements = [];
//
//        if(building_id != null && building_id != 0) link_elements.push('building='+building_id);
//        if(service_id != null && service_id != 0) link_elements.push('service='+service_id);
//        if(apartment_number != null && apartment_number != 0) link_elements.push('apartment='+apartment_number);
//        if(section_name != null && section_name != '') link_elements.push('section='+section_name);
//
//        if(id != null && id != 0) link_elements.push('id='+id);
//        if(datetime != null && datetime.split('to').length > 1 ) link_elements.push('datetime='+datetime);
//        if(description != null && description != '') link_elements.push('description='+description);
//        if(master_type != null && master_type != '') link_elements.push('master_type='+master_type);
//        if(master_id != null && master_id != 0) link_elements.push('master_id='+master_id);
//        if(owner_id != null && owner_id != 0) link_elements.push('owner_id='+owner_id);
//        if(phone != null && phone != '') link_elements.push('phone='+phone);
//        if(status != null && status != '') link_elements.push('status='+status);
//
//        let final_link = (link_elements.length > 0) ? ('?' + link_elements.join('&')) : '';
//
//        window.location.href = '/admin/requests'+final_link;

        let filters = document.querySelectorAll(".my_filters");
        let link_elements = [];
        for(const filter of filters) {
            console.log(filter);
            let filter_value = filter.value;
            let filter_name = filter.dataset.name;

            if(filter_value != null && filter_value != 0 && filter_value != '') link_elements.push(filter_name + '=' + filter_value);
        }
        let datetime_filter = document.querySelector(".datetime_filter");
        if(datetime_filter != null && datetime_filter.value != null && datetime_filter.value.split(' to ').length > 1 && datetime_filter.value != '') {
            link_elements.push(datetime_filter.dataset.name + '=' + datetime_filter.value);
        }
        pageNumber = 0;
        link_elements.push('page='+pageNumber);
        let final_link = (link_elements.length > 0) ? ('?' + link_elements.join('&')) : '';
        console.log(window.location.href.split('?')[0]);
        window.location.href = window.location.href.split('?')[0]+final_link;
    }

    // ФИЛЬТРЫ
    $(document).ready(function(){

        $(".my_filters").change(() => checkFilters());
        $(".datetime_filter").change(function(){
            let datetime = this.value;
            console.log(datetime);
            console.log(datetime.split(' to '));
            if(datetime.split(' to ').length > 1) checkFilters();
        });
    });