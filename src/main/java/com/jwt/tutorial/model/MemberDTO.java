package com.jwt.tutorial.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDTO {
    private long id;
    private String userId;
    private String password;
    private String empNo;
}
