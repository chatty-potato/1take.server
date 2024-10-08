package potato.onetake.infrastructure.jwt;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import potato.onetake.domain.member.dao.MemberRepository;
import potato.onetake.domain.member.domain.Member;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String uuid) throws UsernameNotFoundException {
		Member member = memberRepository.findByUuid(uuid)
			.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다. UUID: " + uuid));

		// 사용자 정보와 권한을 담은 UserDetails 객체 반환
		return new User(
			member.getUuid(), // UUID를 username으로 사용
			"", // 패스워드는 OAuth2 인증에서 필요하지 않으므로 빈 값으로 처리
			Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
		);
	}

}
