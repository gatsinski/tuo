package tuo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tuo.model.Grade;

import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    Page<Grade> findByScoreId(Long scoreId, Pageable pageable);
}
