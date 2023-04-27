package com.example.fastcampusmysql.application.controller;

import com.example.fastcampusmysql.application.usecase.CreatePostLikeUsecase;
import com.example.fastcampusmysql.application.usecase.CreatePostUsecase;
import com.example.fastcampusmysql.application.usecase.GetTimelinePostUsecase;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCount;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequest;
import com.example.fastcampusmysql.domain.post.dto.PostCommand;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.entity.PostDto;
import com.example.fastcampusmysql.domain.post.service.PostReadService;
import com.example.fastcampusmysql.domain.post.service.PostWriteService;
import com.example.fastcampusmysql.util.CursorRequest;
import com.example.fastcampusmysql.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostWriteService postWriteService;
    private final PostReadService postReadService;
    private final GetTimelinePostUsecase getTimelinePostUsecase;
    private final CreatePostUsecase createPostUsecase;
    private final CreatePostLikeUsecase createPostLikeUsecase;

    @PostMapping("")
    public Long create(PostCommand command){
        return createPostUsecase.execute(command);
    }

    @GetMapping("/daily-post-counts")
    public List<DailyPostCount> getDailyPostCounts(DailyPostCountRequest request){
        return postReadService.getDailyPostCount(request);
    }

    @GetMapping("/members/{memberId}")
    public Page<PostDto> getPosts(
            @PathVariable Long memberId,
            Pageable pageable
    ){
        return postReadService.getPosts(memberId, pageable);
    }
    @GetMapping("/members/{memberId}/by-cursor")
    public PageCursor<Post> getPostsByCursor(
            @PathVariable Long memberId,
            CursorRequest cursorRequest
    ){
        return postReadService.getPosts(memberId, cursorRequest);
    }

    @GetMapping("/members/{memberId}/timeline")
    public PageCursor<Post> getTimeline(
            @PathVariable Long memberId,
            CursorRequest cursorRequest){
        return getTimelinePostUsecase.executeByTimeline(memberId,cursorRequest);
    }

    @PostMapping("/{postId}/like/v1")
    public void likePost(@PathVariable Long postId){
//        postWriteService.likePost(postId);
        postWriteService.likePostByOptimisticLock(postId);
    }

    @PostMapping("/{postId}/like/v2")
    public void likePostV2(@PathVariable Long postId, @RequestParam Long memberId){
        createPostLikeUsecase.execute(postId,memberId);
    }

}
