package potato.onetake.infrastructure.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	public Auth(String provider, String providerId) {
		this.provider = provider;
		this.providerId = providerId;
	}
}
