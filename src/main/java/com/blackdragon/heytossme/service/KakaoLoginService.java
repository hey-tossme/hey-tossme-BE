package com.blackdragon.heytossme.service;

import com.blackdragon.heytossme.component.TokenProvider;
import com.blackdragon.heytossme.dto.MemberDto.Response;
import com.blackdragon.heytossme.persist.MemberRepository;
import com.blackdragon.heytossme.persist.entity.Member;
import com.blackdragon.heytossme.type.MemberSocialType;
import com.blackdragon.heytossme.type.MemberStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
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
    private final TokenProvider tokenProvider;

    public String getAccessToken(String authorizationCode) {

        //kakao openId 토큰 발급 받기
        String token = getKakaoOpenIdToken(authorizationCode);
        //token 인코딩 해서 유저 정보 추출
        String payload = getPayload(token);
        //payload 파싱해서 map 객체에 담아주기
        Map<String, String> kakaoInfo = getUserKakaoInfo(payload);
        //DB 에서 해당 유저 이메일로 찾기
        Member member = getOrSaveUserByEmail(kakaoInfo);

        Response response = new Response();
        response.setId(member.getId());
        response.setEmail(member.getEmail());

        return tokenProvider.generateToken(response.getId(), response.getEmail());
    }

    private Member getOrSaveUserByEmail(Map<String, String> kakaoInfo) {
        Optional<Member> memberOptional = memberRepository.findByEmail(kakaoInfo.get("email"));

        if (memberOptional.isPresent()) {
            return memberOptional.get();
        } else {
            Member newMember = Member.builder()
                    .email(kakaoInfo.get("email"))
                    .name(kakaoInfo.get("nickname"))
                    .socialLoginType(MemberSocialType.KAKAO.toString())
                    .imageUrl(kakaoInfo.get("picture"))
                    .password(UUID.randomUUID().toString())
                    .status(MemberStatus.NORMAL.toString())
                    .build();
            return memberRepository.save(newMember);
        }
    }

    private String getPayload(String token) {
        String requestURL = "https://kauth.kakao.com/oauth/tokeninfo";
        String payload;

        try {
            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            //InputStream 으로 서버로 응답을 받을지 유무
            conn.setDoOutput(true);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write("id_token=" + token);
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
            payload = result;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return payload;
    }

    private Map<String, String> getUserKakaoInfo(String payload) {
        Map<String, String> kakaoInfo;
        ObjectMapper mapper = new ObjectMapper();

        try {
            kakaoInfo = mapper.readValue(payload, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return kakaoInfo;
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
            String sb = "grant_type=authorization_code"
                    + "&client_id=" + client_id // REST_API 키 본인이 발급받은 key 넣어주기
                    + "&redirect_uri=" + redirect_uri // REDIRECT_URI 본인이 설정한 주소 넣어주기
                    + "&code=" + authorizationCode;
            bw.write(sb);
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

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            kakaoToken = jsonObject.get("id_token").toString();

            br.close();
            bw.close();

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        return kakaoToken;
    }

}