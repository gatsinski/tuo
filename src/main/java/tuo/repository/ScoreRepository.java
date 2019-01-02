package tuo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tuo.model.Score;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    Page<Score> findByUserId(Long userId, Pageable pageable);
}
