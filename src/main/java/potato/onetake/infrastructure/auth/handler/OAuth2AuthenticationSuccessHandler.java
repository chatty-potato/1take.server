package potato.onetake.infrastructure.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import potato.onetake.infrastructure.auth.cookie.CookieShop;
import potato.onetake.infrastructure.jwt.JwtTokenProvider;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final JwtTokenProvider jwtTokenProvider;
	private final RedisTemplate<String, String> redisTemplate;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication) throws IOException, ServletException {
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		String uuid = oAuth2User.getAttribute("uuid");

		// Access Token 및 Refresh Token 생성
		final String accessToken = jwtTokenProvider.createAccessToken(uuid);
		final String refreshToken = jwtTokenProvider.createRefreshToken(uuid);

		// Refresh Token을 Redis에 저장 (키: 이메일, 값: 토큰)
		redisTemplate.opsForValue()
			.set("refreshToken:" + uuid, refreshToken, jwtTokenProvider.getRefreshTokenValidity(), TimeUnit.MILLISECONDS);

		// Access Token을 쿠키에 저장
		CookieShop.bakedCookie(
			request,
			response,
			"accessToken",
			(int) jwtTokenProvider.getAccessTokenValidity(),
			accessToken,
			false
		);

		// Refresh Token을 쿠키에 저장
		CookieShop.bakedCookie(
			request,
			response,
			"refreshToken",
			(int) jwtTokenProvider.getRefreshTokenValidity(),
			refreshToken,
			true
		);

		// 로그인 후 리다이렉트
		response.sendRedirect("/");
	}
}
