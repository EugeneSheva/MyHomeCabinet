function change() {
    function deleteSelected() {
            var url = "/change-language";

            $.ajax({
                url: url,
                method: 'POST',
                success: function () {
                    location.reload();
                }
            });
    }
}

