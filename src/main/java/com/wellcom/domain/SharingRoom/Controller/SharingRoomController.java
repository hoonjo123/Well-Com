package com.wellcom.domain.SharingRoom.Controller;

import com.wellcom.domain.SharingRoom.Dto.SharingRoomCreateReqDto;
import com.wellcom.domain.SharingRoom.Dto.SharingRoomCreateResDto;
import com.wellcom.domain.SharingRoom.Dto.SharingRoomUpdateReqDto;
import com.wellcom.domain.SharingRoom.Service.SharingRoomService;
import com.wellcom.domain.SharingRoom.SharingRoom;
import com.wellcom.domain.common.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.List;

@RestController
public class SharingRoomController {
    private final SharingRoomService sharingRoomService;
    @Autowired
    public SharingRoomController(SharingRoomService sharingRoomService) {
        this.sharingRoomService = sharingRoomService;
    }

    @PostMapping("/room/create")
    public ResponseEntity<CommonResponse> roomCreate(@RequestBody SharingRoomCreateReqDto sharingRoomCreateReqDto){
        SharingRoom sharingRoom = sharingRoomService.create(sharingRoomCreateReqDto);
        return new ResponseEntity<>(new CommonResponse(HttpStatus.CREATED, "sharingRoom is successfully created", sharingRoom.getId()), HttpStatus.CREATED);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/rooms")
    public List<SharingRoomCreateResDto> orderList(){
        return sharingRoomService.findAll();
    }

    @DeleteMapping("/room/{id}/delte")
    public ResponseEntity<CommonResponse> deleteRoom(@PathVariable Long id) {
        try {
            sharingRoomService.delete(id);
            return ResponseEntity.ok(new CommonResponse(HttpStatus.OK, "Room successfully deleted", null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(HttpStatus.NOT_FOUND, e.getMessage(), null));
        }
    }
    @PutMapping("/room/{id}/update")
    public ResponseEntity<CommonResponse> updateRoom(
            @PathVariable Long id,
            @RequestBody SharingRoomUpdateReqDto updateReqDto) {
        try {
            SharingRoom updatedRoom = sharingRoomService.update(id, updateReqDto);
            return ResponseEntity.ok(new CommonResponse(HttpStatus.OK, "성공적으로 업데이트 되었습니다.", updatedRoom.getId()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CommonResponse(HttpStatus.NOT_FOUND, e.getMessage(), null));
        }
    }


}
