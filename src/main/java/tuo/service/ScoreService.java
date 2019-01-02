package tuo.service;

import tuo.dto.ScoreDto;
import tuo.model.Score;

import java.util.List;
import java.util.Optional;

public interface ScoreService {
    List<Score> findAll();

    List<Score> findByUserId(Long userId);

    Optional<Score> findById(Long id);

    Optional<Score> save(Long userId, ScoreDto scoreDto);

    void delete(Score score);
}
