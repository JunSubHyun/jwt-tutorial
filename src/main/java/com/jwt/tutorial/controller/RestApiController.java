package com.jwt.tutorial.controller;

import com.google.gson.JsonObject;
import com.jwt.tutorial.model.MemberDTO;
import com.jwt.tutorial.model.User;
import com.jwt.tutorial.repository.UserRepository;
import com.jwt.tutorial.sevice.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Member;

@RestController
@RequiredArgsConstructor
public class RestApiController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final MemberService memberService;

    @GetMapping("home")
    public String home(){
        return "<h1>home</h1>";
    }

    @PostMapping("token")
    public String token(){
        return "<h1>token</h1>";
    }

    @PostMapping("join")
    public String join(@RequestBody User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword())); //1234ppp -> ABC33333
        user.setRoles("ROLE_USER");
        userRepository.save(user);
        return "회원가입완료";
    }

    //user,manager,admin 접근가능
    @GetMapping("/api/v1/user")
    public String user(@RequestBody User user){
        return user.getUsername();
    }

    //manager,admin 접근가능
    @GetMapping("/api/v1/manager")
    public String manager(){
        return "manager";
    }

    //admin 접근가능
    @GetMapping("/api/v1/admin")
    public String admin(){
        return "admin";
    }

    @PostMapping("/test/login")
    public String  loginApi(@RequestBody MemberDTO member){
        System.out.println("userId   : "+member.getUserId());
        String result = memberService.loginMember(member.getUserId(),member.getPassword());
        return result;
    }
}
