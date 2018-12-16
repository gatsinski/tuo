package tuo.session;

import java.util.List;

public class Round {
    List<RoundQuestion> roundQuestions;  // Up to four questions per round
    int currentQuestionIndex;

    public Round(List<RoundQuestion> roundQuestions) {
        this.roundQuestions = roundQuestions;
        this.currentQuestionIndex = 0;
    }

    public List<RoundQuestion> getRoundQuestions() {
        return roundQuestions;
    }

    public void setRoundQuestions(List<RoundQuestion> roundQuestions) {
        this.roundQuestions = roundQuestions;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public int getCurrentQuestionNumber() {
        return currentQuestionIndex + 1;
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }

    public RoundQuestion getCurrentRoundQuestion() {
        return roundQuestions.get(currentQuestionIndex);
    }

    public void goToNextQuestion() {
        this.currentQuestionIndex++;
    }

    public boolean isFinished() {
        return currentQuestionIndex >= this.roundQuestions.size();
    }
}
