package com.twoper.toyou.domain.user.Service;

import com.twoper.toyou.domain.user.model.User;
import com.twoper.toyou.domain.user.model.dto.FindAllUserResponse;
import com.twoper.toyou.domain.user.model.dto.RegisterDto;
import com.twoper.toyou.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User register(RegisterDto registerDto) {
        User user = new User();
        user.setName(registerDto.getName());
        user.setPassword(bCryptPasswordEncoder.encode(registerDto.getPassword()));
        user.setUsername(registerDto.getUsername());
        user.setRoles("ROLE_USER");
        return userRepository.save(user);
    }

    public ResponseEntity<FindAllUserResponse> findAll() {
        try {
            List<User> users = userRepository.findAll();
            return new ResponseEntity<>(
                    new FindAllUserResponse("true", "조회 성공", users),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    public User findUser(int id) {
        return userRepository.findById(id).orElseThrow(()-> {
            return new IllegalArgumentException("User ID를 찾을 수 없습니다.");
        });
    }
}