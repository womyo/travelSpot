package com.travel.travelSpot.controller;


import com.travel.travelSpot.dto.LoginDto;
import com.travel.travelSpot.dto.SignUpDto;
import com.travel.travelSpot.dto.TokenDto;
import com.travel.travelSpot.dto.TokenRequestDto;
import com.travel.travelSpot.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Value("${jwt.refresh-token-expire-time}")
    private int refreshTokenExpireTime;

    @PostMapping("/api/v1/signup")
    @ResponseStatus(HttpStatus.OK)
    public Long signUp(@RequestBody @Valid SignUpDto signUpDto) throws Exception {
        return memberService.signUp(signUpDto);
    }

    @PostMapping("/api/v1/login")
    public TokenDto login(@RequestBody @Valid LoginDto loginDto,
        HttpServletResponse response) {
        TokenDto tokenDto = memberService.login(loginDto);

        createCookie(response, tokenDto.getRefreshToken(), "refresh", refreshTokenExpireTime);


        return tokenDto;
    }

    @PostMapping("/api/v1/logout")
    public void logout(@RequestBody @Valid TokenRequestDto tokenRequestDto,
        HttpServletResponse response) {
        memberService.logout(tokenRequestDto);

        deleteCookie(response, "refresh");
    }

    @PostMapping("/api/v1/reissue")
    public TokenDto reissue(@RequestBody @Valid TokenRequestDto tokenRequestDto,
        HttpServletResponse response) {
        TokenDto tokenDto = memberService.reissue(tokenRequestDto);

        createCookie(response, tokenDto.getRefreshToken(), "refresh", refreshTokenExpireTime);

        return tokenDto;
    }

    private void createCookie(HttpServletResponse response,
        String cookieItem,
        String cookieName,
        int expireTime) {
        Cookie cookie = new Cookie(cookieName, cookieItem);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(expireTime);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private void deleteCookie(HttpServletResponse response,
        String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @GetMapping("/api/v1/userHearts")
    public List<String> userHearts() {
        return memberService.getUserHeartedSpots();
    }

    @GetMapping("/test")
    public String test() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String memberName = auth.getName();
        return memberName;
    }
}
