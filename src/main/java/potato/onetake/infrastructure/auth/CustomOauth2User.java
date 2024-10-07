package potato.onetake.infrastructure.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOauth2User extends DefaultOAuth2User {

	private final String uuid;

	public CustomOauth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, String uuid) {
		super(authorities, attributes, nameAttributeKey);
		this.uuid = uuid;
	}

}
