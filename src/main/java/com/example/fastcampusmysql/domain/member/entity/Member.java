package com.example.fastcampusmysql.domain.member.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Member {
    private static final Long NAME_MAX_LENGTH = 10L;
    private final Long id;
    private String nickname;
    private final String email;
    private final LocalDate birthday;
    private final LocalDateTime createdAt;


    @Builder
    public Member(Long id, String nickname, String email, LocalDate birthday, LocalDateTime createdAt) {
        this.id = id;
        this.email = Objects.requireNonNull(email);
        this.birthday = Objects.requireNonNull(birthday);
        this.nickname = Objects.requireNonNull(nickname);
        validateNickname(nickname);
        this.createdAt = createdAt ==null ? LocalDateTime.now() : createdAt;
    }

    void validateNickname(String nickname){
        Assert.isTrue(nickname.length()<=NAME_MAX_LENGTH,"nickname should be less than "+NAME_MAX_LENGTH);
    }
}
