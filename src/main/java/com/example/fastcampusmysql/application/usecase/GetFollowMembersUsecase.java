package com.example.fastcampusmysql.application.usecase;

import com.example.fastcampusmysql.domain.follow.dto.FollowDto;
import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.follow.service.FollowReadService;
import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import com.example.fastcampusmysql.domain.member.service.MemberReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetFollowMembersUsecase {
    private final FollowReadService followReadService;
    private final MemberReadService memberReadService;
    public List<MemberDto> execute(Long fromMemberId){
        /*
        * 1. FollowReadService.findAllByFromMemberId()
        * 2. for follow in follows, get member
        * 3. return List<MemberDto>
        * */
        var followings = followReadService.getFollowings(fromMemberId);
        var followingsMemberIds = followings.stream().map(follow -> follow.toMemberId()).toList();
        return memberReadService.getMembers(followingsMemberIds);
    }
}
