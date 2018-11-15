var timeMark;

var chatInit = function (userId, lobbyId) {
    $('#vchatsubmit').click(function () {
        var msg = $('#vchatmsg').val();
        $('#vchatmsg').val("");
        $.ajax
        (
            {
                type:'POST',
                url: '/chat/send',
                contentType: "application/json",
                dataType: 'json',
                data:JSON.stringify({message: msg, userId: userId, lobbyId: lobbyId}),
                cache: false,
                async: false,
                success:function(result){

                },
                error: function(request, status, error) {
                    var statusCode = request.status; // вот он код ответа
                    console.log(statusCode);
                }
            }
        );
    });
    timeMark = 0;
    setInterval(chatReceiveMsg, 1100, lobbyId);
};

var chatReceiveMsg = function (lobbyId) {
    $.ajax
    (
        {
            type:'POST',
            url: '/chat/receive',
            contentType: "application/json",
            dataType: 'json',
            data:JSON.stringify({lobbyId: lobbyId, timeMark: timeMark}),
            cache: false,
            async: false,
            success:function(result){
                var chat = $('#vchat').val();
                $('#vchat').val(chat + result.data);
                timeMark = result.timeMark;
            },
            error: function(request, status, error) {
                var statusCode = request.status; // вот он код ответа
                console.log(statusCode);
            }
        }
    );
};