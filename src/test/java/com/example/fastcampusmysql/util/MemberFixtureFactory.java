package com.example.fastcampusmysql.util;

import com.example.fastcampusmysql.domain.member.entity.Member;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

public class MemberFixtureFactory {
    private static final Long DEFAULT_SEED = 1L;

    static public Member create(){
        return create(DEFAULT_SEED);
    }

    static public Member create(Long seed){
        var param = new EasyRandomParameters().seed(seed);
        return new EasyRandom(param).nextObject(Member.class);
    }
}
