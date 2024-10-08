package potato.onetake.domain.member.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import potato.onetake.global.BaseEntity.BaseEntity;
import potato.onetake.infrastructure.auth.Auth;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "member")
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "uuid", length = 36, nullable = false)
	private String uuid;

	@Column(name = "img")
	private String img;

	@Column(name = "name", length = 20, nullable = false)
	private String name;

	@Column(name = "email", length = 50, nullable = false)
	private String email;

	@Column(name = "alias", length = 20, nullable = true)
	private String alias;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<Auth> auths = new ArrayList<>();

}
