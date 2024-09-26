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

	public InterviewQna(final Interview interview, final Question question) {
		this.interview = interview;
		this.question = question;
	}

	public  InterviewQna(final Interview interview, final Question question, final String answer) {
		this.interview = interview;
		this.question = question;
		this.answer = answer;
	}
}
