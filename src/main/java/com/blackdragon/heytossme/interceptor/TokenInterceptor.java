package com.blackdragon.heytossme.interceptor;

import com.blackdragon.heytossme.component.AuthExtractor;
import com.blackdragon.heytossme.component.JWTUtil;
import com.blackdragon.heytossme.exception.CustomException;
import com.blackdragon.heytossme.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenInterceptor implements HandlerInterceptor {

	private final JWTUtil jwtUtil;
	private final AuthExtractor authExtractor;
	public static final String AUTHORIZATION = "Authorization";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {

		log.info(">>>>>>>>> 인터셉터 호출됨 >>>>>>>>>>>>");

		String accessToken = authExtractor.extract(request, "Bearer");
		Cookie cookie = request.getCookies()[0];
		String refreshToken = cookie.getAttribute("refreshToken");

		if (!StringUtils.hasText(accessToken)) {
			return false;
		}

		if (!jwtUtil.validateToken(accessToken)) {
//			throw new CustomException(ErrorCode.EXPIRED_TOKEN);
			if (!jwtUtil.isExpiredRefreshToken(refreshToken)) {
				//리프레쉬토큰이 만료되었다면
				cookie.setMaxAge(0);
			}
			// refresh token이 만료안되었다면 accessToken재발급
			accessToken = this.updateAccessToken(accessToken);
			response.setHeader("accessToken", accessToken);
		}

		Long userId = jwtUtil.getUserId(accessToken);
		request.setAttribute("userId", userId);
		return true;
	}

	public String updateAccessToken(String accessToken) {
		Claims claims = jwtUtil.getUserInfo(accessToken);
		String email = claims.getSubject();
		Long id = (Long) claims.get("id");
		return jwtUtil.generateToken(id, email);
	}
}
