package potato.onetake.domain.Content.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import potato.onetake.global.BaseEntity.BaseEntity;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name="category")
public class Category extends BaseEntity {

	@Column(name = "content", nullable = false)
	private String content;

}
