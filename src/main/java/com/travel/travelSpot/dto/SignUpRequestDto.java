package com.travel.travelSpot.dto;

import com.travel.travelSpot.domain.Member;
import com.travel.travelSpot.domain.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequestDto {

    @NotBlank(message = "아이디를 입력해주세요")
    private String email;
    @NotBlank(message = "닉네임을 입력해주세요")
    private String username;
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    public Member toEntity() {
        return Member.builder()
            .email(email)
            .username(username)
            .password(password)
            .role(Role.USER)
            .build();
    }

    @Builder
    public SignUpRequestDto(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
