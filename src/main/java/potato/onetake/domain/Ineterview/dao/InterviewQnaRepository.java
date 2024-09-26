package potato.onetake.domain.Ineterview.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import potato.onetake.domain.Ineterview.domain.InterviewQna;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewQnaRepository extends JpaRepository<InterviewQna, Long> {
	Optional<InterviewQna> findByInterviewIdAndQuestionId(Long interviewId, Long questionId);

	List<InterviewQna> findAllByInterviewId(Long interviewId);

	Optional<Long> findQuestionIdByInterviewId(Long interviewId);
}
