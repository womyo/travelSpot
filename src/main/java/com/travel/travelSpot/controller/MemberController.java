package com.travel.travelSpot.controller;


import com.travel.travelSpot.domain.Heart;
import com.travel.travelSpot.dto.LoginDto;
import com.travel.travelSpot.dto.SignUpDto;
import com.travel.travelSpot.dto.TokenDto;
import com.travel.travelSpot.jwt.TokenProvider;
import com.travel.travelSpot.service.MemberService;
import com.travel.travelSpot.service.SpotService;
import com.travel.travelSpot.service.impl.MemberServiceImpl;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final MemberServiceImpl memberServiceImpl;

    @PostMapping("/api/v1/signup")
    @ResponseStatus(HttpStatus.OK)
    public Long signUp(@RequestBody @Valid SignUpDto signUpDto) throws Exception {
        return memberService.signUp(signUpDto);
    }

    @PostMapping("/api/v1/login")
    public TokenDto login(@RequestBody @Valid LoginDto loginDto) {
        return memberService.login(loginDto);
    }

    @GetMapping("/api/v1/userHearts")
    public List<String> userHearts(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return memberServiceImpl.getUserHeartedSpots(userId);
    }
    @GetMapping("/test")
    public String test() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String memberName = auth.getName();
        return memberName;
    }
}
