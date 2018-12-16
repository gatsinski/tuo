package tuo.form;

import javax.validation.constraints.NotNull;

public class RoundQuestionForm {

    @NotNull
    private Integer answer;  // int defaults to zero while Integer defaults to Null

    public Integer getAnswer() {
        return answer;
    }

    public void setAnswer(Integer answer) {
        this.answer = answer;
    }
}
