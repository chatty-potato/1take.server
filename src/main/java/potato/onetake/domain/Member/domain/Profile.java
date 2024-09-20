package potato.onetake.domain.Member.domain;

import jakarta.persistence.*;
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
@Table(name = "profile")
public class Profile extends BaseEntity {

	// 추후 Auth 관련 domain 작업이 끝난 후 추가
	//	@OneToOne()
	//	@JoinColumn(name = "auth_id", referencedColumnName = "id")
	//	private Auth auth;

	@Column(name = "alias", nullable = false, unique = true)
	private String alias;

	@Column(name = "img")
	private String img;

	public Profile(final String alias) {
		this.alias = alias;
	}
}
