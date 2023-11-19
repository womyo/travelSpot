package com.travel.travelSpot.service.impl;

import com.travel.travelSpot.domain.Member;
import com.travel.travelSpot.dto.SignUpRequestDto;
import com.travel.travelSpot.repository.MemberRepository;
import com.travel.travelSpot.service.MemberService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long signUp(SignUpRequestDto signUpRequestDto) {

        Optional<Member> findMember = memberRepository.findByEmail(signUpRequestDto.getEmail());
        if (findMember.isPresent()) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }

        Member member = memberRepository.save(signUpRequestDto.toEntity());

        return member.getId();
    }
}
