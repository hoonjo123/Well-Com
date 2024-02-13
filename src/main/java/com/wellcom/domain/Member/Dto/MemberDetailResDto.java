package com.wellcom.domain.Member.Dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberDetailResDto {
    private Long id;
    private String nickName;
    private String email;
    private String password;
    private String role;
    private int counts;
    private LocalDateTime createdTime;
}
