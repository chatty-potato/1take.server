package potato.onetake.domain.Ineterview.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import potato.onetake.domain.Position.domain.Profile;
import potato.onetake.global.BaseEntity.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="interview")
public class Interview extends BaseEntity {

	@ManyToOne()
	@JoinColumn(name = "profile_id", referencedColumnName = "id")
	private Profile profile;

	@Column(name = "title", length = 30, nullable = false)
	private String title;

	@Column(name = "done", nullable = false)
	private boolean done;

	public Interview(Profile profile, String title) {
		this.profile = profile;
		this.title = title;
		this.done = false;
	}

	public Interview(Profile profile, String title, boolean done) {
		this.profile = profile;
		this.title = title;
		this.done = done;
	}
}
