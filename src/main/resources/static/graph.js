function callFunction() {
    var targetURL = "http://localhost:8181/api/hierarchy";
    $.ajax({
        type: 'GET',
        url: targetURL,
        contentType: "text",
        cache: false,
        success: function (responseJSON) {
            $.each(responseJSON, function (id, data) {
                var html = '<tr>';
                html += '<td class="enable">' + id + '</td>';
                html += '</tr>';
                $('#table_data').append(html);
            });
            return false;
        },
        error: function (e) {
            alert("failed..");
            return false;
        }
    });
}

function callGraphFunction(uri) {
    $('.graph').remove();
    var targetURL = "http://localhost:8181/api/hierarchy/";
    targetURL += uri;
    $.ajax({
        type: 'GET',
        url: targetURL,
        contentType: "text",
        cache: false,
        success: function (responseJSON) {
            $.each(responseJSON, function (id, data) {
                $.each(data, function (k, v) {
                    if (id === v) {

                    } else {
                        var html = '<tr class="graph">';
                        html += '<td >' + v + '</td>';
                        html += '</tr>';
                        $('#table_graph').append(html);
                    }
                });
            });
            return false;
        },
        error: function (e) {
            alert("failed..");
            return false;
        }
    });
}

function searchText() {
    $("#myInput").on("keyup", function () {
        var value = $(this).val().toLowerCase();
        $("#table_data tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
}
