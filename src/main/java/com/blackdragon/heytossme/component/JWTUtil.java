package com.blackdragon.heytossme.component;

import static com.blackdragon.heytossme.exception.ErrorCode.EXPIRED_TOKEN;
import static com.blackdragon.heytossme.exception.ErrorCode.INCORRECT_KEY;
import static com.blackdragon.heytossme.exception.ErrorCode.SERVER_ERROR;

import com.blackdragon.heytossme.exception.CustomException;
import com.blackdragon.heytossme.exception.ErrorCode;
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
public class JWTUtil {

	@Value("${com.blackdragon.jwt.accesskey}")
	private String accessKey;

	@Value("${com.blackdragon.jwt.refreshkey}")
	private String refreshKey;

	private final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 30;	//30시간
	private final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 30 * 30;	//30일


	public String generateToken(Long id, String email, boolean isRefreshToken) {

		Claims claims = Jwts.claims().setSubject(email);
		claims.put("id", id);

		Date now = new Date();
		Date expiredDate = new Date(now.getTime() +
				(isRefreshToken ? this.REFRESH_TOKEN_EXPIRE_TIME : this.ACCESS_TOKEN_EXPIRE_TIME));

		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(expiredDate)
				.signWith(SignatureAlgorithm.HS512,
						isRefreshToken ? this.refreshKey : this.accessKey)
				.compact();
	}

	private Claims getClaims(String token, boolean isRefreshToken){
		try {
			log.info(">>>>>>>>> getclaims >>>>>>>>>>");
			return Jwts.parser()
					.setSigningKey(isRefreshToken ? refreshKey : accessKey)
					.parseClaimsJws(token).getBody();
		} catch (SignatureException e) {
			log.info(" >>>>>>SignatureException" + e.getMessage());
			log.info(" >>>>>>SignatureException" + e.getCause());
			throw new CustomException(INCORRECT_KEY);
		} catch (ExpiredJwtException e) {
			log.info(" >>>>>>>ExpiredJwtException" + e.getMessage());
			log.info(" >>>>>>>ExpiredJwtException" + e.getCause());
			throw new CustomException(EXPIRED_TOKEN);
		} catch (Exception e) {
			log.info(" >>>>>>>Exception" + e.getMessage());
			log.info(" >>>>>>>Exception" + e.getCause());
			throw new CustomException(SERVER_ERROR);
		}
	}

	public boolean validateToken(String token, boolean isRefreshToken) {
		if (!StringUtils.hasText(token)) return false;
		Claims claims = this.getClaims(token, isRefreshToken);
		return !claims.getExpiration().before(new Date());
	}

	public String getUserEmail(String token, boolean isRefreshToken) {
		return this.getClaims(token, isRefreshToken).getSubject();
	}

	public Long getUserId(String token, boolean isRefreshToken) {
		return (Long)this.getClaims(token, isRefreshToken).get("id");
	}

}