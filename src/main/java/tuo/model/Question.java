package tuo.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "questions")
public class Question extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 254)
    @Column(unique = true)
    private String text;

    @Enumerated(EnumType.STRING)
    private QuestionImage image;

    public Question() {
    }

    public Question(Long id, @NotNull @Size(max = 254) String text) {
        this.id = id;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
