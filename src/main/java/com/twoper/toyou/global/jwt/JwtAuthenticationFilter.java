//package com.twoper.toyou.global.jwt;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.twoper.toyou.domain.user.model.dto.LoginRequestDto;
//import com.twoper.toyou.global.jwt.JwtProperties;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.twoper.toyou.global.jwt.auth.PrincipalDetails;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Date;
//
// //401오류
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//     private final AuthenticationManager authenticationManager;
//
//     // Authentication 객체 만들어서 리턴 => 의존 : AuthenticationManager
//     // 인증 요청시에 실행되는 함수 => /login
//     @Override
//     public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
//             throws AuthenticationException {
//
//         System.out.println("JwtAuthenticationFilter : 진입");
//
//         // request에 있는 username과 password를 파싱해서 자바 Object로 받기
//         ObjectMapper om = new ObjectMapper();
//         LoginRequestDto loginRequestDto = null;
//         try {
//             loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//
//         System.out.println("JwtAuthenticationFilter : " + loginRequestDto);
//
//         // 유저네임패스워드 토큰 생성
//         UsernamePasswordAuthenticationToken authenticationToken =
//                 new UsernamePasswordAuthenticationToken(
//                         loginRequestDto.getUsername(),
//                         loginRequestDto.getPassword());
//
//         System.out.println("JwtAuthenticationFilter : 토큰생성완료");
//
//         // authenticate() 함수가 호출 되면 인증 프로바이더가 유저 디테일 서비스의
//         // loadUserByUsername(토큰의 첫번째 파라메터) 를 호출하고
//         // UserDetails를 리턴받아서 토큰의 두번째 파라메터(credential)과
//         // UserDetails(DB값)의 getPassword()함수로 비교해서 동일하면
//         // Authentication 객체를 만들어서 필터체인으로 리턴해준다.
//
//         // Tip: 인증 프로바이더의 디폴트 서비스는 UserDetailsService 타입
//         // Tip: 인증 프로바이더의 디폴트 암호화 방식은 BCryptPasswordEncoder
//         // 결론은 인증 프로바이더에게 알려줄 필요가 없음.
//         Authentication authentication =
//                 authenticationManager.authenticate(authenticationToken);
//
//         PrincipalDetails principalDetailis = (PrincipalDetails) authentication.getPrincipal();
//         System.out.println("Authentication : " + principalDetailis.getUser().getUsername());
//         return authentication;
//     }
//    // JWT Token 생성해서 response에 담아주기
//    // JWT Token 생성해서 response에 담아주기
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
//                                            Authentication authResult) throws IOException, ServletException {
//
//        PrincipalDetails principalDetailis = (PrincipalDetails) authResult.getPrincipal();
//
//        String jwtToken = JWT.create()
//                .withSubject(principalDetailis.getUsername())
//                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
//                .withClaim("id", principalDetailis.getUser().getId())
//                .withClaim("username", principalDetailis.getUser().getUsername())
//                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
//
//        System.out.println(jwtToken);
//
//        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);
//
//        // /login 마치고 나면, 토큰을 body로 보내줌
//        response.getWriter().write(String.valueOf(jwtToken));
//    }
//
//
//
//}
//

package com.twoper.toyou.global.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twoper.toyou.domain.user.model.dto.LoginRequestDto;
import com.twoper.toyou.global.jwt.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // 인증 요청 처리
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        System.out.println("JwtAuthenticationFilter : 진입");

        // request에 있는 username과 password를 파싱해서 자바 Object로 받기
        LoginRequestDto loginRequestDto = null;
        try {
            loginRequestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("JwtAuthenticationFilter : " + loginRequestDto);

        // 유저네임패스워드 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword());

        System.out.println("JwtAuthenticationFilter : 토큰생성완료");

        try {
            // authenticate() 함수가 호출되면 인증 프로바이더가 유저 디테일 서비스의
            // loadUserByUsername(토큰의 첫번째 파라메터)를 호출하고
            // UserDetails를 리턴받아서 토큰의 두번째 파라메터(credential)과
            // UserDetails(DB값)의 getPassword() 함수로 비교해서 동일하면
            // Authentication 객체를 만들어서 필터체인으로 리턴해준다.
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("Authentication : " + principalDetails.getUser().getUsername());
            return authentication;
        } catch (AuthenticationException e) {
            // 인증 실패 시 예외 처리
            System.out.println("Authentication failed: " + e.getMessage());
            throw e;
        }
    }

    // 인증 성공 시 JWT 토큰 생성하여 응답에 담기
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        System.out.println(jwtToken);

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);

        // /login 마치고 나면, 토큰을 body로 보내줌
        response.getWriter().write(String.valueOf(jwtToken));
    }

    // 인증 실패 시 추가 처리
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);

        // 인증 실패 시의 추가 로그
        System.out.println("Authentication failed: " + failed.getMessage());
    }
}
