package tuo.session;

import tuo.model.Answer;
import tuo.model.Question;

import java.util.List;

public class RoundQuestion {
    Question question;
    List<Answer> answers;

    public RoundQuestion(Question question, List<Answer> answers) {
        this.question = question;
        this.answers = answers;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public Points getAnswerPoints(long answerId) {
        Answer answer = answers.stream()
            .filter(currentAnswer -> answerId == currentAnswer.getId())
            .findAny()
            .orElse(null);
        if (answer != null) {
            return new Points(answer.getKnowledge(), answer.getReputation());
        } else {
            return new Points(0, 0);
        }
    }
}
