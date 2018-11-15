package com.ya.simplegames.controller.profile;



import com.ya.simplegames.ajax.*;
import com.ya.simplegames.config.SqlSafeUtil;
import com.ya.simplegames.entity.Game;
import com.ya.simplegames.entity.Lobby;
import com.ya.simplegames.entity.Sep;
import com.ya.simplegames.entity.User;
import com.ya.simplegames.repository.GameRepository;
import com.ya.simplegames.repository.LobbyRepository;
import com.ya.simplegames.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ProfileAjaxController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LobbyRepository lobbyRepository;

    /**
     * Get types of games from database
     * @param errors - errors received during work of ajax
     * @return names of games
     */
    @PostMapping("/profile/askforgames")
    public ResponseEntity<?> askForGames(@RequestBody AjaxQueryEmpty unused, Errors errors) {

        AjaxResponse result = new AjaxResponse();

        if (errors.hasErrors()) {
            result.setStatus(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }
        Iterable<Game> rs = gameRepository.findAll();

        String resultString = "";
        for (Game s : rs) {
            resultString += s.getIdGames();
            resultString += ",";
            resultString += s.getGameName();
            resultString += ",";
        }

        result.setData(resultString);
        result.setStatus("success");

        return ResponseEntity.ok(result);
    }

    /**
     * Method that processes pending users
     * it processed moves like a change game, set new game, add second player and then start a game
     * @param data := {userId - id of user, gameId - id of game, word - word guessed by player (if this is hangman)}
     * @param errors - errors received during work of ajax
     * @return wait status with generated lobby id, or lobby with re-writed game
     */
    @PostMapping("/profile/pendinguser")
    public ResponseEntity<?> pendingUser(@RequestBody AjaxQueryUserGame data, Errors errors) {

        AjaxResponse result = new AjaxResponse();

        if (errors.hasErrors()) {
            result.setStatus(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }

        Long IdUser = data.getUserId();
        Long IdGame = data.getGameId();
        String word = data.getWord();
        Lobby game = lobbyRepository.findGameWithEmptyPlayer2(IdGame, IdUser);

        if (game != null) {
            String gameLog = word.toUpperCase() + Sep.COMMA + game.getGameLog().toUpperCase() + Sep.HASH;
            lobbyRepository.setPlayer2(IdUser, game.getIdLobby(), gameLog);
            lobbyRepository.deleteSemiEmptyLobbies(IdUser);
            Optional<User> u = userRepository.findById(game.getPlayer1());
            result.setData(String.valueOf(game.getIdLobby()));
            result.setData2(u.get().getName());
            result.setStatus("1");
        } else {
            Long count = lobbyRepository.findPlayer1Lobbies(IdUser);
            if (count > 0) {
                lobbyRepository.setSwitchGame(IdGame, IdUser, word);
            } else {
                lobbyRepository.setNewGame(IdGame, IdUser, word);
            }
            result.setData(String.valueOf(lobbyRepository.findPlayer1Lobby(IdGame, IdUser)));
            result.setStatus("0");
        }
        return ResponseEntity.ok(result);

    }

    /**
     * Processing a waiting user
     * getting player2 in game
     * @param data := {lobbyId - id of lobby}
     * @param errors - errors received during work of ajax
     * @return completed lobby
     */
    @PostMapping("/profile/waitingforlobby")
    public ResponseEntity<?> waitingForLobby(@RequestBody AjaxQueryLobby data, Errors errors) {

        AjaxResponse result = new AjaxResponse();

        if (errors.hasErrors()) {
            result.setStatus(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }

        Long player2 = lobbyRepository.findPlayer2(data.getLobbyId());
        if (player2 != null) {
            result.setData(String.valueOf(player2));
            Optional<User> u = userRepository.findById(player2);
            result.setData2(u.get().getName());
            result.setStatus("success");
        } else {
            result.setData("");
            result.setStatus("fail");
        }
        return ResponseEntity.ok(result);

    }

    /**
     * Method which changing personal data of user
     * @param data := {userId - id of user, firstName - first name of user, secondName - second name of user}
     * @param errors - errors received during work of ajax
     * @return updated query in database
     */
    @PostMapping("/profile/changepersonal")
    public ResponseEntity<?> changePersonal(@RequestBody AjaxQueryPersonal data, Errors errors) {

        AjaxResponse result = new AjaxResponse();

        if (errors.hasErrors()) {
            result.setStatus(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }

        User u = userRepository.findUserByIdUser(data.getUserId());

        u.setFirstName(SqlSafeUtil.fix(data.getFirstName()));
        u.setLastName(SqlSafeUtil.fix(data.getSecondName()));

        userRepository.save(u);
        result.setStatus("success");

        return ResponseEntity.ok(result);
    }

}
