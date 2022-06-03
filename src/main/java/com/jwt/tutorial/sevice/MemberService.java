package com.jwt.tutorial.sevice;

import com.google.gson.JsonObject;

public interface MemberService {

    String loginMember(String userId, String password);

}
