package potato.onetake.domain.Ineterview.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import potato.onetake.domain.Ineterview.domain.InterviewQna;

@Repository
public interface InterviewQnaRepository extends JpaRepository<InterviewQna, Long> {
}
