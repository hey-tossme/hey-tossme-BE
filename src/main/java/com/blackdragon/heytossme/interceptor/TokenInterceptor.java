package com.blackdragon.heytossme.interceptor;

import com.blackdragon.heytossme.component.AuthExtractor;
import com.blackdragon.heytossme.component.TokenProvider;
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

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) {

		Object userId;
		String accessToken = authExtractor.extractAccessToken(request, "Bearer");

		if (!StringUtils.hasText(accessToken)) {
			return false;
		}
    
		if (!tokenProvider.validateToken(accessToken, true)) {//Access토큰만료
			return false;
		}

		userId = tokenProvider.getUserId(accessToken, true);

		request.setAttribute("userId", userId);
		request.setAttribute("accessToken", accessToken);

		return true;
	}

}
