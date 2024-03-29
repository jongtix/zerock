package com.jongtix.zerock.service.security;

import com.jongtix.zerock.domain.user.ClubMember;
import com.jongtix.zerock.domain.user.ClubMemberRepository;
import com.jongtix.zerock.domain.user.Role;
import com.jongtix.zerock.dto.request.ClubAuthMemberRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClubOAuth2UserDetailsService extends DefaultOAuth2UserService {

    private final ClubMemberRepository repository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("------------------------------------");
        log.info("userRequest: " + userRequest);

        String clientName = userRequest.getClientRegistration().getClientName();

        log.info("clientName: " + clientName);
        log.info(userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("====================================");
        oAuth2User.getAttributes().forEach((key, value) -> {
            log.info("key: " + key + ", value: " + value);
        });

        String email = null;

        if ("Google".equals(clientName)) {
            email = oAuth2User.getAttribute("email");
        }

        log.info("EMAIL: " + email);

//        //AS-IS
//        ClubMember member = saveSocialMember(email);
//
//        return oAuth2User;

        //TO-BE
        ClubMember member = saveSocialMember(email);

        ClubAuthMemberRequestDto clubAuthMemberRequestDto = new ClubAuthMemberRequestDto(member.getEmail(), member.getPassword(), true, member.getRoleSet().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toList()));
        clubAuthMemberRequestDto.setName(member.getName());

        return clubAuthMemberRequestDto;
    }

//    //TO-BE
//    private ClubMember saveSocialMember(String email) {
//        ClubMember newMember = ClubMember.builder()
//                .email(email)
//                .name(email)
//                .password(passwordEncoder.encode("1111"))
//                .fromSocial(true)
//                .build();
//
//        newMember.addMemberRole(Role.USER);
//
//        return repository.findByEmail(email, true).orElse(newMember);
//    }

    //AS-IS
    private ClubMember saveSocialMember(String email) {
        //기존에 동일한 이메일로 가입한 회원이 있는 경우에는 그대로 조회만
        Optional<ClubMember> result = repository.findByEmail(email, true);

        if (result.isPresent()) {
            return result.get();
        }

        //없다면 회원 추가 패스워드는 1111 이름은 그냥 이메일 주소로
        ClubMember clubMember = ClubMember.builder()
                .email(email)
                .name(email)
                .password(passwordEncoder.encode("1111"))
                .fromSocial(true)
                .build();

        clubMember.addMemberRole(Role.USER);

        return repository.save(clubMember);
    }
}
