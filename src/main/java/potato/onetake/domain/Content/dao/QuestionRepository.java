package potato.onetake.domain.Content.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import potato.onetake.domain.Content.domain.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
