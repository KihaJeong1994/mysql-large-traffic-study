package com.example.fastcampusmysql.domain.post.repository;

import com.example.fastcampusmysql.domain.post.entity.Timeline;
import com.example.fastcampusmysql.util.CursorRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TimelineRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final String TABLE = "Timeline";

    private static final RowMapper<Timeline> ROW_MAPPER = ((rs, rowNum) -> {
        return Timeline.builder()
                .id(rs.getLong("id"))
                .memberId(rs.getLong("memberId"))
                .postId(rs.getLong("postId"))
                .createdAt(rs.getObject("createdAt", LocalDateTime.class))
                .build();
    });

    public List<Timeline> findAllByMemberIdOrderByIdDesc(Long memberId, int size){
        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE memberId = :memberId
                ORDER BY id DESC
                LIMIT :size
                """, TABLE);
        var param = new MapSqlParameterSource()
                .addValue("memberId",memberId)
                .addValue("size",size);
        List<Timeline> timelines = namedParameterJdbcTemplate.query(sql,param, ROW_MAPPER);
        return timelines;
    }

    public List<Timeline> findAllByMemberIdAndIdLessThanOrderByIdDesc(Long memberId, Long id, int size){
        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE memberId = :memberId
                AND id < :id
                ORDER BY id DESC
                LIMIT :size
                """, TABLE);
        var param = new MapSqlParameterSource()
                .addValue("memberId",memberId)
                .addValue("id",id)
                .addValue("size",size);
        List<Timeline> timelines = namedParameterJdbcTemplate.query(sql,param, ROW_MAPPER);
        return timelines;
    }

    public Timeline save(Timeline timeline){
        if(timeline.getId()==null) return insert(timeline);
        throw new UnsupportedOperationException("Timeline does not support update");
    }

    public void bulkInsert(List<Timeline> timelines){
        String sql = String.format("""
                INSERT INTO %s (memberId, postId, createdAt)
                VALUES (:memberId, :postId, :createdAt)
                """, TABLE);
        var params = timelines.stream()
                        .map(BeanPropertySqlParameterSource::new)
                                .toArray(SqlParameterSource[]::new);
        namedParameterJdbcTemplate.batchUpdate(sql, params);
    }

    private Timeline insert(Timeline timeline) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");
        var params = new BeanPropertySqlParameterSource(timeline);
        long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return Timeline.builder()
                .id(id)
                .memberId(timeline.getMemberId())
                .postId(timeline.getPostId())
                .createdAt(timeline.getCreatedAt()
                )
                .build();
    }

    private Timeline update(Timeline timeline){
        return timeline;
    }
}
