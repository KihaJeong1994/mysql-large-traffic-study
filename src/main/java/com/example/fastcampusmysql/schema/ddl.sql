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