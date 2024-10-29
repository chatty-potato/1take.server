package potato.onetake.domain.Ineterview.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import potato.onetake.domain.Content.domain.Question;
import potato.onetake.domain.Content.domain.QuestionCategory;
import potato.onetake.global.BaseEntity.BaseEntity;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

}
