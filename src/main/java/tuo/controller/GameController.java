package tuo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tuo.dto.ScoreDto;
import tuo.form.BattleRoundForm;
import tuo.form.RoundQuestionForm;
import tuo.model.*;
import tuo.repository.AnswerRepository;
import tuo.repository.QuestionRepository;
import tuo.service.AuthenticationService;
import tuo.service.GradeService;
import tuo.service.ScoreService;
import tuo.service.UserService;
import tuo.session.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/game")
@SessionAttributes("gameData")
public class GameController {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private GradeService gradeService;

    @ModelAttribute("gameData")
    public GameData gameData() {
        return startNewGame();
    }

    @GetMapping("")
    public String newGame(
        Model model,
        @ModelAttribute("gameData") GameData gameData
    ) {
        gameData = startNewGame();
        if (gameData.isTheGameValid()) {
            return "redirect:rules";
        }
        model.addAttribute("gameData", gameData);
        return "redirect:/game/preparation-stage";
    }

    @GetMapping("/preparation-stage")
    public String showRoundQuestionForm(
        Model model,
        @ModelAttribute("gameData") GameData gameData,
        RoundQuestionForm roundQuestionForm
    ) {
        if (gameData.isTheGameValid()) {
            return "redirect:rules";
        } else if (gameData.isTheGameOver()) {
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
        return "game/preparation";
    }

    @PostMapping("/preparation-stage")
    public String submitRoundQuestionForm(
        Model model,
        @ModelAttribute("gameData") GameData gameData,
        @Valid RoundQuestionForm roundQuestionForm,
        BindingResult bindingResult,
        HttpServletRequest request
    ) {
        if (gameData.isTheGameValid()) {
            return "redirect:rules";
        } else if (gameData.isTheGameOver()) {
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
        return "game/preparation";
    }

    @GetMapping("/battle-stage")
    public String showBattleForm(
        Model model,
        @ModelAttribute("gameData") GameData gameData,
        BattleRoundForm battleRoundForm
    ) {
        if (gameData.isTheGameValid()) {
            return "redirect:rules";
        } else if (gameData.isTheGameOver()) {
            return "redirect:final-score";
        } else if (!gameData.isExaminationReached()) {
            return "redirect:preparation-stage";
        }

        model.addAttribute("gameData", gameData);
        return "game/battle";
    }

    @PostMapping("/battle-stage")
    public String submitBattleForm(
        HttpServletRequest request,
        Model model,
        @ModelAttribute("gameData") GameData gameData,
        @Valid BattleRoundForm battleRoundForm,
        BindingResult bindingResult
    ) {
        if (gameData.isTheGameValid()) {
            return "redirect:rules";
        } else if (!gameData.isExaminationReached()) {
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
        return "game/battle";
    }

    @GetMapping("/final-score")
    public String showFinalScore(
        Model model,
        @ModelAttribute("gameData") GameData gameData
    ) {
        if (!gameData.isTheGameOver()) {
            return "redirect:preparation-stage";
        }

        Points points = gameData.getPoints();
        Score score = null;
        List<Grade> grades = new ArrayList<>();
        Authentication authentication = authenticationService.getAuthentication();

        if (authentication.isAuthenticated()) {
            Optional<User> optionalUser = userService.findByEmail(authentication.getName());

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                ScoreDto scoreDto = new ScoreDto();

                scoreDto.setKnowledge(gameData.getTotalKnowledge());
                scoreDto.setReputation(points.getReputation());
                scoreDto.setScore(gameData.calculateFinalScore());

                Optional<Score> optionalScore = scoreService.save(user.getId(), scoreDto);

                if (optionalScore.isPresent()) {
                    score = optionalScore.get();
                    for (Short gradeValue : gameData.getGrades()) {
                        Optional<Grade> optionalGrade = gradeService.save(score.getId(), gradeValue);
                        optionalGrade.ifPresent(grades::add);
                    }
                }
            }
        }

        if (score == null){
            score = new Score();
            score.setKnowledge(gameData.getTotalKnowledge());
            score.setReputation(points.getReputation());
            score.setScore(gameData.calculateFinalScore());

            for (Short gradeValue : gameData.getGrades()) {
                grades.add(new Grade(score, gradeValue));
            }
        }

        model.addAttribute("score", score);
        model.addAttribute("grades", grades);
        return "game/final_score";
    }

    @GetMapping("/rules")
    public String showRules(
        Model model,
        @ModelAttribute("gameData") GameData gameData
    ) {
        return "game/rules";
    }

    @GetMapping("/ranking")
    public String showRanking(Model model) {
        List<Score> scores = scoreService.findTop(10, "score");
        model.addAttribute("scores", scores);
        return "game/ranking";
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

    private static int getRandomNumber(int min, int max) {
        return (int)(Math.random()*(max-min+1))+min;
    }

    private GameData startNewGame() {
        if (questionRepository.count() == 0) {
            return new GameData(new ArrayList<>(), new ArrayList<>());
        }
        int roundsCount = 5;
        int battleCount = 5;
        List<Round> rounds = new ArrayList<>();
        List<BattleRound> battleRounds= new ArrayList<>();

        for (int i=0; i<roundsCount; i++) {
            int questionCount = getRandomNumber(1, 4);
            List<RoundQuestion> roundQuestions = getRandomRoundQuestions(questionCount);
            Round round = new Round(roundQuestions);
            rounds.add(round);
        }

        for (int i=0; i<battleCount; i++) {
            battleRounds.add(new BattleRound());
        }

        return new GameData(rounds, battleRounds);
    }

}
