$(document).ready(function() {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    /**
     * Checks login and password writed by user by ajax
     */
    $('#authfrm').submit(
        function(event) {
            var blockSubmit = true;
            $.ajax
            (
                {
                    type:'POST',
                    url:'/login/checkuser',
                    contentType: "application/json",
                    dataType: 'json',
                    data:JSON.stringify({name:$('#vname').val()}),
                    cache:false,
                    async:false,
                    success:function(result){
                        if(result.status == "fail" ) {
                            blockSubmit = true;
                        }
                        else {
                            var res1;
                            try {
                                checkpw($("#vpassword").val(), result.data, function (res) {res1 = res;});
                            } catch (err) {
                                res1 = false;
                            }
                            blockSubmit = !res1;
                            if (!blockSubmit) {
                                $("#authfrm").hide();
                                $("#vpassword").val(result.data);
                            }
                        }
                    },
                    error: function(request, status, error) {
                        blockSubmit = true;
                        var statusCode = request.status; // вот он код ответа
                        console.log(statusCode);
                    }
                }
            );
            if(blockSubmit) {
                $('#comment').html("<p>Исправьте имя пользователя или пароль</p>");
                return false;
            }else{
                return true;
            }
        });
});

