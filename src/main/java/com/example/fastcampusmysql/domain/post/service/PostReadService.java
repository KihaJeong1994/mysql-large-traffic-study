package com.example.fastcampusmysql.domain.post.service;

import com.example.fastcampusmysql.domain.post.dto.DailyPostCount;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequest;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.entity.PostDto;
import com.example.fastcampusmysql.domain.post.repository.PostLikeRepository;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;
import com.example.fastcampusmysql.util.CursorRequest;
import com.example.fastcampusmysql.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostReadService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    public List<DailyPostCount> getDailyPostCount(DailyPostCountRequest request){
        /*
        * return List<createdDate, memberId, cnt>
        * SELECT memberId, createdDate as date, count(*) as postCount FROM Post
        * WHERE memberId = :memberId and createdDate between :firstDate and :lastDate
        * GROUP BY createdDate
        * ORDER BY createdDate
        * */
        return postRepository.groupByCreatedDate(request);
    }

    public Post getPost(Long postId){
        return postRepository.findById(postId).orElseThrow();
    }

    public Page<PostDto> getPosts(Long memberId, Pageable pageable){
        return postRepository.findAllByMemberId(memberId, pageable).map(this::toDto);
    }

    public PageCursor<Post> getPosts(Long memberId, CursorRequest cursorRequest){
        List<Post> posts = findAllBy(memberId, cursorRequest);
        var nextKey = getNextKey(posts);
        return new PageCursor<>(cursorRequest.next(nextKey),posts);
    }

    public PageCursor<Post> getPosts(List<Long> memberIds, CursorRequest cursorRequest){
        List<Post> posts = findAllBy(memberIds, cursorRequest);
        var nextKey = getNextKey(posts);
        return new PageCursor<>(cursorRequest.next(nextKey),posts);
    }

    public List<Post> getPosts(List<Long> ids){
        return postRepository.findAllByIdIn(ids);
    }



    /*
    * if cursorRequest has no key(when request for 1st time, so does not know info about key)
    * does not need to check key
    * else
    * check key by WHERE clause
    * */
    private List<Post> findAllBy(Long memberId, CursorRequest cursorRequest) {
        if(cursorRequest.hasKey()){
            return postRepository.findAllByIdLessThanAndMemberIdOrderByIdDesc(cursorRequest.key(), memberId, cursorRequest.size());
        }else {
            return postRepository.findAllByMemberIdOrderByIdDesc(memberId, cursorRequest.size());
        }
    }

    private List<Post> findAllBy(List<Long> memberIds, CursorRequest cursorRequest) {
        if(cursorRequest.hasKey()){
            return postRepository.findAllByIdLessThanAndMemberIdInOrderByIdDesc(cursorRequest.key(), memberIds, cursorRequest.size());
        }else {
            return postRepository.findAllByMemberIdInOrderByIdDesc(memberIds, cursorRequest.size());
        }
    }

    private static Long getNextKey(List<Post> posts) {
        return posts.size() != 0 ? posts.get(posts.size() - 1).getId() : CursorRequest.NONE_KEY;
    }

    private PostDto toDto(Post post){
        return new PostDto(
                post.getId(),
                post.getContents(),
                post.getCreatedAt(),
                postLikeRepository.countByPostId(post.getId())
        );
    }
}
