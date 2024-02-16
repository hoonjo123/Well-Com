package com.wellcom.domain.Member.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MemberDetailResDto {
    private Long id;
    private String nickName;
    private String email;
    private String password;
    private String role;
    private LocalDateTime createdTime;

    //sharingRoom 횟수
    private int sharingRoomCount;

    //예약횟수
    private int reservationCount;

    //총 이용시간
    private int totalReservationTime;
}
