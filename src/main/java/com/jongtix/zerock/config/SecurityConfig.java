package com.jongtix.zerock.config;

import com.jongtix.zerock.domain.user.Role;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Log4j2
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //BCryptPasswordEncoder: 'bcrypt'라는 해시 함수를 이용해서 패스워드를 암호화하는 목적으로 설계된 클래스
                                            //                       다시 원래대로 복호화가 불가능하고 매번 암호화된 값도 다름(길이는 동일)
                                            //                       특정한 문자열이 암호화된 결과인지만 확인 가능
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {  //AuthenticationManagerBuilder: 코드를 통해서 직접 인증 매니저를 설정할 때 사용
//        super.configure(auth);
        auth.inMemoryAuthentication()
                .withUser("user1")  //user
                .password("$2a$10$0SiKnOmL9EgKeHyAIXKFU.CjFrpJoeaPho/mTGzUbJD5dLnEvLOkS")   //user12!@에 대한 암호화값
//                .roles("USER"); //사용자 롤
                .roles(Role.USER.getKey());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {  //리소스 접근 제한
//        super.configure(http);
        http.authorizeRequests()
                .antMatchers("/sample/all").permitAll() //모든 사용자에게 허용
                .antMatchers("/sample/member").hasRole(Role.USER.getKey()); //USER 권한이 있는 사용자만 허용

        http
                .formLogin()   //인가/인증에 문제가 발생하면 로그인 화면으로
//                .loginPage()
//                .loginProcessingUrl()
//                .defaultSuccessUrl()
//                .failureUrl()
        ;

        http.csrf().disable();  //외부에서 REST 방식으로 이용할 수 있는 보안 설정을 다루기 위해 CSRF 토큰을 발행하지 않음

        http
                .logout()   //((주의)) csrf 토큰을 사용할 때는 반드시 POST 방식으로만 로그아웃을 처리해야 함, 그렇지 않으면 form 태그와 버튼으로 구성된 화면이 노출
//                .logoutUrl()
//                .logoutSuccessUrl()
//                .invalidateHttpSession()
//                .deleteCookies()
        ;
    }
}
