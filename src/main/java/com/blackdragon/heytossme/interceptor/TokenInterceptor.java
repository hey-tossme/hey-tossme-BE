package com.blackdragon.heytossme.interceptor;

import com.blackdragon.heytossme.component.AuthExtractor;
import com.blackdragon.heytossme.component.JWTUtil;
import com.blackdragon.heytossme.exception.CustomException;
import com.blackdragon.heytossme.exception.ErrorCode;
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

		boolean isRefreshToken = checkRefreshToken(request);
		log.info("isrefreshtoken : " + isRefreshToken);

		String token = "";
		if (isRefreshToken) {
			token = authExtractor.extract(request, "Bearer");
		} else {
			token = request.getHeader("Authorization");
		}

		if (!StringUtils.hasText(token)) {
			return false;
		}

		if (!jwtUtil.validateToken(token, isRefreshToken)) {
			throw new CustomException(ErrorCode.EXPIRED_TOKEN);
		}

		if (isRefreshToken) {
			Long userId = jwtUtil.getUserId(token, isRefreshToken);
			String userEmail = jwtUtil.getUserEmail(token, isRefreshToken);

			//토큰 재발급로직수행
			String accessToken = jwtUtil.generateToken(userId, userEmail, false);
			String refreshToken = jwtUtil.generateToken(userId, userEmail, true);

			response.setHeader("accessToken", accessToken);
			response.setHeader("refreshToken", refreshToken);

			return true;
		}

		Long userId = jwtUtil.getUserId(token, isRefreshToken);
		request.setAttribute("userId", userId);
		return true;
	}

	private boolean checkRefreshToken(HttpServletRequest request) {
		Enumeration<String> headers = request.getHeaders(AUTHORIZATION);
		while (headers.hasMoreElements()) {
			String value = headers.nextElement();
			if (value.toLowerCase().startsWith("bearer")) {
				return false;
			}
		}
		return true;
	}

}
