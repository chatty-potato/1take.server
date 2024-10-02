package potato.onetake.infrastructure.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
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
@Component
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String secretKey;

	// 토큰 유효기간
	private final long accessTokenValidity = 3600000L; // 1시간
	private final long refreshTokenValidity = 604800000L; // 7일

	private final UserDetailsService userDetailsService;

	public JwtTokenProvider(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public String createAccessToken(String email) {
		Claims claims = Jwts.claims().setSubject(email);

		Date now = new Date();
		Date validity = new Date(now.getTime() + accessTokenValidity);

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(validity)
			.signWith(getSigningKey(), SignatureAlgorithm.ES256)
			.compact();
	}

	public String createRefreshToken(String email) {
		Claims claims = Jwts.claims().setSubject(email);

		Date now = new Date();
		Date validity = new Date(now.getTime() + refreshTokenValidity);

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(validity)
			.signWith(getSigningKey(), SignatureAlgorithm.HS256)
			.compact();
	}

	public String getEmail(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(getSigningKey())
			.build()
			.parseClaimsJws(token)
			.getBody()
			.getSubject();
	}

	// 토큰에서 인증 정보 추출
	public Authentication getAuthentication(String token) {
		String email = getEmail(token);
		UserDetails userDetails = userDetailsService.loadUserByUsername(email);
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public boolean validateToken(String toekn) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build().parseClaimsJws(toekn);

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

}
