package com.example.fastcampusmysql.domain.post.repository;

import com.example.fastcampusmysql.domain.post.entity.PostLike;
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
public class PostLikeRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final String TABLE = "PostLike";

    private static final RowMapper<PostLike> ROW_MAPPER = ((rs, rowNum) -> {
        return PostLike.builder()
                .id(rs.getLong("id"))
                .memberId(rs.getLong("memberId"))
                .postId(rs.getLong("postId"))
                .createdAt(rs.getObject("createdAt", LocalDateTime.class))
                .build();
    });



    public PostLike save(PostLike PostLike){
        if(PostLike.getId()==null) return insert(PostLike);
        throw new UnsupportedOperationException("PostLike does not support update");
    }

    public void bulkInsert(List<PostLike> PostLikes){
        String sql = String.format("""
                INSERT INTO %s (memberId, postId, createdAt)
                VALUES (:memberId, :postId, :createdAt)
                """, TABLE);
        var params = PostLikes.stream()
                        .map(BeanPropertySqlParameterSource::new)
                                .toArray(SqlParameterSource[]::new);
        namedParameterJdbcTemplate.batchUpdate(sql, params);
    }

    private PostLike insert(PostLike PostLike) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");
        var params = new BeanPropertySqlParameterSource(PostLike);
        long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return PostLike.builder()
                .id(id)
                .memberId(PostLike.getMemberId())
                .postId(PostLike.getPostId())
                .createdAt(PostLike.getCreatedAt())
                .build();
    }

    private PostLike update(PostLike PostLike){
        return PostLike;
    }

    public Long countByPostId(Long postId){
        String countSql = String.format("""
                SELECT count(id) as cnt
                FROM %s
                WHERE postId = :postId
                """, TABLE);
        var param = new MapSqlParameterSource()
                .addValue("postId",postId);
        return namedParameterJdbcTemplate.queryForObject(countSql,param,Long.class);
    }
}
