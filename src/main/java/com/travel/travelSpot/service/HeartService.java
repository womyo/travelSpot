package com.travel.travelSpot.service;

import com.travel.travelSpot.domain.Heart;
import com.travel.travelSpot.domain.Member;
import com.travel.travelSpot.domain.Spot;
import com.travel.travelSpot.dto.HeartDto;
import com.travel.travelSpot.repository.HeartRepository;
import com.travel.travelSpot.repository.MemberRepository;
import com.travel.travelSpot.repository.SpotRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;
    private final MemberRepository memberRepository;
    private final SpotRepository spotRepository;

    @Transactional
    public HeartDto addHeart(Long spotId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long memberId = Long.parseLong(authentication.getName());

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException(memberId + " -> 멤버 데이터베이스에서 찾을 수 없습니다."));
        Spot spot = spotRepository.findById(spotId).orElseThrow(() -> new EntityNotFoundException(spotId + " -> 장소 데이터베이스에서 찾을 수 없습니다."));

        Optional<Heart> existHeart = heartRepository.findByMemberAndSpot(member, spot);

        HeartDto heartResponseDto = new HeartDto();

        if (existHeart.isPresent()) {
            updateDeleted(existHeart.get());
            heartResponseDto.setResult(existHeart.get().isDeleted() ? "하트 취소" : "하트 성공");
        } else {
            heartRepository.save(Heart.builder().member(member).spot(spot).deleted(false).build());
            heartResponseDto.setResult("하트 성공");
        }

        return heartResponseDto;
    }

    @Transactional
    public void updateDeleted(Heart heart) {
        heart.updateDeleted(!heart.isDeleted());
    }
}
