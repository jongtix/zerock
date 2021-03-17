drop table if exists board;

drop table if exists member;

drop table if exists reply;

drop table if exists guestbook;

drop table if exists tbl_memo;

create table board (
       bno bigint not null auto_increment,
        moddate datetime(6),
        regdate datetime(6),
        content varchar(255),
        title varchar(255),
        writer_email varchar(255),
        primary key (bno)
    ) engine=InnoDB;

create table member (
       email varchar(255) not null,
        moddate datetime(6),
        regdate datetime(6),
        name varchar(255),
        password varchar(255),
        primary key (email)
    ) engine=InnoDB;

create table reply (
       rno bigint not null auto_increment,
        moddate datetime(6),
        regdate datetime(6),
        replyer varchar(255),
        text varchar(255),
        board_bno bigint,
        primary key (rno)
    ) engine=InnoDB;

create table guestbook (
       gno bigint not null auto_increment,
        moddate datetime(6),
        regdate datetime(6),
        content varchar(1500) not null,
        title varchar(100) not null,
        writer varchar(50) not null,
        primary key (gno)
    ) engine=InnoDB;

create table tbl_memo (
       mno bigint not null auto_increment,
        memo_text varchar(200) not null,
        primary key (mno)
    ) engine=InnoDB;