package tuo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tuo.dto.DeletionDto;
import tuo.dto.AnswerDto;
import tuo.model.Answer;
import tuo.service.AnswerService;
import tuo.service.QuestionService;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/admin/answers/{id}")
public class AnswerAdminController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    @ModelAttribute("answer")
    public AnswerDto answerDto() {
        return new AnswerDto();
    }

    @ModelAttribute("answerDeletion")
    public DeletionDto deletionDto() {
        return new DeletionDto();
    }

    @GetMapping
    public String showAnswerDetail(Model model,
                                     @PathVariable("id") long id) {
        Optional<Answer> optionalAnswer = answerService.findById(id);
        if (!optionalAnswer.isPresent()) {
            return "redirect:/admin/questions";
        } else {
            model.addAttribute("answer", optionalAnswer.get());
            return "admin/answers/detail";
        }

    }

    @PostMapping
    public String processAnswerEditForm(Model model,
                                        @PathVariable("id") long id,
                                        @ModelAttribute("answer") @Valid AnswerDto answerDto,
                                        BindingResult result) {
        Optional<Answer> optionalAnswer = answerService.findById(id);
        if (!optionalAnswer.isPresent()) {
            return "redirect:/admin/questions";
        } else {
            if (!result.hasErrors()) {
                optionalAnswer = answerService.update(id, answerDto);
            }

            model.addAttribute("answer", optionalAnswer.get());
            return "admin/answers/detail";
        }


    }

    @GetMapping("delete")
    public String showAnswerDeleteForm(Model model,
                                       @PathVariable("id") long id) {
        Optional<Answer> optionalAnswer = answerService.findById(id);
        if (!optionalAnswer.isPresent()) {
            return "redirect:/admin/questions";
        } else {
            model.addAttribute("answer", optionalAnswer.get());
            return "admin/answers/delete";
        }
    }

    @PostMapping("/delete")
    public String processAnswerDeleteForm(Model model,
                                          @PathVariable("id") long id,
                                          @ModelAttribute("answerDeletion") @Valid DeletionDto deletionDto,
                                          BindingResult result) {
        Optional<Answer> optionalAnswer = answerService.findById(id);
        if (!optionalAnswer.isPresent()) {
            return "redirect:/admin/questions";
        } else {
            Answer answer = optionalAnswer.get();
            if (!result.hasErrors()) {
                answerService.delete(answer);
                return "redirect:/admin/questions/" + answer.getQuestion().getId();
            } else {
                model.addAttribute("answer", answer);
                return "admin/answers/delete";
            }
        }
    }
}
