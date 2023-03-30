package com.example.fastcampusmysql.domain.follow.service;

import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.follow.repository.FollowRepository;
import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


@Service
@RequiredArgsConstructor
public class FollowWriteService {
    private final FollowRepository followRepository;
    public void create(MemberDto fromMember, MemberDto toMember){
        /*
        * @param from, to
        * from, to should be different
        * */
        Assert.isTrue(!fromMember.id().equals(toMember.id()),"from, to should be different");
        var follow = Follow.builder()
                .fromMemberId(fromMember.id())
                .toMemberId(toMember.id())
                .build();
        followRepository.save(follow);
    }
}
