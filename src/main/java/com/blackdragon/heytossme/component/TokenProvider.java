package com.blackdragon.heytossme.component;

import com.blackdragon.heytossme.exception.MemberException;
import com.blackdragon.heytossme.exception.errorcode.MemberErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import java.util.Date;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Log4j2
public class TokenProvider {

	@Value("${com.blackdragon.jwt.accesskey}")
	private String accessKey;

	@Value("${com.blackdragon.jwt.refreshkey}")
	private String refreshKey;

	private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60L ;	//1시간
	private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 3L;	//3시간

	public String generateToken(Long id, String email, boolean isAccessToken) {
		Claims claims = Jwts.claims().setSubject(email);
		claims.put("id", id);

		Date now = new Date();
		Date expiredDate = new Date(now.getTime() +
				(isAccessToken ? ACCESS_TOKEN_EXPIRE_TIME : REFRESH_TOKEN_EXPIRE_TIME));

		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(expiredDate)
				.signWith(SignatureAlgorithm.HS512, isAccessToken ? this.accessKey : this.refreshKey)
				.compact();
	}

	private Claims getClaims(String token, boolean isAccessKey){

		return Jwts.parser()
				.setSigningKey(isAccessKey ? this.accessKey : this.refreshKey)
				.parseClaimsJws(token).getBody();
	}

	public boolean validateToken(String token, boolean isAccessKey) {
		if (!StringUtils.hasText(token)) return false;
		Claims claims;
		try {
			claims = this.getClaims(token, isAccessKey);
		} catch (SignatureException e) {
			log.info(" >>>>>>SignatureException" + e.getMessage());
			log.info(" >>>>>>SignatureException" + e.getCause());
			throw new MemberException(MemberErrorCode.INVALID_KEY);
		} catch (ExpiredJwtException e) {
			return false;
		} catch (Exception e) {
			log.info(" >>>>>>>Exception" + e.getMessage());
			log.info(" >>>>>>>Exception" + e.getCause());
			throw new MemberException(MemberErrorCode.SERVER_ERROR);
		}
		return !claims.getExpiration().before(new Date());
	}

	public boolean isExpiredRefreshToken(String refreshKey) {
		return this.validateToken(refreshKey, false);
	}

	public Claims getUserInfo(String token, boolean isAccessKey) {
		return this.getClaims(token, isAccessKey);
	}

	public Object getUserId(String token, boolean isAccessKey) {
		return Long.valueOf(String.valueOf( this.getClaims(token, isAccessKey).get("id")));
	}

}