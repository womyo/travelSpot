package com.travel.travelSpot.service.impl;

import com.travel.travelSpot.domain.Member;
import com.travel.travelSpot.domain.RefreshToken;
import com.travel.travelSpot.dto.LoginDto;
import com.travel.travelSpot.dto.SignUpDto;
import com.travel.travelSpot.dto.TokenDto;
import com.travel.travelSpot.jwt.TokenProvider;
import com.travel.travelSpot.repository.MemberRepository;
import com.travel.travelSpot.repository.RefreshTokenRepository;
import com.travel.travelSpot.service.MemberService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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
    private final RefreshTokenRepository refreshTokenRepository;
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
    public TokenDto login(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenDto tokenDto = tokenProvider.createToken(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
            .key(authentication.getName())
            .value(tokenDto.getRefreshToken())
            .build();

        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }
}
