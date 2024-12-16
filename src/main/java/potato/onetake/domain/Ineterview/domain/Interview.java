package potato.onetake.domain.Ineterview.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import potato.onetake.domain.Position.domain.Profile;
import potato.onetake.global.BaseEntity.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="interview")
public class Interview extends BaseEntity {

	@ManyToOne()
	@JoinColumn(name = "profile_id", referencedColumnName = "id")
	private Profile profile;

	@Column(name = "title", length = 30, nullable = false)
	private String title;

	@Column(name = "done", nullable = false)
	private boolean done;

}
