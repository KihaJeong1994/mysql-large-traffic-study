package com.example.fastcampusmysql.application.usecase;

import com.example.fastcampusmysql.domain.follow.service.FollowReadService;
import com.example.fastcampusmysql.domain.post.dto.PostCommand;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.service.PostWriteService;
import com.example.fastcampusmysql.domain.post.service.TimelineWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreatePostUsecase {

    private final PostWriteService postWriteService;
    private final FollowReadService followReadService;
    private final TimelineWriteService timelineWriteService;

//    @Transactional // Is it really necessary? Transaction is not silver bullet
    // if there is a lot of followers, must I send all followers the Post?
    public Long execute(PostCommand postCommand){
        var postId = postWriteService.create(postCommand);
        var followers = followReadService.getFollowers(postCommand.memberId());
        var followersId = followers.stream()
                .map(followDto -> followDto.fromMemberId())
                .toList();
        timelineWriteService.deliveryToTimeline(postId,followersId);
        return postId;
    }

}
