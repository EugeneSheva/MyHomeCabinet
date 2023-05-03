function loadApartments(input) {
    let owner_id = input.value;
    console.log(owner_id);

    $("#apartmentID").prop("disabled", (owner_id != '0') ? false : true);

    $.get('/admin/owners/get-apartments/' + owner_id, function(data){
      alert("получил квартиры");
      console.log(data);

      $("#apartmentID").html('');

      for(let i = 0; i < data.length; i++) {
        let option = document.createElement("option");
        option.value = data[i].id;
        option.text = 'кв. ' + data[i].number + ', ' + data[i].building.name;
        $("#apartmentID").append(option);
      }
    });
  }

  $(document).ready(function(){
    $("#date, #time").change(function(){
      alert("change");
      console.log($(this).val());
    });

    let owner_input = document.getElementById("ownerID");
    owner_input.addEventListener('change', () => loadApartments(owner_input));

    $("#ownerID").select2({
        ajax: {
            url: '/admin/owners/get-all-owners',
            data: function(params){
                console.log(params.page);
                return {
                    search: params.term || "",
                    page: params.page || 1
                }
            },

        },
        placeholder: 'Выберите...',
        minimumInputLength: 2
    });

    $("#apartmentID, #master_type, #status, #masters").select2({placeholder:'Выберите...', minimumResultsForSearch:Infinity});


    $("#ownerID").change(function(){
      let owner_id = $("#ownerID").val();
      console.log(owner_id);

      $("#apartmentID").prop("disabled", (owner_id != '0') ? false : true);

      $.get('/admin/owners/get-apartment/' + owner_id, function(data){
        alert("получил квартиры");
        console.log(data);

        $("#apartmentID").html('');

        for(let i = 0; i < data.length; i++) {
          let option = document.createElement("option");
          option.value = data[i].id;
          option.text = 'кв. ' + data[i].number + ', ' + data[i].building.name;
          $("#apartmentID").append(option);
        }
      });
    });

    $("#master_type").change(function(){
      let master_type = $(this).val();
      let master_type_name = $( "#master_type option:selected" ).text();
      console.log(master_type.toLowerCase());
      console.log(master_type_name);

      $.get('/admin/admins/get-masters-by-type', {type:master_type}, function(data){
        alert("получил мастеров типа: " + master_type_name);
        console.log(data);

        $("#masters").html('');

        for(let i = 0; i < data.length; i++) {
          let option = document.createElement("option");
          console.log(data[i].role);
          option.value = data[i].id;
          option.text = master_type_name + ' - ' + data[i].first_name + ' ' + data[i].last_name;
          $("#masters").append(option);
        }
      })
    });

    $("#comment").summernote({height:150});
  })