package com.example.fastcampusmysql.domain.post.service;

import com.example.fastcampusmysql.domain.post.dto.DailyPostCount;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequest;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostReadService {
    private final PostRepository postRepository;
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

    public Page<Post> getPosts(Long memberId, Pageable pageRequest){
        return postRepository.findAllByMemberId(memberId,pageRequest);
    }
}
