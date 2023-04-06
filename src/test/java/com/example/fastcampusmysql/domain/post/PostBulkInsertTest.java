package com.example.fastcampusmysql.domain.post;

import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;
import com.example.fastcampusmysql.util.PostFixtureFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.time.LocalDate;
import java.util.stream.IntStream;

@SpringBootTest // to inject spring bean
//@Transactional // roll back after test
public class PostBulkInsertTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    public void bulkInsert(){
        var easyRandom = PostFixtureFactory.get(1L, LocalDate.of(1970,1,1),LocalDate.now());

        var stopWatch = new StopWatch();
        stopWatch.start();

        int _million = 10000*100;
        var posts = IntStream.range(0,2*_million)
                .parallel()
                .mapToObj(i->easyRandom.nextObject(Post.class))
                .toList();
        stopWatch.stop();
        System.out.println("instance create time "+stopWatch.getTotalTimeSeconds());

        var queryStopWatch = new StopWatch();
        queryStopWatch.start();
        postRepository.bulkInsert(posts);
        queryStopWatch.stop();
        System.out.println("DB insert time "+queryStopWatch.getTotalTimeSeconds());
    }
}
