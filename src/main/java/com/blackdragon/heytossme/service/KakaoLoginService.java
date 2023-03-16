package com.blackdragon.heytossme.service;

import com.blackdragon.heytossme.persist.MemberRepository;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class KakaoLoginService {

    private final MemberRepository memberRepository;

    public String getAccessToken(String authorizationCode) throws RuntimeException {
        String requestURL = "https://kauth.kakao.com/oauth/token";
        String kakaoToken = "";
        String client_id = "5e7a808a1af99e1088e5d4eb40239800";
        String redirect_uri = "http://localhost:8080/login/oauth2/kakao";

        try {
            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            //InputStream 으로 서버로 응답을 받을지 유무
            conn.setDoOutput(true);

            //getOutputStream() -> 메시지 바디에 포함
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + client_id); // REST_API 키 본인이 발급받은 key 넣어주기
            sb.append("&redirect_uri=" + redirect_uri); // REDIRECT_URI 본인이 설정한 주소 넣어주기
            sb.append("&code=" + authorizationCode);
            bw.write(sb.toString());
            bw.flush();

            //200 성공
            int responseCode = conn.getResponseCode();
            log.info("responseCode :" + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            kakaoToken = jsonObject.get("id_token").toString();

            br.close();
            bw.close();


        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        /**
         * 토큰을 까서 이메일 추출
         * if 있으면 -> 로그인 로직
         *    없으면 -> 회원가입 후 로그인
         * 로그인 완료 후 AccessToken 발급 리턴
         */

        return kakaoToken;
    }

}