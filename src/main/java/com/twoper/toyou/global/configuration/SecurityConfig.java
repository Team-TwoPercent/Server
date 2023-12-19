//package com.twoper.toyou.global.configuration;
//import com.twoper.toyou.domain.user.repository.UserRepository;
//import com.twoper.toyou.global.jwt.JwtAuthenticationFilter;
//import com.twoper.toyou.global.jwt.JwtAuthorizationFilter;
//import com.twoper.toyou.global.jwt.auth.PrincipalDetailsService;
//import org.apache.catalina.filters.CorsFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// 401오류
//@Configuration
//@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
//public class SecurityConfig extends WebSecurityConfigurerAdapter{
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private CorsConfig corsConfig;
//    @Bean
//    public BCryptPasswordEncoder encoder() {
//        // DB 패스워드 암호화
//        return new BCryptPasswordEncoder();
//    }
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
////                .addFilter(corsConfig.corsFilter())
//                .cors().configurationSource(corsConfig.corsConfigurationSource()) // 수정된 부분
//                .and()
//                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and
//                .formLogin().disable()
//                .httpBasic().disable()
//                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
//                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))
//                .authorizeRequests()
//                .antMatchers("/join").permitAll()
//                .antMatchers("/login").permitAll()
//                .antMatchers("/users/**")
//                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
//                .antMatchers("/comments/**")
//                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
//                .anyRequest().permitAll();
//    }
//}
//package com.twoper.toyou.global.configuration;
//
//import com.twoper.toyou.domain.user.repository.UserRepository;
//import com.twoper.toyou.global.jwt.JwtAuthenticationFilter;
//import com.twoper.toyou.global.jwt.JwtAuthorizationFilter;
//import com.twoper.toyou.global.jwt.auth.JwtTokenProvider;
//import com.twoper.toyou.global.jwt.auth.PrincipalDetailsService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.filter.CorsFilter;
//
//@Configuration
//@RequiredArgsConstructor
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    private final UserRepository userRepository;
//    private final CorsFilter corsFilter;
//    private final JwtTokenProvider jwtTokenProvider;
//    @Bean
//    public BCryptPasswordEncoder encoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Autowired
//    public SecurityConfig(UserRepository userRepository, CorsFilter corsFilter, JwtTokenProvider jwtTokenProvider) {
//        this.userRepository = userRepository;
//        this.corsFilter = corsFilter;
//        this.jwtTokenProvider = jwtTokenProvider;
//    }
//
//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
//        return new JwtAuthenticationFilter(authenticationManager(), jwtTokenProvider);
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .addFilterBefore(corsFilter, JwtAuthenticationFilter.class)
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .formLogin().disable()
//                .httpBasic().disable()
//                .addFilter(jwtAuthenticationFilter())
//                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))
//                .authorizeRequests()
//                .antMatchers("/join", "/login", "/home").permitAll()
//                .anyRequest().authenticated();
//    }
//
//}
package com.twoper.toyou.global.configuration;
import com.twoper.toyou.domain.user.repository.UserRepository;
import com.twoper.toyou.global.jwt.JwtAuthenticationFilter;
import com.twoper.toyou.global.jwt.JwtAuthorizationFilter;
import com.twoper.toyou.global.jwt.auth.PrincipalDetailsService;
import org.apache.catalina.filters.CorsFilter;
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
@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CorsConfig corsConfig;


    @Bean
    public BCryptPasswordEncoder encoder() {
        // DB 패스워드 암호화
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .addFilter(corsConfig.corsFilter())
                .cors() // CORS 설정을 활성화
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))
                .authorizeRequests()
                .antMatchers("/join").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/users/**").permitAll()
//                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/comments/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();
    }
}
