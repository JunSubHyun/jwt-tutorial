package com.jwt.tutorial.config;

import com.jwt.tutorial.filter.MyFilter1;
import com.jwt.tutorial.filter.MyFilter2;
import com.jwt.tutorial.filter.MyFilter3;
import com.jwt.tutorial.jwt.JwtAuthenticationFilter;
import com.jwt.tutorial.jwt.JwtAuthorizationFilter;
import com.jwt.tutorial.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;
    private final UserRepository userRepository;

   @Override
   protected void configure(HttpSecurity http) throws Exception{

        //http.addFilterBefore(new MyFilter3(),BasicAuthenticationFilter.class);

        http.csrf().disable();

        //세션을 사용하지 않는다
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(corsFilter)                      //@CrossOrigin (인증 X), 시큐리티 필터에 등록 (인증 O)
                .formLogin().disable()
                .httpBasic().disable()                      //여기까지 고정
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))       //AuthenticationManger
                .addFilter(new JwtAuthorizationFilter(authenticationManager(),userRepository))       //AuthenticationManger
                .authorizeRequests()
                .antMatchers("/api/v1/user/**")
                .access("hasRole('ROLE_USER')or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();

   }

}
