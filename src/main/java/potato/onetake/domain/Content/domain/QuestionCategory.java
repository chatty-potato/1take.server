package potato.onetake.domain.Content.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import potato.onetake.global.BaseEntity.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="question_category")
public class QuestionCategory extends BaseEntity {

	@ManyToOne()
	@JoinColumn(name = "question_id", referencedColumnName = "id")
	private Question question;

	@ManyToOne()
	@JoinColumn(name = "category_id", referencedColumnName = "id")
	private Category category;

	public QuestionCategory(final Question question) {
		this.question = question;
	}
}
