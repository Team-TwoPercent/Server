package com.twoper.toyou.domain.user.controller;

import com.twoper.toyou.domain.user.Service.UserService;
import com.twoper.toyou.domain.user.model.dto.FindAllUserResponse;
import com.twoper.toyou.global.jwt.auth.PrincipalDetails;
import com.twoper.toyou.domain.user.model.Response;
import com.twoper.toyou.domain.user.model.User;
import com.twoper.toyou.domain.user.model.dto.RegisterDto;

import com.twoper.toyou.domain.user.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class  UserController {

    private final UserService userService;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/users")
    public ResponseEntity<FindAllUserResponse> findAll() {
        return userService.findAll();
    }

    @ApiOperation(value="유저 찾기", notes = "개별 유저 조회")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{id}")
    public Response<?> findUser(@PathVariable("id") Integer id) {
        return new Response<>("true", "조회 성공", userService.findUser(id));
    }

    @ApiOperation(value = "회원가입", notes="회원가입 진행")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/join")
    public Response<?> register(@RequestBody RegisterDto registerDto) {
        return new Response<>("true", "가입 성공", userService.register(registerDto));
    }


    // 모든 사람이 접근 가능
    @GetMapping("/home")
    public String home() {
        return "<h1>태관아 열심히 해</h1>";
    }

    // Tip : JWT를 사용하면 UserDetailsService를 호출하지 않기 때문에 @AuthenticationPrincipal 사용 불가능.
    // 왜냐하면 @AuthenticationPrincipal은 UserDetailsService에서 리턴될 때 만들어지기 때문이다.

}
