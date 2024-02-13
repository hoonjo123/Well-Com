package com.wellcom.domain.Member.Service;

import com.wellcom.domain.Member.Dto.MemberDetailResDto;
import com.wellcom.domain.Member.Dto.MemberListResDto;
import com.wellcom.domain.Member.Dto.MemberSignUpDto;
import com.wellcom.domain.Member.Dto.MemberUpdateReqDto;
import com.wellcom.domain.Member.Member;
import com.wellcom.domain.Member.Repository.MemberRepository;
import com.wellcom.domain.Member.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(MemberSignUpDto memberSignUpDto) throws Exception {

        if (memberRepository.findByEmail(memberSignUpDto.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        Member member = Member.builder()
                .email(memberSignUpDto.getEmail())
                .password(memberSignUpDto.getPassword())
                .nickname(memberSignUpDto.getNickname())
                .role(Role.USER)
                .build();

        member.passwordEncode(passwordEncoder);
        memberRepository.save(member);
    }
    public Member findById(Long id) throws EntityNotFoundException {
        Member member = memberRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("검색하신 ID의 Member가 없습니다."));
        return member;
    }

    public MemberDetailResDto findMemberDetail(Long id) throws EntityNotFoundException {
        Member member = this.findById(id);
        String role = null;
        if(member.getRole() == null || member.getRole().equals(Role.USER)){
            role = "일반유저";
        }else{
            role = "관리자";
        }
        MemberDetailResDto memberDetailResDto = new MemberDetailResDto();
        memberDetailResDto.setId(member.getId());
        memberDetailResDto.setNickName(member.getNickname());
        memberDetailResDto.setEmail(member.getEmail());
        memberDetailResDto.setPassword(member.getPassword());
        memberDetailResDto.setRole(role);
        memberDetailResDto.setCreatedTime(member.getCreatedTime());
        return memberDetailResDto;


    }

    public void update(Long id, MemberUpdateReqDto memberUpdateReqDto) throws EntityNotFoundException{
        Member member = this.findById(id);
        member.updateMember(memberUpdateReqDto.getNickName(), memberUpdateReqDto.getPassword());
        memberRepository.save(member);
    }
    public void delete(Long id) throws EntityNotFoundException {
        Member member = this.findById(id);
        memberRepository.delete(member);

    }

    public List<MemberListResDto> findAll(){
        List<Member> members = memberRepository.findAll();
        List<MemberListResDto> memberListResDtos = new ArrayList<>();
        for(Member member : members){
            MemberListResDto memberListResDto = new MemberListResDto();
            memberListResDto.setId(member.getId());
            memberListResDto.setNickName(member.getNickname());
            memberListResDto.setEmail(member.getEmail());
            memberListResDtos.add(memberListResDto);
        }
        return memberListResDtos;
    }

    public void blockMember(Long id) throws EntityNotFoundException {
        Member member = this.findById(id);
        member.setBlocked(true);
        memberRepository.save(member);
    }


    public void unblockMember(Long id) throws EntityNotFoundException {
        Member member = this.findById(id);
        member.setBlocked(false);
        memberRepository.save(member);
    }
}
