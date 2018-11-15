package com.ya.simplegames.controller.cowsandbulls;

import com.ya.simplegames.config.SqlSafeUtil;
import com.ya.simplegames.entity.Sep;
import com.ya.simplegames.entity.User;
import com.ya.simplegames.gamelogic.GameLogic;
import com.ya.simplegames.repository.LobbyRepository;
import com.ya.simplegames.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;

@Controller
public class CowsAndBullsController {

    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
    private UserRepository userRepository;


    /**
     * Cows and bulls doPost Servlet
     * @param userId - id of user
     * @param playerNum - number of player
     * @param gameId - id of game
     * @param lobbyId - id of lobby
     * @param userName - user name of first player
     * @param userName2 - user name of second player
     * @param model - supply attributes used for rendering views
     * @return cows and bulls game view
     */
    @PostMapping("/cowsbulls")
    public String printUser(@RequestParam Long userId,
                            @RequestParam int playerNum,
                            @RequestParam Long gameId,
                            @RequestParam Long lobbyId,
                            @RequestParam String userName,
                            @RequestParam String userName2,
                            ModelMap model) {

        model.addAttribute("userId", userId);
        model.addAttribute("playerNum", playerNum);
        model.addAttribute("playerNum2", (playerNum == 1 ? 2 : 1));
        model.addAttribute("gameId", gameId);
        model.addAttribute("lobbyId", lobbyId);

        User u1 = userRepository.findByName(userName);
        User u2 = userRepository.findByName(userName2);

        model.addAttribute("nick", u1.getNick());
        model.addAttribute("nick2", u2.getNick());

        String timeStamp = new SimpleDateFormat("dd.MM.yyyy").format(u1.getRegDate());
        String timeStamp2 = new SimpleDateFormat("dd.MM.yyyy").format(u2.getRegDate());

        model.addAttribute("rating", u1.getRating());
        model.addAttribute("rating2", u2.getRating());
        model.addAttribute("regDate", timeStamp);
        model.addAttribute("regDate2", timeStamp2);

        try {
            if (playerNum == 1) {
                String num1 = GameLogic.guessNumber();
                String num2 = GameLogic.guessNumber();
                lobbyRepository.setUpdatedGameLog(lobbyId, num1 + Sep.COMMA + num2 + Sep.HASH);
            } else {
                Thread.sleep(1000);
            }
        } catch (Exception e){
            e.getMessage();
            e.printStackTrace();
        }
        return "cowsbulls";
    }
}

