package potato.onetake.domain.Member.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import potato.onetake.domain.Content.domain.Category;
import potato.onetake.domain.Position.domain.Profile;
import potato.onetake.global.BaseEntity.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "position")
public class Position extends BaseEntity {

	@ManyToOne()
	@JoinColumn(name = "profile_id", referencedColumnName = "id")
	private Profile profile;

	@ManyToOne()
	@JoinColumn(name = "category_id", referencedColumnName = "id")
	private Category category;

	public Position(final Profile profile) {
		this.profile = profile;
	}
}
