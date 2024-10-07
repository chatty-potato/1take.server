package potato.onetake.infrastructure.auth;

import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

public class CustomOauth2User extends DefaultOAuth2User {
	private final String uuid;

	public CustomOauth2User(DefaultOAuth2User defaultOAuth2User, String uuid) {
		super(defaultOAuth2User.getAuthorities(), defaultOAuth2User.getAttributes(), defaultOAuth2User.getName());
		this.uuid = uuid;
	}

	public String getUuid() {
		return uuid;
	}
}
