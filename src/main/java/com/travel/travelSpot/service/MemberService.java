package com.travel.travelSpot.service;

import com.travel.travelSpot.domain.Member;
import com.travel.travelSpot.dto.LoginDto;
import com.travel.travelSpot.dto.SignUpDto;
import com.travel.travelSpot.dto.TokenDto;
import com.travel.travelSpot.dto.TokenRequestDto;
import com.travel.travelSpot.jwt.TokenProvider;
import com.travel.travelSpot.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final RedisTemplate<String, Object> redisTemplate;

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

        redisTemplate.opsForValue()
            .set("RT:" + authentication.getName(), tokenDto.getRefreshToken(),
                tokenDto.getRefreshTokenExpiresIn(), TimeUnit.MILLISECONDS);

        return tokenDto;
    }

    @Transactional
    public void logout(TokenRequestDto tokenRequestDto) {
        if (!tokenProvider.validateToken(tokenRequestDto.getAccessToken())) {
            throw new RuntimeException("Access Token이 유효하지 않습니다");
        }

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            redisTemplate.delete("RT:" + authentication.getName());
        }

        Long expiration = tokenProvider.getExpiration(tokenRequestDto.getAccessToken());

        redisTemplate.opsForValue()
            .set(tokenRequestDto.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token이 유효하지 않습니다");
        }

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        String refreshToken = (String) redisTemplate.opsForValue().get("RT:" + authentication.getName());

        if (ObjectUtils.isEmpty(refreshToken)) {
            throw new RuntimeException("로그아웃된 유저입니다");
        }

        if (!refreshToken.equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 정보가 일치하지 않습니다");
        }

        TokenDto tokenDto = tokenProvider.createToken(authentication);

        redisTemplate.opsForValue()
            .set("RT:" + authentication.getName(), tokenDto.getRefreshToken(),
                tokenDto.getRefreshTokenExpiresIn(), TimeUnit.MILLISECONDS);

        return tokenDto;
    }

    @Transactional
    public List<String> getUserHeartedSpots() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long memberId = Long.parseLong(authentication.getName());

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException(memberId + " -> 멤버 데이터베이스에서 찾을 수 없습니다."));
        List<String> getHeartedSpots = member.getHeartedSpots();

        return getHeartedSpots;
    }
}
