package potato.onetake.domain.member.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import potato.onetake.domain.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
