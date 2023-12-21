package com.twoper.toyou.domain.letter.service;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class ContentCheckClient {

    public static final String AI_SERVER_URL = "http://18.167.51.58:8000";  // 파이썬 FastAPI 서버 URL로 변경

    public static String checkContent(String content) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // AI 서버에 전송할 데이터 구성
        String requestBody = "{\"content\":\"" + content + "\"}";

        // AI 서버에 POST 요청 보내기
        ResponseEntity<String> responseEntity = new RestTemplate()
                .exchange(AI_SERVER_URL + "/letter/write", HttpMethod.POST, new HttpEntity<>(requestBody, headers), String.class);

        // 응답 받은 결과 반환
        return responseEntity.getBody();
    }
}
