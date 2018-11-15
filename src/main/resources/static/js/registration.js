var blockSubmit = true;
var passSame = false;

$(document).ready(function() {

    /**
     * init csrf tokens
     * @type {*|jQuery}
     */
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    /**
     * Hashing password on submit and allow to continue
     * or block submit if user mistakes
     */
    $('#regfrm').submit(
        function(event) {
            if(blockSubmit || !passSame) {
                $('#comment').html("<p>Исправьте имя пользователя или пароль</p>");
                return false;
            }
            var rounds = 5;
            var salt;
            try {
                salt = gensalt(rounds);
                hashpw($('#vpassword').val(), salt, function(h) {
                    //$('#form1').hide();
                    $('#vpassword').val(h);
                    $('#vconfirmpassword').hide();
                    $('#vnick').hide();
                    $('#vname').hide();
                    $('#vpassword').hide();
                    $('#vsubmit').hide();
                });
            } catch(err) {
                alert(err);
                return false;
            }
            return true;
        });

    /**
     * when user jumps on next input this ajax checks
     * exists user with this name or not
     */
    $("#vname").focusout(
        function()
        {
            console.log($('#vname').val());
            $.ajax
            (
                {
                    type:'POST',
                    url:'/registration/checkname',
                    contentType: "application/json",
                    dataType: 'json',
                    data:JSON.stringify({name:$('#vname').val()}),
                    cache:false,
                    success:function(result){
                        console.log(result.data);
                        data=result.data;
                        if(data > 0 ) {
                            blockSubmit = true;
                            $('#comment').html("<p>Такой пользователь уже существует</p>");
                        }
                        else {
                            blockSubmit = false;
                            $('#comment').text("");
                        }
                    },
                    error: function(request, status, error) {
                        var statusCode = request.status; // вот он код ответа
                        console.log(statusCode);
                    }
                }
            );
        }
    );

    /**
     * This one checks equals of written passwords
     */
    $("#vconfirmpassword").focusout(
        function()
        {
            if($("#vpassword").val() != $("#vconfirmpassword").val()){
                passSame = false;
                $('#comment').html("<p>Пароли не совпадают</p>");
            }else {
                passSame = true;
                $('#comment').text("");
            }
        }
    );
});

