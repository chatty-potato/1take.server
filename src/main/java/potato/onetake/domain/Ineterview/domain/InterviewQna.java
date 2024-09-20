package potato.onetake.domain.Ineterview.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import potato.onetake.domain.Content.domain.Question;
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
	@JoinColumn(name = "question_id", referencedColumnName = "id")
	private Question question;

	@Column(name = "answer", nullable = true)
	private String answer;

	public InterviewQna(Interview interview, Question question) {
		this.interview = interview;
		this.question = question;
	}

	public  InterviewQna(Interview interview, Question question, String answer) {
		this.interview = interview;
		this.question = question;
		this.answer = answer;
	}
}
