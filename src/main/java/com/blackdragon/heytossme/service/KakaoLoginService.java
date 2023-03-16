package com.blackdragon.heytossme.service;

import com.blackdragon.heytossme.component.JWTUtil;
import com.blackdragon.heytossme.dto.KakaoTokenDto;
import com.blackdragon.heytossme.dto.MemberDto;
import com.blackdragon.heytossme.dto.MemberDto.Response;
import com.blackdragon.heytossme.persist.MemberRepository;
import com.blackdragon.heytossme.persist.entity.Member;
import com.blackdragon.heytossme.type.MemberSocialType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
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
    private final JWTUtil jwtUtil;

    public String getAccessToken(String authorizationCode) throws RuntimeException {

        log.info("**************GETACCESSTOKEN 메서드 실행************");

        //kakao openId 토큰 발급 받기
        String token = getKakaoOpenIdToken(authorizationCode);

        log.info("***************토큰 발급 받기 완료**************");
        log.info(token);
        //token 인코딩 해서 유저 정보 추출
        KakaoTokenDto payload = getPayload(token);
        //레포에서 해당 유저 정보 찾기 없으면 회원가입 로직 있으면 로그인 로직 진행
        Member member = memberRepository.findByEmail(payload.getEmail());
        //유저 정보 없으면 회원 가입 진행
        if (member == null) {
            member = memberRepository.save(
                    Member.builder()
                            .email(payload.getEmail())
                            .name(payload.getNickname())
                            .socialLoginType(MemberSocialType.KAKAO.toString())
                            .build()
            );
        }
        Response response = new Response();
        response.setId(member.getId());
        response.setEmail(member.getEmail());

        return jwtUtil.generateToken(response.getId(), response.getEmail());
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private KakaoTokenDto getPayload(String token) {
        String requestURL = "https://kauth.kakao.com/oauth/tokeninfo";
        KakaoTokenDto kakaoTokenDto;

        try {
            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            //InputStream 으로 서버로 응답을 받을지 유무
            conn.setDoOutput(true);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("id_token=" + token);
            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();
            log.info("responseCode :" + responseCode);
            log.info("responseMessage :" + conn.getResponseMessage());

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info(result);

            ObjectMapper objectMapper = new ObjectMapper();
            kakaoTokenDto = objectMapper.readValue(result, KakaoTokenDto.class);

            log.info(kakaoTokenDto);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return kakaoTokenDto;
    }

    private String getKakaoOpenIdToken(String authorizationCode) {

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
            log.info("responseMessage :" + conn.getResponseMessage());

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