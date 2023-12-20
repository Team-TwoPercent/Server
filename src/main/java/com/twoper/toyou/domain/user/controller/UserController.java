package com.twoper.toyou.domain.user.controller;

import com.twoper.toyou.domain.user.Service.UserService;
import com.twoper.toyou.global.jwt.auth.PrincipalDetails;
import com.twoper.toyou.domain.user.model.Response;
import com.twoper.toyou.domain.user.model.User;
import com.twoper.toyou.domain.user.model.dto.RegisterDto;

import com.twoper.toyou.domain.user.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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



//    @ApiOperation(value = "전체 회원 보기", notes = "전체 회원을 조회한다.")
//    @ResponseStatus(HttpStatus.OK)
//    @GetMapping("/users")
//    public Response<?> findAll() {
//        return new Response<>("true", "조회 성공", userService.findAll());
//    }


    @GetMapping("/all")
    public ResponseEntity<Response<List<User>>> findAll() {
        try {
            List<User> users = userService.findAll();
            Response<List<User>> response = new Response<>("true", "조회 성공", users);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            // 예외 처리 로직 추가
            Response<List<User>> errorResponse = new Response<>("false", "조회 실패", null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    // 유저 혹은 매니저 혹은 어드민이 접근 가능
    @GetMapping("/user")
    public String user(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();

        return "<h1>" + principal.getUser().getUsername() + "</h1>";
    }

    // 매니저 혹은 어드민이 접근 가능
    @GetMapping("/manager")
    public String reports() {
        return "<h1>reports</h1>";
    }

    // 어드민이 접근 가능
    @GetMapping("/admin")
    public List<User> users(){
        return userRepository.findAll();
    }


}
