package com.blackdragon.heytossme.interceptor;

import com.blackdragon.heytossme.component.AuthExtractor;
import com.blackdragon.heytossme.component.TokenProvider;
import com.blackdragon.heytossme.dto.MemberDto.AuthResponse;
import io.jsonwebtoken.Claims;
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
			Object handler) throws Exception {

		Object userId = null;
		String accessToken = authExtractor.extractAccessToken(request, "Bearer");
		AuthResponse authResponse =  authExtractor.extractRefreshToken(request);

		if (!StringUtils.hasText(accessToken)) {
			return false;
		}

		if (!tokenProvider.validateToken(accessToken)) {	//access만료
			if (!tokenProvider.isExpiredRefreshToken(authResponse.getRefreshToken())) {	//refresh정상
				accessToken = this.updateAccessToken(authResponse.getRefreshToken());
				userId = tokenProvider.getUserId(accessToken);
			} else {	//refresh만료
				response.sendRedirect(request.getContextPath() + "/member/logout/auth");
			}
		} else {	//access 정상
			if (!tokenProvider.isExpiredRefreshToken(authResponse.getRefreshToken())){	//refresh정상
				userId = tokenProvider.getUserId(accessToken);
			} else {	//refresh만료
				response.sendRedirect(request.getContextPath() + "/member/logout/auth");
			}
		}

		request.setAttribute("userId", userId);
		request.setAttribute("accessToken", accessToken);
		return true;
	}

	public String updateAccessToken(String refreshToken) {
		Claims claims = tokenProvider.getUserInfo(refreshToken);
		String email = claims.getSubject();
		Long id = Long.valueOf(String.valueOf( claims.get("id")));
		return tokenProvider.generateToken(id, email, true);
	}
}
