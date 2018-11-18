package tuo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GameController {

    @GetMapping("/game")
    public String play(@RequestParam(name="round", required=false, defaultValue="1") int round, Model model) {
        model.addAttribute("round", round);
        return "play";
    }

}
