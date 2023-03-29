package com.example.fastcampusmysql.domain.member.repository;

import com.example.fastcampusmysql.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final String TABLE = "Member";

    private final RowMapper<Member> rowMapper = (rs, rowNum)-> Member.builder()
            .id(rs.getLong("id"))
            .nickname(rs.getString("nickname"))
            .email(rs.getString("email"))
            .birthday(rs.getObject("birthday", LocalDate.class))
            .createdAt(rs.getObject("createdAt", LocalDateTime.class))
            .build();

    public Optional<Member> findById(Long id){
        /*
        * select *
        * from Member
        * where id = : id
        * */
        String sql = String.format("SELECT * FROM %s WHERE id = :id", TABLE);
        var param = new MapSqlParameterSource()
                .addValue("id",id);
        var member = namedParameterJdbcTemplate.queryForObject(sql, param, rowMapper);
        return Optional.ofNullable(member);
    }


    public Member save(Member member){
        /*
        * if id is null, insert
        * else update
        * return member with id
        * */
        if(member.getId()==null) return insert(member);
        return update(member);
    }



    private Member insert(Member member){
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");
        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        var id =  simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return Member.builder()
                .id(id)
                .nickname(member.getNickname())
                .email(member.getEmail())
                .birthday(member.getBirthday())
                .createdAt(member.getCreatedAt())
                .build();
    }

    private Member update(Member member){
        String sql = String.format("UPDATE %s SET email = :email, nickname =:nickname , birthday =:birthday WHERE id = :id", TABLE);
        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        namedParameterJdbcTemplate.update(sql,params);
        return member;
    }
}
