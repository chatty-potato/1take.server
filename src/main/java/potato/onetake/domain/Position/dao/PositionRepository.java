package potato.onetake.domain.Position.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import potato.onetake.domain.Member.domain.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
}
