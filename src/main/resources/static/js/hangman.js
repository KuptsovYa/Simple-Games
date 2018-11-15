$(document).ready(function() {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    chatInit(userId, lobbyId);

    theRotator();

    var validation = function (letter) {
        var s = letter.match(/^[A-Я]/);
        if(s == null || s != letter) {
            $('#validmessage').html("<p>Исправьте на букву</p>");
            return false;
        } else {
            $('#validmessage').html("<p></p>");
            return true;
        }
    };

    $('#vstepButton').click(function () {
        if(submitStep()) waitAnother();
        return false;
    });

    var maxFails = 13;

    var myWord = "";
    var anotherWord = "";
    var myFail = 0;
    var anottherFail = 0;

    var timoutId = null;

    // True, if word doesn't have a '_'
    var checkIfDone = function(word) {
        if(word == "") return false;
        else return (word.match(/_/g)||[]).length == 0;
    };

    /**
     * Step by user with write it to database
     * @returns {boolean}
     */
    var submitStep = function () {
        var letter = $('#vletter').val().toUpperCase();
        if (!validation(letter)) return false;
        $('#vletter').val("");
        $.ajax
        (
            {
                type:'POST',
                url: '/hangman/step',
                contentType: "application/json",
                dataType: 'json',
                data:JSON.stringify({value: letter, lobbyId: lobbyId, playerNum: playerNum}),
                cache: false,
                async: false,
                success: function (result) {
                    var a1 = result.data.split(":");
                    $('#word').html("<p>" + a1[0] + "</p>");
                    if (a1[1] > myFail) rotate(1);
                    myWord = a1[0];
                    myFail = a1[1];
                    checkForWin(myWord, myFail, anotherWord, anottherFail);
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
        $('#vstepButton').prop('disabled', true);
        $.ajax
        (
            {
                type:'POST',
                url: '/hangman/secondstep',
                contentType: "application/json",
                dataType: 'json',
                data:JSON.stringify({value: "", lobbyId: lobbyId, playerNum: playerNum}),
                cache: false,
                async: false,
                success:function(result){
                    if(result.status == "fail") checkForWin("fail","fail");
                    else if (result.status != "wait" ){
                        var a2 = result.data.split(":");
                        $('#word2').html("<p>" + a2[0] + "</p>");
                        if(a2[1] > anottherFail) rotate(2);
                        $('#vstepButton').prop('disabled', false);
                        anotherWord = a2[0]; anottherFail = a2[1];
                        checkForWin(myWord, myFail, anotherWord, anottherFail);
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

    if(playerNum == "2"){
        $('#vstepButton').prop('disabled', true);
        timoutId = setTimeout(waitAnother, 1000);
    }

    var addrating = function () {
        console.log(userId + "USER ID");
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

    var checkForWin = function (myWord, myFails, anotherWord, anotherFails) {
        if (myFails < maxFails &&  anotherFails < maxFails &&
            !checkIfDone(myWord) && !checkIfDone(anotherWord)) return;
        if (myFails >= maxFails &&  anotherFails >= maxFails) {
            $('#winmessage').html("Победителя нет");
        }
        if (myFails >= maxFails &&  anotherFails < maxFails) {
            $('#winmessage').html("Ты проиграл");
            minusrating();
        }
        if (myFails < maxFails &&  anotherFails >= maxFails) {
            $('#winmessage').html("Ты выиграл");
            addrating();
        }
        if(checkIfDone(myWord) && checkIfDone(anotherWord)){
            $('#winmessage').html("Ничья");
        }
        if(checkIfDone(myWord) && !checkIfDone(anotherWord)){
            $('#winmessage').html("Ты выиграл");
            addrating();
        }
        if(!checkIfDone(myWord) && checkIfDone(anotherWord)){
            $('#winmessage').html("Ты проиграл");
            minusrating();
        }
        if (myWord == "fail" && anotherWord == "fail") {
            $('#winmessage').html("Другой игрок не вошел");
        }
        clearTimeout(timoutId);
        $('#vstepButton').prop('disabled', true);
        $('#vsubmit').prop('type', 'submit');
    }
});
