package com.blackdragon.heytossme.component;

import static com.blackdragon.heytossme.exception.ErrorCode.INVALID_TOKEN;

import com.blackdragon.heytossme.exception.CustomException;
import com.blackdragon.heytossme.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Log4j2
public class JWTUtil {

	@Value("${com.blackdragon.jwt.secret}")
	private String key;

	private final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60;	//1시간


	public String generateToken(Long id, String email) {

		Claims claims = Jwts.claims().setSubject(email);
		claims.put("id", id);

		Date now = new Date();
		Date expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(expiredDate)
				.signWith(SignatureAlgorithm.HS512, this.key)
				.compact();
	}

	private Claims getClaims(String token){
		try {
			return Jwts.parser().setSigningKey(this.key).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			throw new CustomException(INVALID_TOKEN);
		}
	}

	public boolean validateToken(String token) {
		if (!StringUtils.hasText(token)) return false;

		Claims claims = this.getClaims(token);
		return !claims.getExpiration().before(new Date());
	}

	public String getUserEmail(String token) {
		return this.getClaims(token).getSubject();
	}

	public Long getUserId(String token) {
		return (Long)this.getClaims(token).get("id");
	}

}