package com.jongtix.zerock.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 특정한 URL로 외부에서 로그인이 가능하도록 하고,
 * 로그인이 성공하면
 * 클라이언트가 Authorization 헤더의 값으로 이용할 데이터를 전송
 */
@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

    public ApiLoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("ApiLoginFilter.........................");
        log.info("attemptAuthentication");

        String email = request.getParameter("email");
//        String password = "password";
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        if (email == null) {
            throw new BadCredentialsException("email cannot be null");
        }

//        return null;
        return getAuthenticationManager().authenticate(authenticationToken);
    }

    /**
     * 인증 후 성공 or 실패에 따른 처리 방법 2
     * successfulAuthentication(unsuccessfulAuthentication)를 이용해서 인증에 성공(실패)했을 경우 처리를 지정할 수 있음
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("ApiLoginFilter.........................");
        log.info("successfulAuthentication: " + authResult);    //성공한 사용자의 인증 정보를 가지고 있는 Authentication 객체

        log.info(authResult.getPrincipal());
    }


}
