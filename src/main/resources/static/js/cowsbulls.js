$(document).ready(function() {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    chatInit(userId, lobbyId);

    var validation = function (num) {
        num = num.trim();
        var s = num.match(/\d\d\d\d/);
        if(s == null || s != num) {
            $('#validmessage').html("<p>Исправьте число</p>");
            return false;
        } else {
            $('#validmessage').html("<p></p>");
            return true;
        }
    };

    // Точка входа
    $('#vstepButton').click(function () {
        if(submitStep()) waitAnother();
        return false;
    });

    var stepCounter1 = 0;
    var stepCounter2 = 0;
    var myBulls = "";
    var anotherBulls = "";
    var timoutId = null;

    /**
     * Step by user with write it to database
     * @returns {boolean}
     */
    var submitStep = function () {
        //ожидания шага пользователя
        var number;
        number = $('#vnumber').val();
        if(!validation(number)) return false;

        $('#vnumber').val("");
        $.ajax
        (
            {
                type:'POST',
                url: '/cowsbulls/step',
                contentType: "application/json",
                dataType: 'json',
                data:JSON.stringify({value: number, lobbyId: lobbyId, playerNum: playerNum}),
                cache: false,
                async: false,
                success: function(result) {
                    var a1 = result.data.split(":");
                    myBulls = a1[1];
                    $('#player1').append('<tr> <td>' + (++stepCounter1) + '</td> <td>' + a1[0] + '</td> <td>' + a1[1] + '</td> <td> ' + a1[2] + '</td> </tr>');
                    if(playerNum == 2) checkForWin(myBulls,anotherBulls);
                },
                error: function(request, status, error) {
                    var statusCode = request.status; // вот он код ответа
                    console.log(statusCode);
                }
            }
        );
        return true;
    };

    /**
     * Like a prev. one but it writes on another one game board
     */
    var waitAnother = function () {
    //ожидаем второго пользователя
        $('#vstepButton').prop('disabled', true);
        $.ajax
        (
            {
                type:'POST',
                url: '/cowsbulls/secondstep',
                contentType: "application/json",
                dataType: 'json',
                data:JSON.stringify({value: "", lobbyId: lobbyId, playerNum: playerNum}),
                cache: false,
                async: false,
                success:function(result){
                    if(result.status == "fail") checkForWin("fail","fail");
                    else if (result.status != "wait"){
                        var a = result.data.split(":");
                        anotherBulls = a[1];
                        $('#player2').append( '<tr> <td>' + (++stepCounter2) +'</td> <td>' + a[0] + '</td> <td>'+ a[1] +'</td> <td> '+ a[2] +'</td> </tr>');
                        $('#vstepButton').prop('disabled', false);
                        if(playerNum == 1) checkForWin(myBulls,anotherBulls);
                    }else {
                        timoutId = setTimeout(waitAnother, 1000);
                    }
                },
                error: function(request, status, error) {
                    var statusCode = request.status; // вот он код ответа
                    console.log(statusCode);
                }
            }
        );
    };

    if(playerNum == 2){
        $('#vstepButton').prop('disabled', true);
        timoutId = setTimeout(waitAnother, 1000);
    }

    var addrating = function () {
        $.ajax
        (
            {
                type:'POST',
                url: '/addrating',
                contentType: "application/json",
                dataType: 'json',
                data:JSON.stringify({userId: userId}),
                cache: false,
                success:function(result){},
                error: function(request, status, error) {
                    var statusCode = request.status; // вот он код ответа
                    console.log(statusCode);
                }
            }
        );
    };

    var minusrating = function () {
        console.log(userId + "Minus rating");
        $.ajax
        (
            {
                type:'POST',
                url: '/minusrating',
                contentType: "application/json",
                dataType: 'json',
                data:JSON.stringify({userId: userId}),
                cache: false,
                success:function(result){},
                error: function(request, status, error) {
                    var statusCode = request.status; // вот он код ответа
                    console.log(statusCode);
                }
            }
        );
    };

    var checkForWin = function (myBulls, anotherBulls) {
        if(myBulls != "4" && anotherBulls != "4") return;
        if(myBulls == "4" && anotherBulls == "4"){
            $('#winmessage').html("Ничья");
        }
        if(myBulls == "4" && anotherBulls != "4"){
            $('#winmessage').html("Ты выиграл");
            addrating();
        }
        if(myBulls != "4" && anotherBulls == "4"){
            $('#winmessage').html("Ты проиграл");
            minusrating();
        }
        if (myBulls == "fail" && anotherBulls == "fail") {
            $('#winmessage').html("Другой игрок не вошел");
        }
        clearTimeout(timoutId);
        $('#vstepButton').prop('disabled', true);
        $('#vsubmit').prop('type', 'submit');
    }
});
