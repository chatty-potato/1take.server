package potato.onetake.domain.member.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import potato.onetake.global.BaseEntity.BaseEntity;
import potato.onetake.infrastructure.auth.Auth;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "member")
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

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
