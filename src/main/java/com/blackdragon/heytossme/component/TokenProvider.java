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

	private final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 ;	//1시간
	private final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 3;	//3시간

	public String generateToken(Long id, String email, boolean isAccessToken) {

		Claims claims = Jwts.claims().setSubject(email);
		claims.put("id", id);

		Date now = new Date();
		Date expiredDate = new Date(now.getTime() +
				(isAccessToken ? this.ACCESS_TOKEN_EXPIRE_TIME : REFRESH_TOKEN_EXPIRE_TIME));

		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(expiredDate)
				.signWith(SignatureAlgorithm.HS512, isAccessToken ? this.accessKey : this.refreshKey)
				.compact();
	}

	private Claims getClaims(String token){
		try {
			log.info(">>>>>>>>> getclaims >>>>>>>>>>");
			return Jwts.parser()
					.setSigningKey(accessKey)
					.parseClaimsJws(token).getBody();
		} catch (SignatureException e) {
			log.info(" >>>>>>SignatureException" + e.getMessage());
			log.info(" >>>>>>SignatureException" + e.getCause());
			throw new MemberException(MemberErrorCode.INVALID_KEY);
		} catch (ExpiredJwtException e) {
			log.info(" >>>>>>>ExpiredJwtException" + e.getMessage());
			log.info(" >>>>>>>ExpiredJwtException" + e.getCause());
			throw new MemberException(MemberErrorCode.METHOD_NOT_ALLOWED);
		} catch (Exception e) {
			log.info(" >>>>>>>Exception" + e.getMessage());
			log.info(" >>>>>>>Exception" + e.getCause());
			throw new MemberException(MemberErrorCode.SERVER_ERROR);
		}
	}

	public boolean validateToken(String token) {
		if (!StringUtils.hasText(token)) return false;
		Claims claims = this.getClaims(token);
		return !claims.getExpiration().before(new Date());
	}

	public boolean isExpiredRefreshToken(String refreshKey) {
		return this.validateToken(refreshKey);
	}

	public Claims getUserInfo(String token) {
		return this.getClaims(token);
	}

	public Object getUserId(String token) {
		return Long.valueOf(String.valueOf( this.getClaims(token).get("id")));
	}

}