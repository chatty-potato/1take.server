package potato.onetake.infrastructure.auth;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import potato.onetake.domain.member.domain.Member;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "auth")
public class Auth {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "provider", length = 20, nullable = false)
	private String provider;

	@Column(name = "provider_id", length = 50, nullable = false)
	private String providerId;

	@Column(name = "uuid", length = 50, nullable = false)
	private String uuid;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	public Auth(String provider, String providerId, String uuid) {
		this.provider = provider;
		this.providerId = providerId;
		this.uuid = uuid;
	}
}
