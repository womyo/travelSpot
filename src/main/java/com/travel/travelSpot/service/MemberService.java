package com.travel.travelSpot.service;


import com.travel.travelSpot.dto.LoginDto;
import com.travel.travelSpot.dto.SignUpDto;
import com.travel.travelSpot.dto.TokenDto;
import org.springframework.http.ResponseEntity;

public interface MemberService {
    public Long signUp(SignUpDto signUpDto);

    public TokenDto login(LoginDto loginDto);
}
