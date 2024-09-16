package potato.onetake.domain.Ineterview.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import potato.onetake.domain.Ineterview.domain.Interview;

import java.util.Optional;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
	Optional<Interview> findById(Long interviewId);
}
