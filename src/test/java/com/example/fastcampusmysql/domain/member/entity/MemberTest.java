package com.example.fastcampusmysql.domain.member.entity;

import com.example.fastcampusmysql.util.MemberFixtureFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {
    @DisplayName("user can change nickname")
    @Test
    void testChangeNickname() {
        var member = MemberFixtureFactory.create();
        var expected = "pnu";
        member.changeNickname(expected);
        assertEquals(expected, member.getNickname());
//        LongStream.range(0,10)
//                .mapToObj(MemberFixtureFactory::create)
//                .forEach(m->{
//                    System.out.println(m.getNickname());
//                });

    }

    @DisplayName("User nickname should be less than equal 10")
    @Test
    void testChangeNicknameMaxLength() {
        var member = MemberFixtureFactory.create();
        var overMaxLengthName = "pinokkioggg";
        assertThrows(IllegalArgumentException.class
                ,()->member.changeNickname(overMaxLengthName));
    }
}