package com.blackdragon.heytossme.component;

import com.blackdragon.heytossme.dto.MemberDto.AuthResponse;
import com.blackdragon.heytossme.exception.MemberException;
import com.blackdragon.heytossme.exception.errorcode.MemberErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Log4j2
@RequiredArgsConstructor
public class TokenProvider {

    @Value("${com.blackdragon.jwt.accesskey}")
    private String accessKey;

    @Value("${com.blackdragon.jwt.refreshkey}")
    private String refreshKey;
    private final AuthExtractor authExtractor;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L;    //1시간
    //	private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L ;	//1초
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 3L;    //3시간
//	private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L;	//1초

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
                .signWith(SignatureAlgorithm.HS512,
                        isAccessToken ? this.accessKey : this.refreshKey)
                .compact();
    }

    private Claims getClaims(String token, boolean isAccessKey) {
        Claims claim;
        try {
            claim = Jwts.parser()
                    .setSigningKey(isAccessKey ? this.accessKey : this.refreshKey)
                    .parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            if (isAccessKey) {
                throw new MemberException(MemberErrorCode.INVALID_KEY);
            } else {
                throw new MemberException(MemberErrorCode.METHOD_NOT_ALLOWED);
            }
        } catch (SignatureException e) {
            throw new MemberException(MemberErrorCode.INVALID_KEY);
        }
        return claim;
    }

    public boolean validateToken(String token, boolean isAccessKey) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        Claims claims;
        try {
            claims = this.getClaims(token, isAccessKey);
        } catch (SignatureException e) {
            throw new MemberException(MemberErrorCode.INVALID_SIGNATURE);
        } catch (ExpiredJwtException e) {
            throw new MemberException(MemberErrorCode.INVALID_KEY);
        } catch (Exception e) {
            throw new MemberException(MemberErrorCode.SERVER_ERROR);
        }
        return !claims.getExpiration().before(new Date());
    }

    public boolean isExpiredRefreshToken(HttpServletRequest request, boolean isAccessKey) {
        AuthResponse auth = authExtractor.extractRefreshToken(request);
        String refreshToken = auth.getRefreshToken();

        if (!StringUtils.hasText(refreshToken)) {
            return false;
        }
        Claims claims;
        try {
            claims = this.getClaims(refreshToken, isAccessKey);
        } catch (SignatureException e) {
            throw new MemberException(MemberErrorCode.INVALID_SIGNATURE);
        } catch (ExpiredJwtException e) {
            return false;
        }
        return !claims.getExpiration().before(new Date());
    }

    public Claims getUserInfo(String token, boolean isAccessKey) {
        return this.getClaims(token, isAccessKey);
    }

    //토큰이 만료되더라도 id만 가져올 수 있음
    public Long getUserId(String token, boolean isAccessKey) {
        return Long.valueOf(String.valueOf(this.getClaims(token, isAccessKey).get("id")));
    }

    public String updateAccessToken(String refreshToken) {
        Claims claims = this.getUserInfo(refreshToken, false);
        String email = claims.getSubject();
        Long id = Long.valueOf(String.valueOf(claims.get("id")));
        return this.generateToken(id, email, true);
    }
}
