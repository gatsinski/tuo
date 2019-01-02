package tuo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tuo.model.Score;
import tuo.model.User;
import tuo.service.AuthenticationService;
import tuo.service.ScoreService;
import tuo.service.UserService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @Autowired
    private ScoreService scoreService;


    @GetMapping("/profile")
    public String showProfile(Model model) {
        Authentication authentication = authenticationService.getAuthentication();
        Optional<User> optionalUser = userService.findByEmail(authentication.getName());

        if (!optionalUser.isPresent()) {
            return "redirect:/";
        } else {
            User user = optionalUser.get();
            List<Score> scores = scoreService.findByUserId(user.getId());

            model.addAttribute("user", user);
            model.addAttribute("scores", scores);
            return "user/profile/index";
        }
    }
}
