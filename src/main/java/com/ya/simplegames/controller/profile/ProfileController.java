package com.ya.simplegames.controller.profile;

import com.ya.simplegames.entity.User;
import com.ya.simplegames.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Controller
public class ProfileController {

    @Value("${gamesmapping}")
    private String gamesMapping;

    @Autowired
    private UserRepository userRepository;

    /**
     * Profile doGet method with adding props in model from database
     * @param model - model of profile
     * @return profile view
     */
    @GetMapping("/profile")
    public String profile(ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        User user = userRepository.findByName(name);

        String timeStamp = new SimpleDateFormat("dd.MM.yyyy").format(user.getRegDate());
        model.addAttribute("nick", user.getNick());
        model.addAttribute("userName", name);
        model.addAttribute("userId", user.getIdUsers());
        model.addAttribute("gamesMapping",gamesMapping);
        model.addAttribute("rating", user.getRating());
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());
        model.addAttribute("regDate", timeStamp);
        return "profile";
    }
}
