package com.jongtix.zerock.handler;

import com.jongtix.zerock.dto.request.ClubAuthMemberRequestDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class ClubLoginSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private PasswordEncoder passwordEncoder;

    public ClubLoginSuccessHandler(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("---------------------------");
        log.info("onAuthenticationSuccess");

        ClubAuthMemberRequestDto clubAuthMemberRequestDto = (ClubAuthMemberRequestDto) authentication.getPrincipal();

        boolean fromSocial = clubAuthMemberRequestDto.isFromSocial();

        log.info("Need Modify Member Info? " + fromSocial);

        boolean passwordResult = passwordEncoder.matches("1111", clubAuthMemberRequestDto.getPassword());

        if (fromSocial && passwordResult) {
            redirectStrategy.sendRedirect(request, response, "/member/modify?from=social");
        }
    }

}
