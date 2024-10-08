package potato.onetake.infrastructure.auth.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieShop {
	/**
	 * cookie.setDomain("loalhost") 유효한 도메인에서만 쿠키허용
	 * 		 .setPath("/") 사이트 전체 "/"에 대해 쿠키가 유효하게 설정
	 * 		 .setMaxAge(expiry) 쿠키 만료시간 설정. 초 단위
	 * 		 .setHttpOnly(http) HttpOnly가 true면 Javascript로 쿠키에 접근 불가
	 * 		 .setSecure(true) 쿠키가 HTTPS 연결에서만 전송되도록 설정
	 *
	 *  response.addCookie(cookie) HttpServletResponse객체에 추가 후 클라이언트 전송
	 * */
	public static void bakedCookie(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final String key,
		final int expiry,
		final String token,
		final boolean http) {
		final Cookie cookie = new Cookie(key, token);

		// 요청 도메인을 기반으로 도메인 설정 (localhost가 아닌 실제 도메인 적용)
		// String domain = request.getServerName();
		// cookie.setDomain(domain);

		cookie.setDomain("localhost");

		cookie.setPath("/");
		cookie.setMaxAge(expiry);
		cookie.setHttpOnly(http);

		// 요청이 HTTPS인 경우에만 Secure 속성 적용
		cookie.setSecure(request.isSecure());

		response.addCookie(cookie);
	}
}
