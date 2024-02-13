package com.wellcom.domain.SharingRoom.Dto;

import lombok.Data;
@Data
public class SharingRoomUpdateReqDto {

        private String title;
        private String contents;
        private int cntPeople;
        private String itemName;
        private String itemImagePath;
        // 필요한 경우 추가 필드를 정의할 수 있습니다.

}
