package potato.onetake.domain.Member.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import potato.onetake.domain.Member.domain.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
