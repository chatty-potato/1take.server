package potato.onetake.infrastructure.auth;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

//	@todo Member Entity 추가 후 주석 해제
//	@ManyToOne
//	@JoinColumn(name = "member_id")
//	private Member member;

	public Auth(String provider, String providerId) {
		this.provider = provider;
		this.providerId = providerId;
	}
}
