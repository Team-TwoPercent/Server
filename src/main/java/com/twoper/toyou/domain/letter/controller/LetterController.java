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

import java.util.List;

@RestController
@RequestMapping("/letter")
@RequiredArgsConstructor
public class LetterController {

    private final LetterService letterService;


    @PostMapping("/select_12")
    public ResponseEntity<?> selectZodiacSign(@RequestBody ZodiacSignSelectionDTO selectionDTO) {
        try {
            ZodiacSigne zodiacSign = ZodiacSigne.fromAnimal(selectionDTO.getZodiacSign());
            letterService.selectZodiacSign(selectionDTO.getUsername(), zodiacSign);
            return ResponseEntity.ok("동물 사인을 선택했습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/select_human")
    public ResponseEntity<?> selectRecipient(@RequestBody RecipientSelectionDTO selectionDTO) {
        try {
            letterService.selectRecipient(selectionDTO.getUsername(), selectionDTO.getReceiverId());
            return ResponseEntity.ok("받을 사람을 선택했습니다.");
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
    public Response<List<LetterDto>> getReceivedLetter(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();

        List<LetterDto> receivedLetters = letterService.receivedLetter(user);
        return new Response<>("성공", "받은 편지를 불러왔습니다.", receivedLetters);
    }

    @GetMapping("/received/{id}")
    public ResponseEntity<Response<LetterDto>> getReceivedLetterById(@PathVariable int id){
        try{
            LetterDto receivedLetter = letterService.findLetterById(id);
            Response<LetterDto> response = new Response<>("성공", "편지를 불러왔습니다.", receivedLetter);
            return ResponseEntity.ok(response);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>("실패", e.getMessage(), null));
        }
    }


    @GetMapping("/sent/{id}")
    public ResponseEntity<Response<LetterDto>> getSentLetterById(@PathVariable int id) {
        try {
            LetterDto sentLetter = letterService.findLetterById(id);
            Response<LetterDto> response = new Response<>("성공", "편지를 불러왔습니다.", sentLetter);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>("실패", e.getMessage(), null));
        }
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
    @ApiOperation(value = "편지 보내기", notes = "편지 보내기")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/write")
    public ResponseEntity<?> writeLetter(@RequestBody LetterDto letterDto, Authentication authentication) {
        try {
            User user = validateAndGetUser(authentication);

            letterDto.setUsername(user.getUsername());
            // 사용자가 선택한 동물 사인을 전달

            ZodiacSigne zodiacSigne = ZodiacSigne.fromAnimal(letterDto.getZodiacSign());

            LetterDto savedLetter = letterService.write(letterDto, zodiacSigne);

            return ResponseEntity.ok(savedLetter);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}









