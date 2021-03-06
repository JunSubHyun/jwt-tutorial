package com.jwt.tutorial.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.jwt.tutorial.auth.PrincipalDetails;
import com.jwt.tutorial.model.User;
import com.jwt.tutorial.repository.UserRepository;
import org.codehaus.groovy.syntax.TokenException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 시큐리티가 filter 가지고 있는데 그 필터 중에 BasicAuthenticationFilter 라는 것이 있음
// 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 되어이씀
// 만약에 권한이 인증이 필요한 주소가 아니라면 이 필터를 안탄다.
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {


    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }


    //인증이나 권한이 필요한 주소요청이 있을 때 해당 필터를 타게 됨.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        super.doFilterInternal(request, response, chain);
        System.out.println("인증이나 권한이 필요한 주소 요청이 됨.");



        try {
            String jwtHeader = request.getHeader("Authorization");
            System.out.println("jwtHeader : "+jwtHeader);

            //헤더가 있는지 확인
            if(jwtHeader == null || !jwtHeader.startsWith("Bearer")){
                //토큰이 없을때 또는 잘못된 시작
                chain.doFilter(request,response);
                return;
            }

            // JWT 토큰을 검증을 해서 정상적인 사용자인지 확인
            String jwtToken = request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PROFIX,"");
            String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(jwtToken).getClaim("username").asString();


            //추가 -> 토큰의 시간 만료 체크

            //서명이 정상적으로 됨
            if(username != null){
                User userEntity = userRepository.findByUsername(username);

                PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

                //JWT 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어준다.
                Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails,null,principalDetails.getAuthorities());

                //강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{
                //비정상적 서명
                System.out.println("비정상적 서명   : "+username);
            }
            System.out.println("정상!");
            chain.doFilter(request,response);


        }catch (IllegalArgumentException e){
            System.out.println("비정상적 에러::::");
        }catch (TokenExpiredException e){
            System.out.println("만료시간 에러::::"+e);
        }







    }
}
