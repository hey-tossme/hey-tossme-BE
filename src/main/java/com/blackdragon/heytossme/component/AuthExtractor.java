package com.blackdragon.heytossme.component;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

@Component
public class AuthExtractor {

    public static final String AUTHORIZATION = "Authorization";
    public static final String ACCESS_TOKEN_TYPE =
            AuthExtractor.class.getSimpleName() + ".ACCESS_TOKEN_TYPE";

    public String extract(HttpServletRequest request, String type) {
        Enumeration<String> headers = request.getHeaders(AUTHORIZATION);
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if (value.toLowerCase().startsWith(type.toLowerCase())) {
                return value.substring(type.length()).trim();
            }
        }

        return Strings.EMPTY;
    }


}
