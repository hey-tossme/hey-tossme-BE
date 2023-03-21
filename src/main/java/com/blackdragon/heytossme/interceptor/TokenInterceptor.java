package com.blackdragon.heytossme.interceptor;

import com.blackdragon.heytossme.component.AuthExtractor;
import com.blackdragon.heytossme.component.TokenProvider;
import com.blackdragon.heytossme.exception.MemberException;
import com.blackdragon.heytossme.exception.errorcode.MemberErrorCode;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenInterceptor implements HandlerInterceptor {

	private final TokenProvider tokenProvider;
	private final AuthExtractor authExtractor;
	public static final String AUTHORIZATION = "Authorization";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {

		String refreshToken = null;
		String accessToken = null;
		Cookie cookie = null;
		Object userId = null;

		accessToken = authExtractor.extractAccessToken(request, "Bearer");
		refreshToken =  authExtractor.extractRefreshToken(request);

		if (!StringUtils.hasText(accessToken)) {
			return false;
		}

		if (!tokenProvider.validateToken(accessToken)) {
			if (!tokenProvider.isExpiredRefreshToken(refreshToken)) {
				accessToken = this.updateAccessToken(refreshToken);
				userId = tokenProvider.getUserId(accessToken);
			} else {	//refresh tokne이 만료되어 로그아웃 + 쿠키삭제
				cookie.setMaxAge(0);
				throw new MemberException(MemberErrorCode.UNAUTHORIZED);
			}
		} else {
			userId = tokenProvider.getUserId(accessToken);
		}

		request.setAttribute("userId", userId);
		request.setAttribute("accessToken", accessToken);
		return true;
	}

	public String updateAccessToken(String refreshToken) {
		Claims claims = tokenProvider.getUserInfo(refreshToken);
		String email = claims.getSubject();
		Long id = Long.valueOf(String.valueOf( claims.get("id")));
		return tokenProvider.generateToken(id, email);
	}
}
