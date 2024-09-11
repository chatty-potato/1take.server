package potato.onetake.ineterview;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
	Optional<Interview> findById(Long interviewId);
}
