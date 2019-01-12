package tuo.dto;

import tuo.model.QuestionImage;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class QuestionDto {
    @NotBlank
    @Size(max = 254)
    private String text;

    private QuestionImage image;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public QuestionImage getImage() {
        return image;
    }

    public void setImage(QuestionImage image) {
        this.image = image;
    }
}
