package com.ya.simplegames.controller.registration;

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
public class RegistraionAjaxController {

    @Autowired
    private UserRepository userRepository;

    /**
     * This one is checks exists user with this name or not
     * @param data := {name - user name}
     * @param errors - errors received during work of ajax
     * @return 1 - not exists, 0 - exists
     */
    @PostMapping("/registration/checkname")
    public ResponseEntity<?> checkName(@RequestBody AjaxQueryName data, Errors errors) {

        AjaxResponse result = new AjaxResponse();

        if (errors.hasErrors()) {
            result.setStatus(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }

        User user = userRepository.findByName(data.getName());
        if (user!=null) {
            result.setData("1");
        } else {
            result.setData("0");
        }
        result.setStatus("success");

        return ResponseEntity.ok(result);
    }
}