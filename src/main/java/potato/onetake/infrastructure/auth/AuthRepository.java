package potato.onetake.infrastructure.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Auth, Long> {
	Optional<Auth> findByProviderAndProviderId(String provider, String providerId);
}
