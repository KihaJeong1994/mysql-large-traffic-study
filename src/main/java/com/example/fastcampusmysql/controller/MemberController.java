package com.example.fastcampusmysql.controller;

import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import com.example.fastcampusmysql.domain.member.dto.RegisterMemberCommand;
import com.example.fastcampusmysql.domain.member.service.MemberReadService;
import com.example.fastcampusmysql.domain.member.service.MemberWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberReadService memberReadService;
    private final MemberWriteService memberWriteService;

    @GetMapping("/members/{id}")
    public MemberDto getMember(@PathVariable Long id){
        return memberReadService.getMember(id);
    }

    @PostMapping("/members")
    public MemberDto register(@RequestBody RegisterMemberCommand command){
        return memberReadService.toDto(memberWriteService.register(command));
    }


}
