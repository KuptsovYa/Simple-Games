package com.ya.simplegames.controller;

import com.ya.simplegames.ajax.AjaxQueryUserId;
import com.ya.simplegames.ajax.AjaxResponse;
import com.ya.simplegames.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
public class WinnerAjaxController {

    @Autowired
    private UserRepository userRepository;


    /**
     * This one provides add rating to winner player
     * @param data := {userId - user id}
     * @param errors - errors received during work of ajax
     * @return updated rating in database
     */
    @PostMapping("/addrating")
    public ResponseEntity<?> addRating(@RequestBody AjaxQueryUserId data, Errors errors) {

        AjaxResponse result = new AjaxResponse();

        if (errors.hasErrors()) {
            result.setStatus(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }

        System.out.println(data.getUserId());

        Long rating = userRepository.getRatingById(data.getUserId());

        System.out.println(rating);
        userRepository.setUpdateRating(data.getUserId(), rating + 25);
        result.setStatus("success");
        return ResponseEntity.ok(result);
    }

    /**
     * This one provides minus rating to winner player
     * @param data := {userId - user id}
     * @param errors - errors received during work of ajax
     * @return updated rating in database
     */
    @PostMapping("/minusrating")
    public ResponseEntity<?> minusRating(@RequestBody AjaxQueryUserId data, Errors errors) {

        AjaxResponse result = new AjaxResponse();

        if (errors.hasErrors()) {
            result.setStatus(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));
            return ResponseEntity.badRequest().body(result);
        }

        System.out.println(data.getUserId());

        Long rating = userRepository.getRatingById(data.getUserId());

        System.out.println(rating);
        userRepository.setUpdateRating(data.getUserId(), rating - 25);
        result.setStatus("success");
        return ResponseEntity.ok(result);
    }
}