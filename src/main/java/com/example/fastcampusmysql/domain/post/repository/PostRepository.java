package com.example.fastcampusmysql.domain.post.repository;

import com.example.fastcampusmysql.util.PageHelper;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCount;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequest;
import com.example.fastcampusmysql.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final String TABLE = "Post";

    private static final RowMapper<Post> ROW_MAPPER = ((rs, rowNum) -> {
       return Post.builder()
               .id(rs.getLong("id"))
               .memberId(rs.getLong("memberId"))
               .contents(rs.getString("contents"))
               .createdDate(rs.getObject("createdDate", LocalDate.class))
               .likeCount(rs.getLong("likeCount"))
               .version(rs.getLong("version"))
               .createdAt(rs.getObject("createdAt", LocalDateTime.class))
               .build();
    });
    private static final RowMapper<DailyPostCount> DAILY_POST_COUNT_ROW_MAPPER = ((rs, rowNum) -> {
       return new DailyPostCount(
               rs.getLong("memberId"),
               rs.getObject("createdDate", LocalDate.class),
               rs.getLong("count")
       );
    });

    public List<DailyPostCount> groupByCreatedDate(DailyPostCountRequest request){
        var sql = String.format("""
                SELECT memberId, createdDate, count(id) as count
                FROM %s
                WHERE memberId = :memberId and createdDate between :firstDate and :lastDate
                GROUP BY memberId, createdDate 
                """, TABLE);
        var params = new BeanPropertySqlParameterSource(request);
        return namedParameterJdbcTemplate.query(sql,params,DAILY_POST_COUNT_ROW_MAPPER);
    }

    public List<Post> findAllByIdIn(List<Long> ids){
        if(ids.isEmpty()) return List.of();
        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE id IN (:ids)
                """, TABLE);
        var params = new MapSqlParameterSource()
                .addValue("ids",ids);
        return namedParameterJdbcTemplate.query(sql,params,ROW_MAPPER);
    }

    public Page<Post> findAllByMemberId(Long memberId, Pageable pageable){
        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE memberId = :memberId
                ORDER BY %s
                LIMIT :size
                OFFSET :offset
                """, TABLE, PageHelper.orderBy(pageable.getSort()));
        var params = new MapSqlParameterSource()
                .addValue("memberId",memberId)
                .addValue("size",pageable.getPageSize())
                .addValue("offset",pageable.getOffset());
        var posts = namedParameterJdbcTemplate.query(sql,params,ROW_MAPPER);

        return new PageImpl<>(posts, pageable,getCount(memberId));
    }

    public Optional<Post> findById(Long id){
        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE id = :id
                """, TABLE);
        var param = new MapSqlParameterSource()
                .addValue("id",id);
        Post post = namedParameterJdbcTemplate.queryForObject(sql, param, ROW_MAPPER);
        return Optional.ofNullable(post);
    }

    // There is @Lock(LockModeType.PESSIMISTIC_WRITE) annotation in JPA to use SELECT FOR UPDATE
    public Optional<Post> findById(Long id, boolean requiredLock){
        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE id = :id
                """, TABLE);
        if(requiredLock) sql += " FOR UPDATE";
        var param = new MapSqlParameterSource()
                .addValue("id",id);
        Post post = namedParameterJdbcTemplate.queryForObject(sql, param, ROW_MAPPER);
        return Optional.ofNullable(post);
    }

    private Long getCount(Long memberId){
        String countSql = String.format("""
                SELECT count(id) as cnt
                FROM %s
                WHERE memberId = :memberId
                """, TABLE);
        var param = new MapSqlParameterSource()
                .addValue("memberId",memberId);
        return namedParameterJdbcTemplate.queryForObject(countSql,param,Long.class);
    }

    public List<Post> findAllByMemberIdOrderByIdDesc(Long memberId, int size){
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
        return namedParameterJdbcTemplate.query(sql,param,ROW_MAPPER);
    }

    public List<Post> findAllByMemberIdInOrderByIdDesc(List<Long> memberIds, int size){
        if(memberIds.isEmpty()) return List.of();
        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE memberId in (:memberIds)
                ORDER BY id DESC
                LIMIT :size
                """, TABLE);
        var param = new MapSqlParameterSource()
                .addValue("memberIds",memberIds)
                .addValue("size",size);
        return namedParameterJdbcTemplate.query(sql,param,ROW_MAPPER);
    }

    public List<Post> findAllByIdLessThanAndMemberIdOrderByIdDesc(Long id, Long memberId, int size){
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
                .addValue("size",size)
                .addValue("id",id);
        return namedParameterJdbcTemplate.query(sql,param,ROW_MAPPER);
    }

    public List<Post> findAllByIdLessThanAndMemberIdInOrderByIdDesc(Long id, List<Long> memberIds, int size){
        if(memberIds.isEmpty()) return List.of();
        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE memberId in (:memberIds)
                AND id < :id
                ORDER BY id DESC
                LIMIT :size
                """, TABLE);
        var param = new MapSqlParameterSource()
                .addValue("memberIds",memberIds)
                .addValue("size",size)
                .addValue("id",id);
        return namedParameterJdbcTemplate.query(sql,param,ROW_MAPPER);
    }


    public Post save(Post post){
        /*
         * if id is null, insert
         * else update
         * return member with id
         * */
        if(post.getId()==null) return insert(post);
        return update(post);
    }



    /*
    * While using Spring Data JPA,
    * jdbcTemplate is still used a lot when PK is auto increment
    * because saveAll(List<?>) method is not bulk insert(insert all at once).
    * It inserts data by loop.
    * */
    public void bulkInsert(List<Post> posts){
        var sql = String.format("""
                INSERT INTO %s (memberId, contents, createdDate, createdAt)
                VALUES (:memberId, :contents, :createdDate, :createdAt)
                """, TABLE);
        SqlParameterSource[] params = posts
                .stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);
        namedParameterJdbcTemplate.batchUpdate(sql,params);
    }


    private Post insert(Post post){
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");
        SqlParameterSource params = new BeanPropertySqlParameterSource(post);
        var id =  simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return Post.builder()
                .id(id)
                .memberId(post.getMemberId())
                .contents(post.getContents())
                .createdDate(post.getCreatedDate())
                .createdAt(post.getCreatedAt())
                .build();
    }

    private Post update(Post post) {
        String sql = String.format("""
                UPDATE %s SET
                memberId = :memberId,
                contents = :contents,
                createdDate = :createdDate,
                createdAt = :createdAt, 
                likeCount = :likeCount,
                version = :version +1
                WHERE id = :id and version = :version
                """, TABLE);
        var params = new BeanPropertySqlParameterSource(post);
        int updatedCount = namedParameterJdbcTemplate.update(sql, params);
        if(updatedCount==0) throw new RuntimeException("update fail");
        return post;
    }
}
