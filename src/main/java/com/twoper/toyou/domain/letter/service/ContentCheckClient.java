package com.twoper.toyou.domain.letter.service;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class ContentCheckClient {

    public static final String FASTAPI_SERVER_URL = "http://15.165.110.46:8000/letter/write";

    public static String checkContent(String content) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // FastAPI 서버에 전송할 데이터 구성
        String requestBody = "{\"content\":\"" + content + "\"}";

        // FastAPI 서버에 POST 요청 보내기
        ResponseEntity<String> responseEntity = new RestTemplate()
                .exchange(FASTAPI_SERVER_URL, HttpMethod.POST, new HttpEntity<>(requestBody, headers), String.class);

        // 응답 받은 결과 반환
        return responseEntity.getBody();
    }
}
