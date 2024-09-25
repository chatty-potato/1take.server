package potato.onetake.domain.Position.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import potato.onetake.domain.Position.domain.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
