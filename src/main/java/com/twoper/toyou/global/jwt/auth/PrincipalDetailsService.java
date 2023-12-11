package com.twoper.toyou.global.jwt.auth;

import com.twoper.toyou.domain.user.model.User;
import com.twoper.toyou.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// http://localhost:8080/login => 여기서 동작을 안함 왜냐면 formLogin.disable() goqjfutj
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User userEntity = userRepository.findByUsername(username);


        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        System.out.println("Loaded UserDetails: " + userEntity);

        return new PrincipalDetails(userEntity);
    }
            }
