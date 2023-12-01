package com.travel.travelSpot.service.impl;

import com.travel.travelSpot.domain.Member;
import com.travel.travelSpot.dto.LoginDto;
import com.travel.travelSpot.dto.SignUpDto;
import com.travel.travelSpot.dto.TokenDto;
import com.travel.travelSpot.jwt.JwtFilter;
import com.travel.travelSpot.jwt.TokenProvider;
import com.travel.travelSpot.repository.MemberRepository;
import com.travel.travelSpot.service.MemberService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public Long signUp(SignUpDto signUpDto) {

        Optional<Member> findMember = memberRepository.findByEmail(signUpDto.getEmail());
        if (findMember.isPresent()) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }

        Member member = memberRepository.save(signUpDto.toEntity());
        member.encodePassword(passwordEncoder);

        return member.getId();
    }

    @Transactional
    public ResponseEntity<TokenDto> login(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }
}
