package tuo.service;

import tuo.model.Grade;

import java.util.List;
import java.util.Optional;

public interface GradeService {
    List<Grade> findAll();

    List<Grade> findByScoreId(Long scoreId);

    Optional<Grade> findById(Long id);

    Optional<Grade> save(Long scoreId, Short grade);

    void delete(Grade grade);
}
