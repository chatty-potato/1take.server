package potato.onetake.domain.Content.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import potato.onetake.global.BaseEntity.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="category")
public class Category extends BaseEntity {

	@Column(name = "content", nullable = false)
	private String content;

}
