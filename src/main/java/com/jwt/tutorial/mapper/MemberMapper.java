package com.jwt.tutorial.mapper;

import com.jwt.tutorial.model.MemberDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    MemberDTO selectMemberDetail(String userId);

}
