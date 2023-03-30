package com.example.fastcampusmysql.application.controller;

import com.example.fastcampusmysql.application.usecase.CreateFollowMemberUsecase;
import com.example.fastcampusmysql.application.usecase.GetFollowMembersUsecase;
import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {
    private final CreateFollowMemberUsecase createFollowMemberUsecase;
    private final GetFollowMembersUsecase getFollowMembersUsecase;

    @PostMapping("/{fromMemberId}/{toMemberId}")
    public void create(@PathVariable Long fromMemberId, @PathVariable Long toMemberId){
        createFollowMemberUsecase.execute(fromMemberId,toMemberId);
    }

    @GetMapping("/members/{fromId}")
    public List<MemberDto> getFollowings(@PathVariable Long fromId){
        return getFollowMembersUsecase.execute(fromId);
    }
}
