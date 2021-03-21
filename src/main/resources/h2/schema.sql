drop table if exists board;

drop table if exists member;

drop table if exists reply;

drop table if exists guestbook;

drop table if exists tbl_memo;

drop table if exists m_member;

drop table if exists movie;

drop table if exists movie_image;

drop table if exists review;

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

create table m_member (
           mid bigint not null auto_increment,
            moddate datetime(6),
            regdate datetime(6),
            email varchar(255),
            nickname varchar(255),
            password varchar(255),
            primary key (mid)
        ) engine=InnoDB;

create table movie (
           mno bigint not null auto_increment,
            moddate datetime(6),
            regdate datetime(6),
            title varchar(255),
            primary key (mno)
        ) engine=InnoDB;

create table movie_image (
           inum bigint not null auto_increment,
            img_name varchar(255),
            path varchar(255),
            uuid varchar(255),
            movie_mno bigint,
            primary key (inum)
        ) engine=InnoDB;

create table review (
           reviewnum bigint not null auto_increment,
            moddate datetime(6),
            regdate datetime(6),
            grade integer not null,
            text varchar(255),
            movie_mno bigint,
            movie_member_mid bigint,
            primary key (reviewnum)
        ) engine=InnoDB;

alter table reply
           add constraint FKr1bmblqir7dalmh47ngwo7mcs
           foreign key (board_bno)
           references board (bno);

alter table movie_image
           add constraint FKitwj3761d8j8ku189u4qrseih
           foreign key (movie_mno)
           references movie (mno);

alter table review
           add constraint FKdg4bkv5wfpxx015elj4h915gw
           foreign key (movie_mno)
           references movie (mno);

alter table review
           add constraint FK72p4lhkux0byjmyc5cys6t17g
           foreign key (movie_member_mid)
           references m_member (mid);