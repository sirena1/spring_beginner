package com.example.study.repository;

import com.example.study.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JdbcTemplateMemberRepository implements MemberRepository {

    // private final : 직접적으로 값을 참조할 수는 없지만, 생성자를 통해 값을 참조할 수 있다.
    // private static final : 생성자를 통해 값을 참조할 수 없다 -> 무조건 초기화 필요!
    private final JdbcTemplate jdbcTemplate;

    // 생성자가 1개라면 @Autowired 생략가능
    @Autowired
    public JdbcTemplateMemberRepository(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Member save(Member member) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("member").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", member.getName());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        member.setId(key.longValue());
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        List<Member> result =  jdbcTemplate.query( "select * from member where id = ?", memberRowMapper(), id );
        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result =  jdbcTemplate.query( "select * from member where name = ?", memberRowMapper(), name );
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return jdbcTemplate.query( "select * from member", memberRowMapper() );
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {

            Member member = new Member();
            member.setId(rs.getLong("id"));
            member.setName(rs.getString("name"));

            return member;
        };
    }
}
