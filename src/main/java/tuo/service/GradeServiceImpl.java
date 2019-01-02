package tuo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tuo.model.Grade;
import tuo.model.Score;
import tuo.repository.GradeRepository;
import tuo.repository.ScoreRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GradeServiceImpl implements GradeService {
    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Override
    public List<Grade> findAll() {
        return gradeRepository.findAll();
    }

    @Override
    public List<Grade> findByScoreId(Long scoreId) {
        Page<Grade> pages = gradeRepository.findByScoreId(scoreId, Pageable.unpaged());
        return pages.getContent();
    }

    @Override
    public Optional<Grade> findById(Long id) {
        return gradeRepository.findById(id);
    }

    @Override
    public Optional<Grade> save(Long scoreId, Short gradeValue) {
        Optional<Score> optionalScore = scoreRepository.findById(scoreId);
        if (optionalScore.isPresent()) {
            Grade grade = new Grade();
            grade.setGrade(gradeValue);
            grade.setScore(optionalScore.get());
            return Optional.of(gradeRepository.save(grade));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void delete(Grade grade) {
        gradeRepository.delete(grade);
    }
}
