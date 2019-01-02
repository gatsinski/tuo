package tuo.service;

import tuo.dto.QuestionDto;
import tuo.model.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionService {

    List<Question> findAll();

    Optional<Question> findById(long id);

    Optional<Question> findByText(String text);

    Question save(QuestionDto questionDto);  // TODO: Change to Optional<Answer>

    Optional<Question> update(long questionId, QuestionDto questionDto);

    void delete(Question question);
}
