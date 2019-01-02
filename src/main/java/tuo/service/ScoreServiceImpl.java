package tuo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tuo.dto.ScoreDto;
import tuo.model.Score;
import tuo.model.User;
import tuo.repository.ScoreRepository;
import tuo.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ScoreServiceImpl implements ScoreService {
    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Score> findAll() {
        return scoreRepository.findAll();
    }

    @Override
    public List<Score> findByUserId(Long userId) {
        Page<Score> pages = scoreRepository.findByUserId(userId, Pageable.unpaged());
        return pages.getContent();
    }

    @Override
    public Optional<Score> findById(Long id) {
        return scoreRepository.findById(id);
    }

    @Override
    public Optional<Score> save(Long userId, ScoreDto scoreDto) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Score score = new Score();

            score.setKnowledge(scoreDto.getKnowledge());
            score.setReputation(scoreDto.getReputation());
            score.setScore(scoreDto.getScore());
            score.setUser(optionalUser.get());
            return Optional.of(scoreRepository.save(score));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void delete(Score score) {
        scoreRepository.delete(score);
    }
}
