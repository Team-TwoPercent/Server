package com.twoper.toyou.global.configuration;
import com.twoper.toyou.domain.user.repository.UserRepository;
import com.twoper.toyou.global.jwt.JwtAuthenticationFilter;
import com.twoper.toyou.global.jwt.JwtAuthorizationFilter;
import com.twoper.toyou.global.jwt.auth.PrincipalDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
public class SecurityConfig{

    private final UserRepository userRepository;
    private final CorsFilter corsFilter;


    @Bean
    public BCryptPasswordEncoder encoder() {
        // DB 패스워드 암호화
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 설정 Disable
        http.csrf().disable()

                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

                // 시큐리티는 기본적으로 세션을 사용
                // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()

                // 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
                .authorizeHttpRequests()
                .anyRequest().authenticated() // 나머지 API 는 전부 인증 필요

                .antMatchers("/join").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/home").permitAll()
                .anyRequest().authenticated();

        return http.build();
    }

}


