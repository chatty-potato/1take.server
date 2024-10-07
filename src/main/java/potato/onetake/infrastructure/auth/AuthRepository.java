package potato.onetake.infrastructure.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {
	Optional<Auth> findByProviderAndProviderId(String provider, String providerId);
}
