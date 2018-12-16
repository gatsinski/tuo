package tuo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tuo.form.BattleRoundForm;
import tuo.form.RoundQuestionForm;
import tuo.model.Answer;
import tuo.session.*;
import tuo.model.Question;
import tuo.repository.AnswerRepository;
import tuo.repository.QuestionRepository;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes("gameData")
public class GameController {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @GetMapping("/new-game")
    public String newGame(
        Model model,
        @ModelAttribute("gameData") GameData gameData
    ) {
        gameData = startNewGame();
        model.addAttribute("gameData", gameData);
        return "redirect:preparation-stage";
    }

    @GetMapping("/preparation-stage")
    public String showRoundQuestionForm(
        Model model,
        @ModelAttribute("gameData") GameData gameData,
        RoundQuestionForm roundQuestionForm
    ) {
        if (gameData.isTheGameOver()) {
            return "redirect:final-score";
        } else if (gameData.isExaminationReached()) {
            return "redirect:battle-stage";
        }

        Round round = gameData.getCurrentRound();
        RoundQuestion roundQuestion = round.getCurrentRoundQuestion();

        model.addAttribute("round", round);
        model.addAttribute("question", roundQuestion.getQuestion());
        model.addAttribute("answers", roundQuestion.getAnswers());
        model.addAttribute("gameData", gameData);
        return "preparation";
    }

    @PostMapping("/preparation-stage")
    public String submitRoundQuestionForm(
        Model model,
        @ModelAttribute("gameData") GameData gameData,
        @Valid RoundQuestionForm roundQuestionForm,
        BindingResult bindingResult,
        HttpServletRequest request
    ) {
        if (gameData.isTheGameOver()) {
            return "redirect:final-score";
        }

        Round round = gameData.getCurrentRound();
        RoundQuestion roundQuestion = round.getCurrentRoundQuestion();

        if (!bindingResult.hasErrors()) {
            Long answerId = Long.valueOf(request.getParameter("answer"));
            Points points = roundQuestion.getAnswerPoints(answerId);
            gameData.updateScore(points);

            round.goToNextQuestion();

            if (round.isFinished()) {
                gameData.goToNextRound();

                if (gameData.isExaminationReached()) {
                    return "redirect:battle-stage";
                }

                round = gameData.getCurrentRound();
            }

            roundQuestion = round.getCurrentRoundQuestion();
        }

        model.addAttribute("round", round);
        model.addAttribute("question", roundQuestion.getQuestion());
        model.addAttribute("answers", roundQuestion.getAnswers());
        model.addAttribute("gameData", gameData);
        return "preparation";
    }

    @GetMapping("/battle-stage")
    public String showBattleForm(
        Model model,
        @ModelAttribute("gameData") GameData gameData,
        BattleRoundForm battleRoundForm
    ) {
        if (gameData.isTheGameOver()) {
            return "redirect:final-score";
        } else if (!gameData.isExaminationReached()) {
            return "redirect:preparation-stage";
        }

        model.addAttribute("gameData", gameData);
        return "battle";
    }

    @PostMapping("/battle-stage")
    public String submitBattleForm(
        HttpServletRequest request,
        Model model,
        @ModelAttribute("gameData") GameData gameData,
        @Valid BattleRoundForm battleRoundForm,
        BindingResult bindingResult
    ) {
        if (!gameData.isExaminationReached()) {
            return "redirect:preparation-stage";
        }

        if (!bindingResult.hasErrors()) {
            int bet = Integer.parseInt(request.getParameter("bet"));
            if (gameData.getPoints().getKnowledge() >= bet) {
                gameData.placeBet(bet);

                if (gameData.isTheGameOver()) {
                    return "redirect:final-score";
                }

                gameData.goToNextBattleRound();
            }
        }

        model.addAttribute("gameData", gameData);
        return "battle";
    }

    @GetMapping("/final-score")
    public String showFinalScore(
        Model model,
        @ModelAttribute("gameData") GameData gameData
    ) {
        if (!gameData.isTheGameOver()) {
            return "redirect:preparation-stage";
        }

        model.addAttribute("finalScore", gameData.calculateFinalScore());
        model.addAttribute("gameData", gameData);
        return "final_score";
    }

    private List<RoundQuestion> getRandomRoundQuestions(int questionCount) {
        long count = questionRepository.count();
        List<RoundQuestion> roundQuestions = new ArrayList<>();

        do {
            // Better solution is needed
            int index = (int) (Math.random() * count);
            Page<Question> questionPage = questionRepository.findAll(PageRequest.of(index, 1));
            if (questionPage.hasContent()) {
                Question question = questionPage.getContent().get(0);
                List<Answer> answers = answerRepository.findByQuestionId(question.getId(), Pageable.unpaged()).getContent();
                roundQuestions.add(new RoundQuestion(question, answers));
            }
        } while (roundQuestions.size() != questionCount);

        return roundQuestions;
    }

    @ModelAttribute("gameData")
    public GameData gameData() {
        return startNewGame();
    }

    private static int getRandomNumber(int min, int max) {
        return (int)(Math.random()*(max-min+1))+min;
    }

    private GameData startNewGame() {
        int roundsCount = 5;
        int battleCount = 5;
        List<Round> rounds = new ArrayList<>();
        List<BattleRound> battleRounds= new ArrayList<>();

        for (int i=0; i<roundsCount; i++) {
            int questionCount = getRandomNumber(1, 4);
            List<RoundQuestion> roundQuestions= getRandomRoundQuestions(questionCount);
            Round round = new Round(roundQuestions);
            rounds.add(round);
        }

        for (int i=0; i<battleCount; i++) {
            battleRounds.add(new BattleRound());
        }

        return new GameData(rounds, battleRounds);
    }

}
