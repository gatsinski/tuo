package tuo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tuo.model.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

}
