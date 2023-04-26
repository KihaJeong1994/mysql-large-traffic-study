CREATE TABLE Member
(
    id int auto_increment,
    email varchar(20) not null,
    nickname varchar(20) not null,
    birthday date not null,
    createdAt datetime not null,
    constraint member_id_uindex
        primary key (id)
);

CREATE TABLE MemberNicknameHistory
(
    id int auto_increment,
    memberId int not null,
    nickname varchar(20) not null,
    createdAt datetime not null,
    constraint memberNicknameHistory_id_uidex
        primary key (id)
);

CREATE TABLE Follow
(
  id int auto_increment,
  fromMemberId int not null,
  toMemberId int not null,
  createdAt datetime not null,
  constraint follow_id_uindex
    primary key(id)
);

CREATE TABLE Post
(
    id int auto_increment,
    memberId int not null,
    contents varchar(100) not null,
    createdDate date not null,
    createdAt datetime not null,
    constraint post_id_uindex
        primary key(id)
);

CREATE TABLE Timeline
(
    id int auto_increment,
    memberId int not null ,
    postId int not null,
    createdAt datetime not null ,
    constraint Timeline_id_uindex
        primary key (id)
);

CREATE INDEX POST__index_member_id
    ON Post(memberId);

CREATE INDEX POST__index_created_date
    ON Post(createdDate);

CREATE INDEX POST__index_member_id_created_date
    on Post(memberId,createdDate);

ALTER TABLE Post ADD COLUMN likeCount int;