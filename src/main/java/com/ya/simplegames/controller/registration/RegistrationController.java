package com.ya.simplegames.controller.registration;

import com.ya.simplegames.config.SqlSafeUtil;
import com.ya.simplegames.entity.User;
import com.ya.simplegames.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    /**
     * Add user in database with hashed password
     * @param name - user name
     * @param password - user password
     * @return redirect to profile
     */
    @PostMapping("/registration/adduser")
    public String adduser(@RequestParam String name, @RequestParam String password, @RequestParam String nick) {
        String role = name.equals("admin") ? "ADMIN" : "USER";
        Timestamp tm = new Timestamp(System.currentTimeMillis());
        User user = new User(SqlSafeUtil.fix(nick), SqlSafeUtil.fix(name), SqlSafeUtil.fix(password), role, 0, "", "", tm);
        userRepository.save(user);
        return "redirect:/profile";
    }
}