package tuo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tuo.dto.QuestionDto;
import tuo.model.Answer;
import tuo.model.Question;
import tuo.repository.QuestionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;


    @Override
    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    @Override
    public Optional<Question> findById(long id) {
        return questionRepository.findById(id);
    }

    @Override
    public Optional<Question> findByText(String text) {
        return questionRepository.findByText(text);
    }

    @Override
    public Question save(QuestionDto questionDto) {
        Question question = new Question();
        question.setText(questionDto.getText());
        question.setImage(questionDto.getImage());
        return questionRepository.save(question);
    }

    @Override
    public Optional<Question> update(long questionId, QuestionDto questionDto) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);

        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            question.setText(questionDto.getText());
            question.setImage(questionDto.getImage());
            return Optional.of(questionRepository.save(question));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void delete(Question question) {
        questionRepository.delete(question);
    }
}
