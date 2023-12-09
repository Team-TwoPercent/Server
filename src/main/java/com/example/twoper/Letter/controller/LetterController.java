package com.example.twoper.Letter.controller;

import com.example.twoper.Letter.model.Letter;
import com.example.twoper.Letter.model.dto.LetterDto;
import com.example.twoper.Letter.service.LetterService;

import com.twoper.toyou.domain.user.model.Response;
import com.twoper.toyou.domain.user.model.User;
import com.twoper.toyou.domain.user.repository.UserRepository;
import com.twoper.toyou.global.jwt.auth.PrincipalDetails;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LetterController {


  private final LetterService letterService;
    private final UserRepository userRepository;

    @ApiOperation(value = "편지 보내기", notes = "편지 보내기")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/letter/write")
    public Response<?> sendLetter(@RequestBody LetterDto letterDto, Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            // authentication이 null이거나 principal이 null인 경우에 대한 처리
            return new Response<>("에러", "인증되지 않은 사용자입니다.", null);
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();

        if (user == null) {
            // user가 null인 경우에 대한 처리
            return new Response<>("에러", "사용자 정보를 찾을 수 없습니다.", null);
        }

        letterDto.setSenderName(user.getName());

        return new Response<>("성공", "편지를 보냈습니다.", letterService.write(letterDto));
    }
//    @PostMapping("/letter")
//    public Response<?> sendLetter(@RequestBody LetterDto letterDto, Authentication authentication) {
//        // 임의로 유저 정보를 넣었지만, JWT 도입하고 현재 로그인 된 유저의 정보를 넘겨줘야함
//        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
//        User user = principalDetails.getUser();
//
//        letterDto.setSenderName(user.getName());
//
//        return new Response<>("성공", "편지를 보냈습니다.", letterService.write(letterDto));
//    }

    @ApiOperation(value = "받은 편지함 읽기", notes = "받은 편지함 확인")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/letter/received")
    public Response<?> getReceivedLetter(Authentication authentication) {
        // 임의로 유저 정보를 넣었지만, JWT 도입하고 현재 로그인 된 유저의 정보를 넘겨줘야함
//        User user = userRepository.findById(14).orElseThrow( ()-> {
//            return new IllegalArgumentException("유저를 찾을 수 없습니다.");
//        });

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();

        return new Response("성공", "받은 편지를 불러왔습니다.", letterService.receivedLetter(user));

    }

    @ApiOperation(value = "보낸 편지함 읽기", notes = "보낸편지함 확인")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/letter/sent")
    public Response<?> getSentLetter(Authentication authentication){
        // 임의로 유저 정보를 넣었지만, JWT 도입하고 현재 로그인 된 유저의 정보를 넘겨줘야함

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();

        return new Response<>("성공", "보낸 쪽지를 불러왔습니다.",letterService.sentLetter(user));
    }


}

