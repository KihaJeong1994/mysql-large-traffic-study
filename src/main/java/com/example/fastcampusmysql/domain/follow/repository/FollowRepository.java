package com.example.fastcampusmysql.domain.follow.repository;

import com.example.fastcampusmysql.domain.follow.entity.Follow;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
public class FollowRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final String TABLE = "Follow";
    private static final RowMapper<Follow> rowMapper = (rs, rowNum) ->{
        return Follow.builder()
                .id(rs.getLong("id"))
                .fromMemberId(rs.getLong("fromMemberId"))
                .toMemberId(rs.getLong("toMemberId"))
                .createdAt(rs.getObject("createdAt", LocalDateTime.class))
                .build();
    };

    public List<Follow> findAllByFromMemberId(Long fromMemberId){
        String sql = String.format("SELECT * FROM %s WHERE fromMemberId = :fromMemberId",TABLE);
        SqlParameterSource param = new MapSqlParameterSource().addValue("fromMemberId",fromMemberId);
        return namedParameterJdbcTemplate.query(sql, param, rowMapper);
    }

    public List<Follow> findAllByToMemberId(Long toMemberId){
        String sql = String.format("SELECT * FROM %s WHERE toMemberId = :toMemberId",TABLE);
        SqlParameterSource param = new MapSqlParameterSource().addValue("toMemberId",toMemberId);
        return namedParameterJdbcTemplate.query(sql, param, rowMapper);
    }

    public Follow save(Follow follow){
        if(follow.getId()==null) return insert(follow);
        return update(follow);
    }

    private Follow insert(Follow follow) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(follow);
        var id = simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
        return Follow.builder()
                .id(id)
                .fromMemberId(follow.getFromMemberId())
                .toMemberId(follow.getToMemberId())
                .createdAt(follow.getCreatedAt())
                .build();
    }

    private Follow update(Follow follow){
        return follow;
    }
}
