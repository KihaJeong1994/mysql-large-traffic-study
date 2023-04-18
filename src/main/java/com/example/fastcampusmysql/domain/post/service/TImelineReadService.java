package com.example.fastcampusmysql.domain.post.service;

import com.example.fastcampusmysql.domain.post.entity.Timeline;
import com.example.fastcampusmysql.domain.post.repository.TimelineRepository;
import com.example.fastcampusmysql.util.CursorRequest;
import com.example.fastcampusmysql.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TImelineReadService {
    private final TimelineRepository timelineRepository;

    public PageCursor<Timeline> getTimelines(Long memberId, CursorRequest cursorRequest){
        var timelines = findAllBy(memberId, cursorRequest);
        var nextKey = timelines.size()!=0 ? timelines.get(timelines.size()-1).getId() : CursorRequest.NONE_KEY;
        return new PageCursor<>(cursorRequest.next(nextKey),timelines);
    }

    private List<Timeline> findAllBy(Long memberId, CursorRequest cursorRequest) {
        if(cursorRequest.hasKey()){
            return timelineRepository.findAllByMemberIdAndIdLessThanOrderByIdDesc(memberId, cursorRequest.key(), cursorRequest.size());
        }else {
            return timelineRepository.findAllByMemberIdOrderByIdDesc(memberId, cursorRequest.size());
        }
    }
}
