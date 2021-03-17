insert into member(moddate, regdate, name, password, email) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'name1', 'password1', 'email1');
insert into member(moddate, regdate, name, password, email) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'name2', 'password2', 'email2');
insert into member(moddate, regdate, name, password, email) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'name3', 'password3', 'email3');

insert into board(moddate, regdate, content, title, writer_email) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'content1', 'title1', 'email1');
insert into board(moddate, regdate, content, title, writer_email) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'content2', 'title2', 'email2');
insert into board(moddate, regdate, content, title, writer_email) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'content3', 'title3', 'email3');

insert into reply(moddate, regdate, board_bno, replyer, text) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 1, 'replyer1', 'text1');
insert into reply(moddate, regdate, board_bno, replyer, text) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 2, 'replyer2', 'text2');
insert into reply(moddate, regdate, board_bno, replyer, text) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 3, 'replyer3', 'text3');
insert into reply(moddate, regdate, board_bno, replyer, text) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 1, 'replyer4', 'text4');
insert into reply(moddate, regdate, board_bno, replyer, text) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 2, 'replyer5', 'text5');
insert into reply(moddate, regdate, board_bno, replyer, text) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 3, 'replyer6', 'text6');
insert into reply(moddate, regdate, board_bno, replyer, text) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 1, 'replyer7', 'text7');
insert into reply(moddate, regdate, board_bno, replyer, text) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 2, 'replyer8', 'text8');
insert into reply(moddate, regdate, board_bno, replyer, text) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 3, 'replyer9', 'text9');