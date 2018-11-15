package com.ya.simplegames.controller.chat;

import com.ya.simplegames.ajax.AjaxQueryChatRecieveMsg;
import com.ya.simplegames.ajax.AjaxQueryChatSendMsg;
import com.ya.simplegames.ajax.AjaxQueryStep;
import com.ya.simplegames.ajax.AjaxResponse;
import com.ya.simplegames.entity.Chat;
import com.ya.simplegames.entity.User;
import com.ya.simplegames.repository.ChatRepository;
import com.ya.simplegames.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ChatAjaxController {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Write message of player in database
     * @param data := {Message - user's message, userId - id of user, lobbyId - id of lobby}
     * @param errors - errors received during work of ajax
     * @return status of response, more often it's a success
     */
    @PostMapping("/chat/send")
    public ResponseEntity<?> chat(@RequestBody AjaxQueryChatSendMsg data, Errors errors) {

        AjaxResponse result = new AjaxResponse();

        if (errors.hasErrors()) {
            result.setStatus(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }

        Timestamp tm = new Timestamp(System.currentTimeMillis());

        chatRepository.setMessage(data.getLobbyId(),data.getUserId(), data.getMessage(), tm);

        result.setStatus("success");
        return ResponseEntity.ok(result);
    }

    /**
     * Get last messages from database by the last time period
     * @param data := {lobbyId - id of a lobby, timeMark - time of last "harvest"}
     * @param errors - errors received during work of ajax
     * @return time of this "harvest", messages of users and status
     */
    @PostMapping("/chat/receive")
    public ResponseEntity<?> chat(@RequestBody AjaxQueryChatRecieveMsg data, Errors errors) {

        AjaxResponse result = new AjaxResponse();

        if (errors.hasErrors()) {
            result.setStatus(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }
        try {
            Timestamp tm = data.getTimeMark();
            Timestamp tm_new = null;

            Iterable<Chat> chat = chatRepository.findAllByLobbyId(data.getLobbyId());

            String chatMessages = "";

            Map<Long, String> userNames = new HashMap<Long, String>();

            for (Chat ch : chat) {
                tm_new = ch.getTimeMark();
                if (tm == null || tm_new.after(tm)) {
                    Long userId = ch.getPlayer();
                    if (userNames.get(userId) == null) {
                        User user = userRepository.findUserByIdUser(userId);
                        userNames.put(userId, user.getNick());
                    }
                    chatMessages += userNames.get(userId) + ": " + ch.getMessage() + "\n";
                }
            }
            result.setData(chatMessages);
            result.setTimeMark(tm_new);
            result.setStatus("success");
        } catch (Exception e){
            e.getMessage();
            e.printStackTrace();
        }
        return ResponseEntity.ok(result);
    }
}
