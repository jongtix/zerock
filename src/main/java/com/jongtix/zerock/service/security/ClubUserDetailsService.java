package com.jongtix.zerock.service.security;

import com.jongtix.zerock.domain.user.ClubMember;
import com.jongtix.zerock.domain.user.ClubMemberRepository;
import com.jongtix.zerock.dto.request.ClubAuthMemberRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClubUserDetailsService implements UserDetailsService {

    private final ClubMemberRepository clubMemberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("ClubUserDetailsService loadUserByUsername " + username);

        ClubMember clubMember = clubMemberRepository.findByEmail(username, false).orElseThrow(() -> new UsernameNotFoundException("해당하는 사용자가 없습니다. email: " + username));

        log.info("----------------------------------------");
        log.info(clubMember);

        ClubAuthMemberRequestDto clubAuthMember = new ClubAuthMemberRequestDto(
                clubMember.getEmail(),
                clubMember.getPassword(),
                clubMember.isFromSocial(),
                clubMember.getRoleSet().stream().map(
                        role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet())
        );

        clubAuthMember.setName(clubMember.getName());
        clubAuthMember.setFromSocial(clubMember.isFromSocial());

        return clubAuthMember;
    }

}
