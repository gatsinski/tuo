package tuo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tuo.dto.AnswerDto;
import tuo.dto.DeletionDto;
import tuo.dto.QuestionDto;
import tuo.model.Answer;
import tuo.model.Question;
import tuo.service.AnswerService;
import tuo.service.QuestionService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/questions")
public class QuestionAdminController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    @ModelAttribute("question")
    public QuestionDto questionDto() {
        return new QuestionDto();
    }

    @ModelAttribute("questionDeletion")
    public DeletionDto deletionDto() {
        return new DeletionDto();
    }

    @ModelAttribute("answer")
    public AnswerDto answerDto() {
        return new AnswerDto();
    }

    @GetMapping
    public String showQuestionCreateForm(Model model) {
        List<Question> questions = questionService.findAll();
        model.addAttribute("questions", questions);
        return "admin/questions/list";
    }

    @PostMapping
    public String submitQuestionCreateForm(Model model,
                                           @ModelAttribute("question") @Valid QuestionDto questionDto,
                                           BindingResult result) {
        Optional<Question> optionalQuestion = questionService.findByText(questionDto.getText());
        if (optionalQuestion.isPresent()){
            result.rejectValue("text", null, "There is already a question with that text");
        }

        if (!result.hasErrors()) {
            questionService.save(questionDto);
        }

        List<Question> questions = questionService.findAll();
        model.addAttribute("questions", questions);
        return "admin/questions/list";
    }

    @GetMapping("/{id}")
    public String showQuestionsEditForm(Model model,
                                        @PathVariable("id") long id) {
        Optional<Question> optionalQuestion = questionService.findById(id);
        if (!optionalQuestion.isPresent()) {
            return "redirect:/admin/questions";
        } else {
            List<Answer> answers = answerService.findByQuestionId(id);
            model.addAttribute("question", optionalQuestion.get());
            model.addAttribute("answers", answers);
            return "admin/questions/detail";
        }
    }

    @PostMapping("/{id}")
    public String processQuestionEditForm(Model model,
                                          @PathVariable("id") long id,
                                          @ModelAttribute("question") @Valid QuestionDto questionDto,
                                          BindingResult result) {
        Optional<Question> optionalQuestion = questionService.findById(id);
        if (!optionalQuestion.isPresent()) {
            return "redirect:/admin/questions";
        } else {
            if (!result.hasErrors()) {
                optionalQuestion = questionService.update(id, questionDto);
            }

            List<Answer> answers = answerService.findByQuestionId(id);
            model.addAttribute("question", optionalQuestion.get());
            model.addAttribute("answers", answers);
            return "admin/questions/detail";
        }
    }

    @GetMapping("/{id}/delete")
    public String showQuestionDeleteForm(Model model,
                                       @PathVariable("id") long id) {
        Optional<Question> optionalQuestion = questionService.findById(id);
        if (!optionalQuestion.isPresent()) {
            return "redirect:/admin/questions";
        } else {
            model.addAttribute("question", optionalQuestion.get());
            return "admin/questions/delete";
        }
    }

    @PostMapping("/{id}/delete")
    public String processQuestionDeleteForm(Model model,
                                            @PathVariable("id") long id,
                                            @ModelAttribute("questionDeletion") @Valid DeletionDto deletionDto,
                                            BindingResult result) {

        Optional<Question> optionalQuestion = questionService.findById(id);
        if (!optionalQuestion.isPresent()) {
            return "redirect:/admin/questions";
        } else {
            Question question = optionalQuestion.get();
            if (!result.hasErrors()) {
                questionService.delete(question);
                return "redirect:/admin/questions";
            } else {
                model.addAttribute("question", question);
                return "admin/questions/delete";
            }
        }
    }

    @GetMapping("/{id}/answers")
    public String showAnswerCreateForm(Model model,
                                          @PathVariable("id") long id) {
        Optional<Question> optionalQuestion = questionService.findById(id);
        if (!optionalQuestion.isPresent()) {
            return "redirect:/admin/questions";
        } else {
            List<Answer> answers = answerService.findByQuestionId(id);
            model.addAttribute("question", optionalQuestion.get());
            model.addAttribute("answers", answers);
            return "admin/questions/answer_create";
        }

    }

    @PostMapping("/{id}/answers")
    public String processAnswerCreateForm(Model model,
                                          @PathVariable("id") long id,
                                          @ModelAttribute("answer") @Valid AnswerDto answerDto,
                                          BindingResult result) {
        Optional<Question> optionalQuestion = questionService.findById(id);
        if (!optionalQuestion.isPresent()) {
            return "redirect:/admin/questions";
        } else {
            if (!result.hasErrors()) {
                answerService.save(id, answerDto);
            }

            List<Answer> answers = answerService.findByQuestionId(id);
            model.addAttribute("question", optionalQuestion.get());
            model.addAttribute("answers", answers);
            return "admin/questions/answer_create";
        }
    }

}
