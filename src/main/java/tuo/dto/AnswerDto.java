package tuo.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class AnswerDto {

    @NotEmpty
    @Size(max = 254)
    private String text;

    private int reputation;
    private int knowledge;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public int getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(int knowledge) {
        this.knowledge = knowledge;
    }
}
