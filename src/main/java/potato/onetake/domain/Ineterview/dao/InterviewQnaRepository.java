package potato.onetake.domain.Ineterview.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import potato.onetake.domain.Ineterview.domain.InterviewQna;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewQnaRepository extends JpaRepository<InterviewQna, Long> {
	Optional<InterviewQna> findByInterviewIdAndQuestionCategoryId(Long interviewId, Long questionCategoryId);

	List<InterviewQna> findAllByInterviewId(Long interviewId);
}
