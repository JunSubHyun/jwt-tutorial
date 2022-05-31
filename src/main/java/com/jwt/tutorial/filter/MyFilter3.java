package com.jwt.tutorial.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter3 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        //토큰 : 코스(예시)
        //id,pw 정상적으로 들어와서 로그인이 완료되면 토큰을 만들어주고 그걸 응답해준다.
        //요청할 때 마다 header에 Authorization에 value 값으로 토큰을 가져오고
        //그때 토큰이 넘어오면 토큰이 내가 만든 토큰이 맞는지만 검증만 하면됨
        if(req.getMethod().equals("POST")){
            String headerAuth = req.getHeader("Authorization");
            System.out.println("headAuth  :" + headerAuth  );

            if(headerAuth.equals("cos")){
                chain.doFilter(req,res);
            }else{
                PrintWriter out = res.getWriter();
                out.println("인증안됨");
            }

        }



        System.out.println("=========필터1=========");

    }
}
