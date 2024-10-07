package potato.onetake.infrastructure.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import potato.onetake.domain.member.dao.MemberRepository;
import potato.onetake.domain.member.domain.Member;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final AuthRepository authRepository;
	private final MemberRepository memberRepository;


	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		// 기본 OAuth2UserService를 통해 사용자 정보 가져오기
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);

		// 클라이언트 등록 ID (예: kakao, github)
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		// OAuth2 로그인 시 키가 되는 필드명
		String userNameAttributeName = userRequest.getClientRegistration()
			.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

		// 사용자 정보
		Map<String, Object> attributes = oAuth2User.getAttributes();

		// 공급자별로 사용자 정보 파싱
		OAuthAttributes extractAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, attributes);

		if (extractAttributes == null) {
			log.error("OAuthAttributes is null");
			throw new OAuth2AuthenticationException("Failed to extract OAuthAttributes");
		}


		// Auth와 Member 엔티티 생성 또는 업데이트
		 Auth auth = saveOrUpdate(extractAttributes);
		 String uuid = auth.getUuid();


		// SecurityContext에 저장될 객체 반환
		return new CustomOauth2User (
			Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
			attributes,
			userNameAttributeName,
			uuid);
	}


	private Auth saveOrUpdate(OAuthAttributes attributes) {
		Auth auth = authRepository.findByProviderAndProviderId(attributes.getProvider(), attributes.getProviderId())
			.orElseGet(() -> new Auth(attributes.getProvider(), attributes.getProviderId(), UUID.randomUUID().toString()));

		Member member = auth.getMember();
		if (member == null) {
			member = new Member();
			auth.setMember(member);
			member.getAuths().add(auth);
			memberRepository.save(member);
		}

		authRepository.save(auth);

		return auth;
	}


}
