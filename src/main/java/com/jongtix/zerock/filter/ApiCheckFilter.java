package com.jongtix.zerock.filter;

import com.jongtix.zerock.utils.JWTUtil;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 가장 일반적이며, 매번 동작하는 기본적인 필터
 */
@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {

    private AntPathMatcher antPathMatcher;  //Ant Pattern에 맞는지 검사하는 유틸리티

    private String pattern;

    private JWTUtil jwtUtil;

    public ApiCheckFilter(String pattern) {
        this.antPathMatcher = new AntPathMatcher();
        this.pattern = pattern;
    }

    public ApiCheckFilter(String pattern, JWTUtil jwtUtil) {
        this.antPathMatcher = new AntPathMatcher();
        this.pattern = pattern;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("request url: " + request.getRequestURI());

        log.info(antPathMatcher.match(pattern, request.getRequestURI()));

        if (antPathMatcher.match(pattern, request.getRequestURI())) {
            log.info("ApiCheckFilter.........................");

            boolean checkHeader = checkAuthHeader(request);

            if (checkHeader) {  //인증에 성공했을 경우
                filterChain.doFilter(request, response);
            } else {    //인증에 실패했을 경우
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json;charset=utf-8");
                JSONObject json = new JSONObject();
                String message = "FAIL CHECK API TOKEN";
                json.put("code", "403");
                json.put("message", message);

                PrintWriter out = response.getWriter();
                out.print(json);
            }

            return;
        }

        filterChain.doFilter(request, response);    //다음 필터의 단계로 넘어가는 역할
    }

    private boolean checkAuthHeader(HttpServletRequest request) {
        boolean checkResult = false;

        String authHeader = request.getHeader("Authorization");

//        //JWT 인증 적용 전(AS-IS)
//        if (StringUtils.hasText(authHeader)) {
//            log.info("Authorization exist: " + authHeader);
//            if ("12345678".equals(authHeader)) {
//                checkResult = true;
//            }
//        }

        //JWT 인증 적용 후(TO-BE)
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            log.info("Authorization exist: " + authHeader);

            try {
                String email = jwtUtil.validateAndExtract(authHeader.substring(7));

                log.info("validate result: " + email);

                checkResult = email.length() > 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return checkResult;
    }
}
