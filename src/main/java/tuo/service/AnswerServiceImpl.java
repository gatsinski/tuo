package tuo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tuo.dto.AnswerDto;
import tuo.model.Answer;
import tuo.model.Question;
import tuo.repository.AnswerRepository;
import tuo.repository.QuestionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AnswerServiceImpl implements AnswerService {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public List<Answer> findAll() {
        return answerRepository.findAll();
    }

    @Override
    public List<Answer> findByQuestionId(Long questionId) {
        Page<Answer> pages = answerRepository.findByQuestionId(questionId, Pageable.unpaged());
        return pages.getContent();
    }

    @Override
    public Optional<Answer> findById(long id) {
        return answerRepository.findById(id);
    }

    @Override
    public Answer save(long questionId, AnswerDto answerDto) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        Answer answer = new Answer();

        answer.setText(answerDto.getText());
        answer.setReputation(answerDto.getReputation());
        answer.setKnowledge(answerDto.getKnowledge());

        if (optionalQuestion.isPresent()) {
            answer.setQuestion(optionalQuestion.get());
            return answerRepository.save(answer);
        } else {
            return answer;
        }
    }

    @Override
    public Optional<Answer> update(long answerId, AnswerDto answerDto) {
        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);

        if (optionalAnswer.isPresent()) {
            Answer answer = optionalAnswer.get();
            answer.setText(answerDto.getText());
            answer.setReputation(answerDto.getReputation());
            answer.setKnowledge(answerDto.getKnowledge());
            return Optional.of(answerRepository.save(answer));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void delete(Answer answer) {
        answerRepository.delete(answer);
    }
}
