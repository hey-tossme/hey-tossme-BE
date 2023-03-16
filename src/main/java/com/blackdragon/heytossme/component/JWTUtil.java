package com.blackdragon.heytossme.component;

import static com.blackdragon.heytossme.exception.ErrorCode.INCORRECT_KEY;
import static com.blackdragon.heytossme.exception.ErrorCode.METHOD_NOT_ALLOWED;
import static com.blackdragon.heytossme.exception.ErrorCode.SERVER_ERROR;

import com.blackdragon.heytossme.dto.MemberDto.ResponseToken;
import com.blackdragon.heytossme.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import java.util.Date;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Log4j2
public class JWTUtil {

	@Value("${com.blackdragon.jwt.accesskey}")
	private String accessKey;

	private final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 30;	//30시간
	private final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 30 * 30;	//30일


	public String generateToken(Long id, String email) {

		Claims claims = Jwts.claims().setSubject(email);
		claims.put("id", id);

		Date now = new Date();
		Date expiredDate = new Date(now.getTime() + this.ACCESS_TOKEN_EXPIRE_TIME);

		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(expiredDate)
				.signWith(SignatureAlgorithm.HS512, this.accessKey)
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
			throw new CustomException(INCORRECT_KEY);
		} catch (ExpiredJwtException e) {
			log.info(" >>>>>>>ExpiredJwtException" + e.getMessage());
			log.info(" >>>>>>>ExpiredJwtException" + e.getCause());
			throw new CustomException(METHOD_NOT_ALLOWED);
		} catch (Exception e) {
			log.info(" >>>>>>>Exception" + e.getMessage());
			log.info(" >>>>>>>Exception" + e.getCause());
			throw new CustomException(SERVER_ERROR);
		}
	}

	//유효기간 확인 및 refresh 만료 메서드 작성해서 만료되었다면 거기서 예외날리고
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

	public Long getUserId(String token) {
		return (Long)this.getClaims(token).get("id");
	}

}