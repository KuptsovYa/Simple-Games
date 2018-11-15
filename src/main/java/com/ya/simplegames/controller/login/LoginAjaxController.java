package com.ya.simplegames.controller.login;

import com.ya.simplegames.ajax.AjaxQueryName;
import com.ya.simplegames.ajax.AjaxResponse;
import com.ya.simplegames.entity.User;
import com.ya.simplegames.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
public class LoginAjaxController {

    @Autowired
    private UserRepository userRepository;

    /**
     * This one checks for existing users in database
     * @param data := {name - user name}
     * @param errors - errors received during work of ajax
     * @return password or failed status
     */
    @PostMapping("/login/checkuser")
    public ResponseEntity<?> checkUser(@RequestBody AjaxQueryName data, Errors errors) {

        AjaxResponse result = new AjaxResponse();

        if (errors.hasErrors()) {
            result.setStatus(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }

        User user = userRepository.findByName(data.getName());
        if(user==null){
            result.setData("");
            result.setStatus("fail");
        }else {
            result.setData(user.getPassword());
            result.setStatus("success");
        }
        return ResponseEntity.ok(result);
    }
}
