package com.twoper.toyou.domain.letter.controller;

import com.twoper.toyou.domain.letter.ZodiacSigne;
import com.twoper.toyou.domain.letter.model.dto.LetterDto;
import com.twoper.toyou.domain.letter.model.dto.RecipientSelectionDTO;
import com.twoper.toyou.domain.letter.model.dto.ZodiacSignSelectionDTO;
import com.twoper.toyou.domain.letter.service.LetterService;

import com.twoper.toyou.domain.user.model.Response;
import com.twoper.toyou.domain.user.model.User;
import com.twoper.toyou.global.jwt.auth.PrincipalDetails;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/letter")
@RequiredArgsConstructor
public class LetterController {

  private final LetterService letterService;

    @PostMapping("/select_12")
    public ResponseEntity<?> selectZodiacSign(@RequestBody ZodiacSignSelectionDTO selectionDTO) {
            letterService.selectZodiacSign(selectionDTO.getUsername(), selectionDTO.getZodiacSign());
            return ResponseEntity.ok("12간지를 선택했습니다.");
    }

    @PostMapping("/select_human")
    public ResponseEntity<?> selectRecipient(@RequestBody RecipientSelectionDTO selectionDTO) {
            letterService.selectRecipient(selectionDTO.getSenderName(), selectionDTO.getReceiverId());
            return ResponseEntity.ok("받을 사람을 선택했습니다.");
    }





    @ApiOperation(value = "편지 보내기", notes = "편지 보내기")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/write")
    public ResponseEntity<?> writeLetter(@RequestBody LetterDto letterDto, Authentication authentication) {
        try {
            User user = validateAndGetUser(authentication);

            letterDto.setSenderName(user.getName());

            return ResponseEntity.ok("편지를 작성했습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private User validateAndGetUser(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalArgumentException("인증되지 않은 사용자입니다.");
        }

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();

        if (user == null) {
            throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다.");
        }

        return user;
    }
    @ApiOperation(value = "받은 편지함 읽기", notes = "받은 편지함 확인")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/received")
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
    @GetMapping("/sent")
    public Response<?> getSentLetter(Authentication authentication){
        // 임의로 유저 정보를 넣었지만, JWT 도입하고 현재 로그인 된 유저의 정보를 넘겨줘야함

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();

        return new Response<>("성공", "보낸 쪽지를 불러왔습니다.",letterService.sentLetter(user));
    }

    //    @ApiOperation(value = "편지 보내기", notes = "편지 보내기")
//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping("/write")
//    public Response<?> sendLetter(@RequestBody LetterDto letterDto, Authentication authentication) {
//        if (authentication == null || authentication.getPrincipal() == null) {
//            // authentication이 null이거나 principal이 null인 경우에 대한 처리
//            return new Response<>("에러", "인증되지 않은 사용자입니다.", null);
//        }
//        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
//        User user = principalDetails.getUser();
//        if (user == null) {
//            // user가 null인 경우에 대한 처리
//            return new Response<>("에러", "사용자 정보를 찾을 수 없습니다.", null);
//        }
//        letterDto.setSenderName(user.getName());
//        return new Response<>("성공", "편지를 보냈습니다.", letterService.write(letterDto));
//    }



}

