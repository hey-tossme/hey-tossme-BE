package com.blackdragon.heytossme.component;

import com.blackdragon.heytossme.dto.MemberDto.AuthResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthExtractor {

    public static final String AUTHORIZATION = "Authorization";

    public String extractAccessToken(HttpServletRequest request, String type) {
        Enumeration<String> headers = request.getHeaders(AUTHORIZATION);
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if (value.toLowerCase().startsWith(type.toLowerCase())) {
                return value.substring(type.length()).trim();
            }
        }
        return Strings.EMPTY;
    }

    public AuthResponse extractRefreshToken(HttpServletRequest request) {

        String refreshToken = "";
        Cookie cookie = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie _cookie : cookies) {
                if ("refreshToken".equals(_cookie.getName())) {
                    refreshToken = _cookie.getValue();
                    cookie = _cookie;
                    log.info("refresh cookie name : " + _cookie.getName());
                    break;
                }
            }
        }

        return new AuthResponse(refreshToken, cookie);
    }
}
