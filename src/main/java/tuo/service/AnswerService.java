package tuo.service;

import tuo.dto.AnswerDto;
import tuo.model.Answer;

import java.util.List;
import java.util.Optional;

public interface AnswerService {

    List<Answer> findAll();

    List<Answer> findByQuestionId(Long questionId);

    Optional<Answer> findById(long id);

    Answer save(long questionId, AnswerDto answerDto);  // TODO: Change to Optional<Answer>

    Optional<Answer> update(long answerId, AnswerDto answerDto);

    void delete(Answer answer);
}
