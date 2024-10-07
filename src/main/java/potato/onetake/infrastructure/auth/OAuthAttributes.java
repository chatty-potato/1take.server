package potato.onetake.infrastructure.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Builder
@Getter
public class OAuthAttributes {

	private final Map<String, Object> attributes;
	private final String provider;
	private final String providerId;
	private final String email;
	private final String username;
	private final String nameAttributeKey;

	public static OAuthAttributes of(String provider, String userNameAttributeName, Map<String, Object> attributes) {
		if ("kakao".equals(provider)) {
			return ofKakao(userNameAttributeName, attributes);
		} else if ("github".equals(provider)) {
			return ofGitHub(userNameAttributeName, attributes);
		}

		return null;
	}

	private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
		Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
		String email = (String) kakaoAccount.get("email");
		Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
		String nickname = (String) profile.get("nickname");

		return OAuthAttributes.builder()
			.attributes(attributes)
			.provider("kakao")
			.providerId(attributes.get("id").toString())
			.email(email)
			.username(nickname)
			.nameAttributeKey(userNameAttributeName)
			.build();
	}

	private static OAuthAttributes ofGitHub(String userNameAttributeName, Map<String, Object> attributes) {
		String email = (String) attributes.get("email");
		String login = (String) attributes.get("login");

		return OAuthAttributes.builder()
			.attributes(attributes)
			.provider("github")
			.providerId(attributes.get("id").toString())
			.email(email)
			.username(login)
			.nameAttributeKey(userNameAttributeName)
			.build();
	}

}
