package potato.onetake.domain.Ineterview.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import potato.onetake.domain.Content.domain.Category;
import potato.onetake.global.BaseEntity.BaseEntity;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name="interview_category")
public class InterviewCategory extends BaseEntity {

	@ManyToOne()
	@JoinColumn(name = "interview_id", referencedColumnName = "id")
	private Interview interview;

	@ManyToOne()
	@JoinColumn(name = "category_id", referencedColumnName = "id")
	private Category category;

}
