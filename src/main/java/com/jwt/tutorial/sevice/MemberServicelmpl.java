package com.jwt.tutorial.sevice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.jwt.tutorial.mapper.MemberMapper;
import com.jwt.tutorial.model.MemberDTO;
import com.jwt.tutorial.model.User;
import com.jwt.tutorial.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServicelmpl implements MemberService{

    private final MemberMapper memberMapper;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public String loginMember(String userId, String password) {
        String result = null;
        System.out.println("여기까지!");

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setUserId(userId);

        MemberDTO member = memberMapper.selectMemberDetail(userId);

        try {
            if(member!=null) {
                if(passwordEncoder.matches(password,member.getPassword())){
                    ObjectMapper mapper = new ObjectMapper();
                    result =  mapper.writeValueAsString(member);
                }
            }
        } catch (JsonProcessingException e) { e.printStackTrace(); }

        return result;

    }
}
