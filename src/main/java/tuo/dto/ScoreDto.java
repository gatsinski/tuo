package tuo.dto;

import tuo.model.Grade;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class ScoreDto {

    @NotEmpty
    private Integer reputation;

    @NotEmpty
    private Integer knowledge;

    @NotEmpty
    private List<Grade> grades;

    @NotEmpty
    private Double score;

    public Integer getReputation() {
        return reputation;
    }

    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }

    public Integer getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(Integer knowledge) {
        this.knowledge = knowledge;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
