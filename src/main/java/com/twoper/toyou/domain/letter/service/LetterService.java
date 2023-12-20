package com.twoper.toyou.domain.letter.service;

import com.twoper.toyou.domain.letter.ZodiacSigne;
import com.twoper.toyou.domain.letter.model.Letter;
import com.twoper.toyou.domain.letter.model.dto.LetterDto;
import com.twoper.toyou.domain.letter.repository.LetterRepository;
import com.twoper.toyou.domain.user.model.User;
import com.twoper.toyou.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class LetterService {

    private final LetterRepository letterRepository;
    private final UserRepository userRepository;

    @Transactional
    public void selectZodiacSign(String username, ZodiacSigne zodiacSign) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        user.setZodiacSign(zodiacSign.getAnimal());
        userRepository.save(user);
    }

    @Transactional
    public void selectRecipient(String username, Long receiverId) {
        User sender = userRepository.findByUsername(username);

        if (sender == null) {
            throw new IllegalArgumentException("보낸 사람을 찾을 수 없습니다.");
        }

        User receiver = userRepository.findById(receiverId.intValue()) // Convert Long to int
                .orElseThrow(() -> new IllegalArgumentException("받을 사람을 찾을 수 없습니다."));

        sender.setReceiver(receiver);
        userRepository.save(sender);
    }

    @Transactional
    public LetterDto write(LetterDto letterDto, String zodiacSign) {
        try {
            User sender = userRepository.findByUsername(letterDto.getUsername());
            User receiver = userRepository.getById(letterDto.getReceiverId());

            if (sender == null || receiver == null) {
                throw new IllegalArgumentException("보낸 사람 또는 받을 사람을 찾을 수 없습니다.");
            }

            // 문자열을 ZodiacSigne Enum 값으로 변환
            ZodiacSigne zodiacSigne = ZodiacSigne.fromAnimal(zodiacSign);

            Letter letter = new Letter();
            letter.setReceiver(receiver);
            letter.setSender(sender);
            letter.setZodiacSing(zodiacSigne.getAnimal());

            letter.setTitle(letterDto.getTitle());
            letter.setContent(letterDto.getContent());
            letter.setDeletedByReceiver(false);
            letter.setDeletedBySender(false);

            letterRepository.save(letter);

            return LetterDto.toDto(letter);
        } catch (Exception e) {
            // 예외 발생 시 로그 출력
            e.printStackTrace();
            throw new RuntimeException("편지 작성 중 오류 발생: " + e.getMessage(), e);
        }
    }



//@Transactional
//public LetterDto write(LetterDto letterDto, ZodiacSigne zodiacSign, Long receiverId) {
//    try {
//        User sender = userRepository.findByUsername(letterDto.getUsername());
//        User receiver = userRepository.getById(letterDto.getReceiverId());
//
//        if (sender == null || receiver == null) {
//            throw new IllegalArgumentException("보낸 사람 또는 받을 사람을 찾을 수 없습니다.");
//        }
//
//        Letter letter =Letter.builder()
//                .receiver(sender)
//                .sender(sender)
//                .zodiacSing(zodiacSign.getAnimal())
//                .title(letterDto.getTitle())
//                .content(letterDto.getContent())
//                .deletedBySender(false)
//                .deletedByReceiver(false)
//                .build();
//
////        Letter letter = new Letter();
////        letter.setReceiver(receiver);
////        letter.setSender(sender);
////        letter.setZodiacSing(zodiacSign.getAnimal());
////
////        letter.setTitle(letterDto.getTitle());
////        letter.setContent(letterDto.getContent());
////        letter.setDeletedByReceiver(false);
////        letter.setDeletedBySender(false);
//
//        letterRepository.save(letter);
//
//        return LetterDto.toDto(letter);
//    } catch (Exception e) {
//        // 예외 발생 시 로그 출력
//        log.error("편지 작성 중 오류 발생: {}", e.getMessage(), e);
//        throw new RuntimeException("편지 작성 중 오류 발생");
//    }
//    }




    @Transactional(readOnly = true)
    public LetterDto findLetterById(int id) {
        Letter letter = letterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("편지를 찾을 수 없습니다."));

        return LetterDto.toDto(letter);
    }

    @Transactional(readOnly = true)
    public List<LetterDto> receivedLetter(User user) {
        List<Letter> letters = letterRepository.findAllByReceiver(user);
        List<LetterDto> letterDtos = new ArrayList<>();

        for (Letter letter : letters) {
            if (!letter.isDeletedByReceiver()) {
                letterDtos.add(LetterDto.toDto(letter));
            }
        }
        return letterDtos;
    }

    @Transactional(readOnly = true)
    public List<LetterDto> sentLetter(User user) {
        List<Letter> letters = letterRepository.findAllBySender(user);
        List<LetterDto> letterDtos = new ArrayList<>();

        for (Letter letter : letters) {
            if (!letter.isDeletedBySender()) {
                letterDtos.add(LetterDto.toDto(letter));
            }
        }
        return letterDtos;
    }
}
