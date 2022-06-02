package com.jwt.tutorial.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwt.tutorial.auth.PrincipalDetails;
import com.jwt.tutorial.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;


//스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 가 있음
//login 요청해서 username, password 전송하면 (post)
// UsernamePasswordAuthenticationFilter 동작을 함.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    //login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter : 로그인 시도중");

        try {
//            BufferedReader br = request.getReader();
//
//            String input = null;
//            while ((input=br.readLine())!=null){
//                System.out.println(input);
//            }
            //jsonData 를 파싱해주는 class
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(),User.class);
            //user = User(id=0, username=ssar, password=1234, roles=null)
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken (user.getUsername(),user.getPassword());

            //PrincipalDetailsService의 loadUserByUsername() 함수가 실행됨
            //PrincipalDetailsSerivce의 loadUserByUsername() 함수가 실행된 후 정상이면 authentication 이 리턴됨
            //DB에 있는 username 과 password 가 일치한다.
            Authentication authentication = authenticationManager.authenticate(authenticationToken);            //로그인한 정보
            //authentication 객체가 session 영역에 저장됨 => 로그인이이
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println(principalDetails.getUser().getUsername());               //로그인 정상적으로 되었다는듯
            //authentication 객체가 session 영역에 저장을 해야하고 그 방법이 return 해주면됨
            //리턴의 이유는 권한 관리를 security 가 대신 해주기 때문에 편하려고 하는거임
            //굳이 JWT토큰을 사용하면서 세션을 만들이유가 없음. 근데 단지 권한 처리때문에 session에 넣어줍니다.
            return authentication;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //1.username, password 받아서

        //2. 정상인지 로그인 시도를 해본다. authenticationManager로 로그인 시도를 하면
        //   PrincipalDetailsService 가 호출 loadUserByUsername() 함수가 실행됨.
        
        //3. PrinciaplDetails 를 세션에 담고(권한 관리를 위해서)
        
        //4. JWT 토큰을 만들어서 응답을 해주면됨
//         return super.attemptAuthentication(request, response);
    }
    
    //RSA방식은 아니구 Hash 암호방식
    @Override
    protected void successfulAuthentication (HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)throws  IOException, ServletException{

        System.out.println("successfulAuthentication 실행이 됐다는건 로그인 성공");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        System.out.println("new Date(System.currentTimeMillis()+(JwtProperties.EXPIRATION_TIME)) :"+new Date(System.currentTimeMillis()+(JwtProperties.EXPIRATION_TIME)));
        String jwtToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+(JwtProperties.EXPIRATION_TIME)))
                .withClaim("id",principalDetails.getUser().getUsername())
                .withClaim("username",principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        response.addHeader(JwtProperties.HEADER_STRING,JwtProperties.TOKEN_PROFIX+jwtToken);

        String jwtTokenRefresh = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+(JwtProperties.EXPIRATION_TIME_REFRESH)))
                .withClaim("id",principalDetails.getUser().getUsername())
                .withClaim("username",principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET_REFRESH));

        response.addHeader(JwtProperties.HEADER_STRING_REFRESH,JwtProperties.TOKEN_PROFIX_REFRESH+jwtTokenRefresh);

    }
}
