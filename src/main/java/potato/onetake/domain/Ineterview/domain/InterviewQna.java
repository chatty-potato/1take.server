package potato.onetake.domain.Ineterview.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import potato.onetake.domain.Content.domain.Question;
import potato.onetake.domain.Content.domain.QuestionCategory;
import potato.onetake.global.BaseEntity.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="interview_qna")
public class InterviewQna extends BaseEntity {

	@ManyToOne()
	@JoinColumn(name = "interview_id", referencedColumnName = "id")
	private Interview interview;

	@ManyToOne()
	@JoinColumn(name = "question_category_id", referencedColumnName = "id")
	private QuestionCategory questionCategory;

	@Column(name = "answer", nullable = true)
	private String answer;

	public InterviewQna(final Interview interview, final QuestionCategory questionCategory) {
		this.interview = interview;
		this.questionCategory = questionCategory;
	}

	public  InterviewQna(final Interview interview, final QuestionCategory questionCategory, final String answer) {
		this.interview = interview;
		this.questionCategory = questionCategory;
		this.answer = answer;
	}
}
