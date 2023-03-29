package com.example.fastcampusmysql.domain.member.repository;

import com.example.fastcampusmysql.domain.member.entity.MemberNicknameHistory;
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
public class MemberNicknameHistoryRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final String TABLE = "MemberNicknameHistory";

    private final RowMapper<MemberNicknameHistory> rowMapper = (rs, rowNum)-> MemberNicknameHistory.builder()
            .id(rs.getLong("id"))
            .memberId(rs.getLong("memberId"))
            .nickname(rs.getString("nickname"))
            .createdAt(rs.getObject("createdAt", LocalDateTime.class))
            .build();

    public List<MemberNicknameHistory> findAllByMemberId(Long memberId){
        String sql = String.format("SELECT * FROM %s WHERE memberId = :memberId", TABLE);
        SqlParameterSource params = new MapSqlParameterSource().addValue("memberId",memberId);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }



    public MemberNicknameHistory save(MemberNicknameHistory memberNicknameHistory){
        if(memberNicknameHistory.getId()==null) return insert(memberNicknameHistory);
        throw new UnsupportedOperationException("MemberNicknameHistoryRepository does not support update");
    }



    private MemberNicknameHistory insert(MemberNicknameHistory memberNicknameHistory){
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");
        SqlParameterSource params = new BeanPropertySqlParameterSource(memberNicknameHistory);
        var id =  simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return MemberNicknameHistory.builder()
                .id(id)
                .memberId(memberNicknameHistory.getMemberId())
                .nickname(memberNicknameHistory.getNickname())
                .createdAt(memberNicknameHistory.getCreatedAt())
                .build();
    }

}
