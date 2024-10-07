package potato.onetake.infrastructure.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Getter
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String secretKey;

	// 토큰 유효기간
	// 1시간 -> 밀리초로 변환
	private static final long accessTokenValidity = 1000L * 60L * 60L; // 1시간
	// 7일 -> 밀리초로 변환
	private static final long refreshTokenValidity = 1000L * 60L * 60L * 24L * 7L; // 7일


	private final UserDetailsService userDetailsService;


	public String createAccessToken(String uuid) {
		Claims claims = Jwts.claims().setSubject(uuid);

		Date now = new Date();
		Date validity = new Date(now.getTime() + accessTokenValidity);

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(validity)
			.signWith(getSigningKey(), SignatureAlgorithm.HS256)
			.compact();
	}


	public String createRefreshToken(String uuid) {
		Claims claims = Jwts.claims().setSubject(uuid);

		Date now = new Date();
		Date validity = new Date(now.getTime() + refreshTokenValidity);

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(validity)
			.signWith(getSigningKey(), SignatureAlgorithm.HS256)
			.compact();
	}

	// JWT에서 UUID 추출
	public String getUuidFromToken(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(getSigningKey())
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getSubject();
	}

	// 토큰에서 인증 정보 추출
	public Authentication getAuthentication(String token) {
		String uuid = getUuidFromToken(token);
		UserDetails userDetails = userDetailsService.loadUserByUsername(uuid);
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build().parseClaimsJws(token);

			return true;
		} catch (JwtException | IllegalArgumentException e) {
			log.error("JWT 토큰 유효성 검증 실패", e);
			return false;
		}
	}

	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public long getAccessTokenValidity() {
		return accessTokenValidity;
	}

	public long getRefreshTokenValidity() {
		return refreshTokenValidity;
	}

}
