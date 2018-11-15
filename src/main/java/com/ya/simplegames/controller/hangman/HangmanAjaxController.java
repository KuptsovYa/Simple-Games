package com.ya.simplegames.controller.hangman;

import com.ya.simplegames.ajax.AjaxQueryStep;
import com.ya.simplegames.ajax.AjaxResponse;
import com.ya.simplegames.entity.Lobby;
import com.ya.simplegames.entity.Sep;
import com.ya.simplegames.gamelogic.GameLogic;
import com.ya.simplegames.repository.LobbyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class HangmanAjaxController {

    @Autowired
    private LobbyRepository lobbyRepository;

    /**
     * Step of user when his turn came
     * @param data := {value - player's values, lobbyId - id of lobby, playerNum - number of player (1?2)}
     * @param errors - errors received during work of ajax
     * @return number of guessed digit or wait - if queue did not come yet
     */
    @PostMapping("/hangman/step")
    public ResponseEntity<?> step(@RequestBody AjaxQueryStep data, Errors errors) {

        AjaxResponse result = new AjaxResponse();

        if (errors.hasErrors()) {
            result.setStatus(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }

        try {
            int playerNum = data.getPlayerNum();
            Optional<Lobby> lobby = lobbyRepository.findById(data.getLobbyId());
            String gameLog = lobby.get().getGameLog();
            char lastElem = gameLog.charAt(gameLog.length() - 1);
            if ((playerNum == 1 && lastElem == Sep.COMMA) || (playerNum == 2 && lastElem == Sep.HASH)) {
                result.setStatus("wait");
            } else {
                String letter = data.getValue();
                String answer = GameLogic.getAnswer(letter.charAt(0), GameLogic.getGuesedWord(gameLog, playerNum), GameLogic.myPrevStep(gameLog));
                lobbyRepository.setUpdatedGameLog(data.getLobbyId(), gameLog + answer + (playerNum == 1 ? Sep.COMMA : Sep.HASH));
                //log("Updated game log : " + newGameLog);
                result.setStatus("success");
                result.setData(answer);
            }
        } catch (Exception e){
            e.printStackTrace();
            e.getMessage();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * In this method player waits another one to until he is do his step
     * Code is checking database for last element of game log
     * If it is a hash first one is waiting, if it is a comma second one is waiting
     * @param data := {value - player's values, lobbyId - id of lobby, playerNum - number of player (1?2)}
     * @param errors - errors received during work of ajax
     * @return wait - if queue did not come yet or step of another player to write it on a page
     */
    @PostMapping("/hangman/secondstep")
    public ResponseEntity<?> secondStep(@RequestBody AjaxQueryStep data, Errors errors) {

        AjaxResponse result = new AjaxResponse();

        if (errors.hasErrors()) {
            result.setStatus(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }

        try {
            int playerNum = data.getPlayerNum();
            Optional<Lobby> lobby = lobbyRepository.findById(data.getLobbyId());
            String gameLog = lobby.get().getGameLog();
            if(false){
                lobbyRepository.deleteFailedLobbies(data.getLobbyId());
                result.setStatus("fail");
            } else {
                char lastElem = gameLog.charAt(gameLog.length() - 1);
                if ((playerNum == 1 && lastElem == Sep.HASH) || (playerNum == 2 && lastElem == Sep.COMMA)) {
                    result.setData(GameLogic.prevStep(gameLog));
                    if (result.getData().equals("")) result.setStatus("wait");
                } else {
                    result.setStatus("wait");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            e.getMessage();
        }
        return ResponseEntity.ok(result);
    }
}