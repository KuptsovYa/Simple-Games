var timeCounter;
var timerId = 0;

/**
 * This one is checks did the player2 appeared
 * if ajax returns wait status, first player continue to wait
 * @param lobbyId - id of created lobby
 * @param gameId - id of game hangman/cows..
 */
function checkGame(lobbyId, gameId) {
    console.log("checkgame " + lobbyId + " " + gameId);
    var readyCheck = false;
    $.ajax
    (
        {
            type:'POST',
            url: '/profile/waitingforlobby',
            contentType: "application/json",
            dataType: 'json',
            data:JSON.stringify({lobbyId: lobbyId}),
            cache: false,
            async: false,
            success: function (result) {
                if(result.status == "success") {
                    console.log(result);
                    readyCheck = true;
                    $('#vgameId').val(gameId);
                    $('#vplayerNum').val(1);
                    $('#vlobbyId').val(lobbyId);
                    $('#vuserName2').val(result.data2);
                    clearTimeout(timerId);
                }else {
                    readyCheck = false;
                    timeCounter+=1;
                    $('#message').text("Ожидание другого игрока " + timeCounter + " сек.");
                    //timerId = setTimeout(checkGame, 1000, lobbyId);
                }
            },
            error: function(request, status, error) {
                var statusCode = request.status; // вот он код ответа
                console.log(statusCode);
            }
        }
    );
    if(readyCheck) $('#vprofileForm').trigger('submit');
}

/**
 * Validation of russian letters
 * @param name
 * @returns {boolean}
 */
var validateRusLet = function (name) {
    var per_name =/[^А-ЯЁ]/i;
    if(per_name.test(name)) return false;
    else return true;
};

/**
 * Method which one creates a lobbys and starts a timer of waiting
 * or starts the game
 * @param gameId - id of a game
 * @returns {boolean}
 */
function pendingUser(gameId) {
    if($('#word' + gameId).prop("type") == "text"){
        if($('#word' + gameId).val() == "" ){
            $('#message').text("Загадайте слово");
            return false;
        }
        if(!validateRusLet($('#word' + gameId).val())){
            $('#message').text("Слово должно содержать только русские буквы");
            return false;
        }
    }

    $('#vprofileForm').attr('action',gamesMapping[gameId]);
    var readyCheck = false;
    $.ajax
    (
        {
            type:'POST',
            url: '/profile/pendinguser',
            contentType: "application/json",
            dataType: 'json',
            data:JSON.stringify({userId: userId, gameId: gameId, word: $('#word' + gameId).val()}),
            cache: false,
            async: false,
            success: function (result) {
                if(result.status == "1") {
                    readyCheck = true;
                    $('#vgameId').val(gameId);
                    $('#vplayerNum').val(2);
                    $('#vlobbyId').val(result.data);
                    $('#vuserName2').val(result.data2);
                    console.log(result.data2);
                }else {
                    clearTimeout(timerId);
                    readyCheck = false;
                    timeCounter = 0;
                    console.log("pending user " + result.data);
                    timerId = setInterval(checkGame, 1000, result.data, gameId);
                }
            },
            error: function(request, status, error) {
                var statusCode = request.status; // вот он код ответа
                console.log(statusCode);
            }
        }
    );
    if(readyCheck) $('#vprofileForm').trigger('submit');
    else return false;
}

var rdyChange = false;

$(document).ready(function() {

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    var gameList = [];
    $.ajax
    (
        {
            type:'POST',
            url:'/profile/askforgames',
            contentType: "application/json",
            dataType: 'json',
            data:JSON.stringify({unused:''}),
            cache: false,
            async: false,
            success:function(result){
                var d = result.data.split(',');
                d.splice(-1,1);
                gameList = d;
            },
            error: function(request, status, error) {
                var statusCode = request.status; // вот он код ответа
                console.log(statusCode);
            }
        }
    );

    for (var i = 0; i < gameList.length; i+=2){
        $("#vprofileForm").append('<input type="submit" class="btn11 btn btn-primary " name="next" value="' + gameList[i+1] + '" id="input'+  gameList[i] +'" onclick="pendingUser(' + gameList[i] + '); return false;">');
        $("#vprofileForm").append('<input type="hidden" id="word'+  gameList[i] +'" value="" placeholder="Придумайте слово"><br>');

    }

    $('#word1').attr("type","text");

    /**
     * Method provides to change personal data
     */
    $('#changepersonal').click(function () {
        console.log("click");
        if(!rdyChange){
            $('#cpName').prop('readonly', false);
            $('#cpName').removeClass("personalinputread");
            $('#cpName').addClass("personalinputwrite");
            $('#cpSecondName').prop('readonly', false);
            $('#cpSecondName').removeClass("personalinputread");
            $('#cpSecondName').addClass("personalinputwrite");
            $('#changepersonal').val("Сохранить");
            rdyChange = true;
            return false;
        }
        if(rdyChange){
            $.ajax
            (
                {
                    type:'POST',
                    url: '/profile/changepersonal',
                    contentType: "application/json",
                    dataType: 'json',
                    data:JSON.stringify({userId: userId, firstName: $('#cpName').val(), secondName:  $('#cpSecondName').val() }),
                    cache: false,
                    async: false,
                    success: function (result) {},
                    error: function(request, status, error) {
                        var statusCode = request.status;
                        console.log(statusCode);
                    }
                }
            );

            $('#cpName').prop('readonly', true);
            $('#cpName').removeClass("personalinputwrite");
            $('#cpName').addClass("personalinputread");
            $('#cpSecondName').prop('readonly', true);
            $('#cpSecondName').removeClass("personalinputwrite");
            $('#cpSecondName').addClass("personalinputread");
            $('#changepersonal').val("Изменить личные данные");
            rdyChange = false;
            return false;
        }
    });
});
