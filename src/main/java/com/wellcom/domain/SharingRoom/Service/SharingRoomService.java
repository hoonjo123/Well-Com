package com.wellcom.domain.SharingRoom.Service;

import com.wellcom.domain.Item.Item;
import com.wellcom.domain.Item.ItemStatus;
import com.wellcom.domain.Member.Member;
import com.wellcom.domain.Member.Repository.MemberRepository;
import com.wellcom.domain.SharingRoom.Dto.SharingRoomCreateReqDto;
import com.wellcom.domain.SharingRoom.Dto.SharingRoomCreateResDto;
import com.wellcom.domain.SharingRoom.Dto.SharingRoomUpdateReqDto;
import com.wellcom.domain.SharingRoom.Repository.SharingRoomRepository;
import com.wellcom.domain.SharingRoom.SharingRoom;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SharingRoomService {
    private final SharingRoomRepository sharingRoomRepository;
    private final MemberRepository memberRepository;

    public SharingRoom create(SharingRoomCreateReqDto sharingRoomCreateReqDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Member member = memberRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("not found email"));

        if(member.isBlocked()){
            throw new IllegalStateException("차단된 회원은 이용할 수 없습니다.");
        }

        Item item = Item.builder()
                .name(sharingRoomCreateReqDto.getItemName())
                .imagePath(sharingRoomCreateReqDto.getItemImagePath())
                .itemStatus(ItemStatus.SHARING)
                .build();

        // SharingRoom 객체가 생성될 때 Item 객체도 함께 생성 : Cascading PERSIST
        SharingRoom sharingRoom = SharingRoom.builder()
                .member(member)
                .title(sharingRoomCreateReqDto.getTitle())
                .contents(sharingRoomCreateReqDto.getContents())
                .cntPeople(sharingRoomCreateReqDto.getCntPeople())
                .item(item).build();

        return sharingRoomRepository.save(sharingRoom);
    }

    public List<SharingRoomCreateResDto> findAll() {
        List<SharingRoom> sharingRooms = sharingRoomRepository.findAll();
        return sharingRooms.stream().map(o -> SharingRoomCreateResDto.toDto(o)).collect(Collectors.toList());
    }

    //findAll : delYn이 N인 SharingRoom만 찾기

    public void delete(Long id) {
        SharingRoom sharingRoom = sharingRoomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SharingRoom not found with id: " + id));
        sharingRoomRepository.delete(sharingRoom);
    }

    public SharingRoom update(Long id, SharingRoomUpdateReqDto updateReqDto) {
        SharingRoom sharingRoom = sharingRoomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SharingRoom not found with id: " + id));

        Item item = sharingRoom.getItem();
        item.setName(updateReqDto.getItemName());
        item.setImagePath(updateReqDto.getItemImagePath());

        sharingRoom.setTitle(updateReqDto.getTitle());
        sharingRoom.setContents(updateReqDto.getContents());
        sharingRoom.setCntPeople(updateReqDto.getCntPeople());
        // Item 객체를 업데이트한 후 다시 설정
        sharingRoom.setItem(item);

        return sharingRoomRepository.save(sharingRoom);
    }


}