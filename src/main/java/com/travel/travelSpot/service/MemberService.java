package com.travel.travelSpot.service;


import com.travel.travelSpot.dto.SignUpRequestDto;

public interface MemberService {
    public Long signUp(SignUpRequestDto signUpRequestDto);
}
