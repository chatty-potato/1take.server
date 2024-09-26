package potato.onetake.domain.Auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import potato.onetake.domain.Auth.cookie.CookieShop;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private static final int ACCESS_TOKEN_EXPIRES_IN_SECONDS = 60 * 60;
	private static final int REFRESH_TOKEN_EXPIRES_IN_SECONDS = 60 * 60 * 24;

	@Override
	public void onAuthenticationSuccess(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final Authentication authentication)
		throws IOException {
		log.info("onAuthenticationSuccess");

		CookieShop.bakedCookie(
			response,
			JwtContants.ACCESS_TOKEN,
	}
}
