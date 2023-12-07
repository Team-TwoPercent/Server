package com.example.twoper.Letter.service;

import com.example.twoper.Letter.model.Letter;
import com.example.twoper.Letter.model.dto.LetterDto;
import com.example.twoper.Letter.repository.LetterRepository;
import com.example.twoper.jwt.model.User;
import com.example.twoper.jwt.repoditory.UserRepository;
//import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import org.springframework.transaction.annotation.Transactional;

import javax.xml.ws.ServiceMode;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LetterService {

    private final LetterRepository letterRepository;
    private final UserRepository userRepository;

    @Transactional
    public LetterDto write(LetterDto letterDto) {
        User receiver = userRepository.findByName(letterDto.getReceiverName());
        User sender = userRepository.findByName(letterDto.getSenderName());

        Letter letter = new Letter();
        letter.setReceiver(receiver);
        letter.setSender(sender);

        letter.setTitle(letterDto.getTitle());
        letter.setContent(letterDto.getContent());
        letter.setDeletedByReceiver(false);
        letter.setDeletedBySender(false);
        letterRepository.save(letter);

        return LetterDto.toDto(letter);
    }

    @Transactional(readOnly = true)
    public LetterDto findLetterById(int id){
        Letter letter = letterRepository.findById(id).orElseThrow(()-> {
            return new IllegalArgumentException("편지를 찾을 수 없습니다.");
        });

        return LetterDto.toDto(letter);
    }
    @Transactional(readOnly = true)
    public List<LetterDto> receivedLetter(User user) {
        // 받은 편지함 불러오기
        // 한 명의 유저가 받은 모든 메시지
        // 추후 JWT를 이용해서 재구현 예정
        List<Letter> letters = letterRepository.findAllByReceiver(user);
        List<LetterDto> letterDtos = new ArrayList<>();

        for (Letter letter : letters) {
            // letter 에서 받은 편지함에서 삭제하지 않았으면 보낼 때 추가해서 보내줌
            if (!letter.isDeletedByReceiver()) {
                letterDtos.add(LetterDto.toDto(letter));
            }
        }
        return letterDtos;
    }

    // 받은 편지 삭제
    @Transactional
    public Object deleteLetterByReceiver(LetterDto letterDto, User user) {
        Letter letter = letterRepository.findById(letterDto.getId()).get();
        letter.deleteByReceiver();//받은 사람에게 편지 삭제
        if (letter.isDeleted()) {
            // 받은 사람과 보낸 사람 모두 삭제했으며, 데이터베이스 에서 삭제 요청
            letterRepository.delete(letter);
            return "양쪽 모두 삭제";
        }
        return "한쪽만 삭제";
    }

    @Transactional(readOnly = true)
    public List<LetterDto> sentLetter(User user) {
        // 보낸 편지함 불러오기
        // 한 명의 유저가 받은 모든 메시지
        // 추후 JWT를 이용해서 재구현 예정
        List<Letter> letters = letterRepository.findAllBySender(user);
        List<LetterDto> letterDtos = new ArrayList<>();

        for (Letter letter : letters) {
            // letter에서 받은 편지함에서 삭제하지 않았으면 보낼 때 추가해서 보내줌
            if (!letter.isDeletedBySender()) {
                letterDtos.add(LetterDto.toDto(letter));
            }
        }
        return letterDtos;
    }

}