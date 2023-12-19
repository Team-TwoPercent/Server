package com.example.twoper.Letter.service;

import com.example.twoper.Letter.model.Letter;
import com.example.twoper.Letter.model.dto.LetterDto;
import com.example.twoper.Letter.repository.LetterRepository;
import com.twoper.toyou.domain.user.model.User;
import com.twoper.toyou.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LetterService {

    private final LetterRepository letterRepository;
    private final UserRepository userRepository;

    @Transactional
    public void selectZodiacSign(String username, String zodiacSign) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        user.setZodiacSign(zodiacSign);
        userRepository.save(user);
    }

    @Transactional
    public void selectRecipient(String senderName, Long receiverId) {
        User sender = userRepository.findByName(senderName);

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
        User sender = userRepository.findByName(letterDto.getSenderName());
        User receiver = userRepository.findByName(letterDto.getReceiverName());

        if (sender == null || receiver == null) {
            throw new IllegalArgumentException("보낸 사람 또는 받을 사람을 찾을 수 없습니다.");
        }

        Letter letter = new Letter();
        letter.setReceiver(receiver);
        letter.setSender(sender);
        letter.setZodiacSing(zodiacSign);

        letter.setTitle(letterDto.getTitle());
        letter.setContent(letterDto.getContent());
        letter.setDeletedByReceiver(false);
        letter.setDeletedBySender(false);
        letterRepository.save(letter);

        return LetterDto.toDto(letter);
    }

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
