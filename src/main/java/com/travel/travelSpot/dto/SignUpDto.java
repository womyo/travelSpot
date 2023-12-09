package com.travel.travelSpot.dto;

import com.travel.travelSpot.domain.Member;
import com.travel.travelSpot.domain.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {

    @NotBlank(message = "아이디를 입력해주세요")
    @Size(min = 3, max = 50)
    private String email;
    @NotBlank(message = "닉네임을 입력해주세요")
    @Size(min = 3, max = 50)
    private String username;
    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(min = 3, max = 100)
    private String password;

    public Member toEntity() {
        return Member.builder()
            .email(email)
            .username(username)
            .password(password)
            .role(Role.USER)
            .build();
    }
}
