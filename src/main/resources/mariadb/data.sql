insert into member(moddate, regdate, name, password, email) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'name1', 'password1', 'email1');
insert into member(moddate, regdate, name, password, email) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'name2', 'password2', 'email2');
insert into member(moddate, regdate, name, password, email) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'name3', 'password3', 'email3');

insert into board(moddate, regdate, content, title, writer_email) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'content1', 'title1', 'email1');
insert into board(moddate, regdate, content, title, writer_email) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'content2', 'title2', 'email2');
insert into board(moddate, regdate, content, title, writer_email) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'content3', 'title3', 'email3');
insert into board(moddate, regdate, content, title, writer_email) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'content4', 'title4', 'email1');
insert into board(moddate, regdate, content, title, writer_email) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'content5', 'title5', 'email2');
insert into board(moddate, regdate, content, title, writer_email) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'content6', 'title6', 'email3');
insert into board(moddate, regdate, content, title, writer_email) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'content7', 'title7', 'email1');
insert into board(moddate, regdate, content, title, writer_email) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'content8', 'title8', 'email2');
insert into board(moddate, regdate, content, title, writer_email) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'content9', 'title9', 'email3');
insert into board(moddate, regdate, content, title, writer_email) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'content10', 'title10', 'email1');
insert into board(moddate, regdate, content, title, writer_email) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'content11', 'title11', 'email2');
insert into board(moddate, regdate, content, title, writer_email) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'content12', 'title12', 'email3');

insert into reply(moddate, regdate, board_bno, replyer, text) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 1, 'replyer1', 'text1');
insert into reply(moddate, regdate, board_bno, replyer, text) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 2, 'replyer2', 'text2');
insert into reply(moddate, regdate, board_bno, replyer, text) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 3, 'replyer3', 'text3');
insert into reply(moddate, regdate, board_bno, replyer, text) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 1, 'replyer4', 'text4');
insert into reply(moddate, regdate, board_bno, replyer, text) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 2, 'replyer5', 'text5');
insert into reply(moddate, regdate, board_bno, replyer, text) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 3, 'replyer6', 'text6');
insert into reply(moddate, regdate, board_bno, replyer, text) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 1, 'replyer7', 'text7');
insert into reply(moddate, regdate, board_bno, replyer, text) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 2, 'replyer8', 'text8');
insert into reply(moddate, regdate, board_bno, replyer, text) values(CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 3, 'replyer9', 'text9');

insert into movie(moddate, regdate, title) values(current_timestamp(), current_timestamp(), 'title1');
insert into movie_image(img_name, movie_mno, path, uuid) values('imgName1.jpg', 1, 'imgPath11', 'ae0632bb-ad9d-42ac-a36a-141f3a8dfa16');
insert into movie_image(img_name, movie_mno, path, uuid) values('imgName1.png', 1, 'imgPath12', 'ae0632bb-ad9d-42ac-a36a-141f3a8dfa17');
insert into movie_image(img_name, movie_mno, path, uuid) values('imgName1.gif', 1, 'imgPath13', 'ae0632bb-ad9d-42ac-a36a-141f3a8dfa18');
insert into movie(moddate, regdate, title) values(current_timestamp(), current_timestamp(), 'title2');
insert into movie_image(img_name, movie_mno, path, uuid) values('imgName2.jpg', 2, 'imgPath21', 'ae0632bb-ad9d-42ac-a36a-141f3a8dfa26');
insert into movie_image(img_name, movie_mno, path, uuid) values('imgName2.png', 2, 'imgPath22', 'ae0632bb-ad9d-42ac-a36a-141f3a8dfa27');
insert into movie_image(img_name, movie_mno, path, uuid) values('imgName2.gif', 2, 'imgPath23', 'ae0632bb-ad9d-42ac-a36a-141f3a8dfa28');
insert into movie(moddate, regdate, title) values(current_timestamp(), current_timestamp(), 'title3');
insert into movie_image(img_name, movie_mno, path, uuid) values('imgName3.jpg', 3, 'imgPath31', 'ae0632bb-ad9d-42ac-a36a-141f3a8dfa36');
insert into movie_image(img_name, movie_mno, path, uuid) values('imgName3.png', 3, 'imgPath32', 'ae0632bb-ad9d-42ac-a36a-141f3a8dfa37');
insert into movie_image(img_name, movie_mno, path, uuid) values('imgName3.gif', 3, 'imgPath33', 'ae0632bb-ad9d-42ac-a36a-141f3a8dfa38');
insert into movie(moddate, regdate, title) values(current_timestamp(), current_timestamp(), 'title4');
insert into movie_image(img_name, movie_mno, path, uuid) values('imgName4.jpg', 4, 'imgPath41', 'ae0632bb-ad9d-42ac-a36a-141f3a8dfa46');
insert into movie_image(img_name, movie_mno, path, uuid) values('imgName4.png', 4, 'imgPath42', 'ae0632bb-ad9d-42ac-a36a-141f3a8dfa47');
insert into movie_image(img_name, movie_mno, path, uuid) values('imgName4.gif', 4, 'imgPath43', 'ae0632bb-ad9d-42ac-a36a-141f3a8dfa48');
insert into movie(moddate, regdate, title) values(current_timestamp(), current_timestamp(), 'title5');
insert into movie_image(img_name, movie_mno, path, uuid) values('imgName5.jpg', 5, 'imgPath51', 'ae0632bb-ad9d-42ac-a36a-141f3a8dfa56');
insert into movie_image(img_name, movie_mno, path, uuid) values('imgName5.png', 5, 'imgPath52', 'ae0632bb-ad9d-42ac-a36a-141f3a8dfa57');
insert into movie_image(img_name, movie_mno, path, uuid) values('imgName5.gif', 5, 'imgPath53', 'ae0632bb-ad9d-42ac-a36a-141f3a8dfa58');

insert into m_member(moddate, regdate, email, nickname, password) values(current_timestamp(), current_timestamp(), 'email1', 'nickname1', 'password1');
insert into m_member(moddate, regdate, email, nickname, password) values(current_timestamp(), current_timestamp(), 'email2', 'nickname2', 'password2');
insert into m_member(moddate, regdate, email, nickname, password) values(current_timestamp(), current_timestamp(), 'email3', 'nickname3', 'password3');
insert into m_member(moddate, regdate, email, nickname, password) values(current_timestamp(), current_timestamp(), 'email4', 'nickname4', 'password4');
insert into m_member(moddate, regdate, email, nickname, password) values(current_timestamp(), current_timestamp(), 'email5', 'nickname5', 'password5');

insert into review(moddate, regdate, grade, movie_mno, movie_member_mid, text) values(current_timestamp(), current_timestamp(), 5, 1, 1, 'text11');
insert into review(moddate, regdate, grade, movie_mno, movie_member_mid, text) values(current_timestamp(), current_timestamp(), 3, 1, 2, 'text12');
insert into review(moddate, regdate, grade, movie_mno, movie_member_mid, text) values(current_timestamp(), current_timestamp(), 5, 1, 3, 'text13');
insert into review(moddate, regdate, grade, movie_mno, movie_member_mid, text) values(current_timestamp(), current_timestamp(), 2, 1, 4, 'text14');
insert into review(moddate, regdate, grade, movie_mno, movie_member_mid, text) values(current_timestamp(), current_timestamp(), 4, 1, 5, 'text15');
insert into review(moddate, regdate, grade, movie_mno, movie_member_mid, text) values(current_timestamp(), current_timestamp(), 5, 2, 1, 'text21');
insert into review(moddate, regdate, grade, movie_mno, movie_member_mid, text) values(current_timestamp(), current_timestamp(), 5, 2, 3, 'text23');
insert into review(moddate, regdate, grade, movie_mno, movie_member_mid, text) values(current_timestamp(), current_timestamp(), 4, 2, 5, 'text25');
insert into review(moddate, regdate, grade, movie_mno, movie_member_mid, text) values(current_timestamp(), current_timestamp(), 2, 4, 1, 'text41');
insert into review(moddate, regdate, grade, movie_mno, movie_member_mid, text) values(current_timestamp(), current_timestamp(), 1, 4, 2, 'text42');
insert into review(moddate, regdate, grade, movie_mno, movie_member_mid, text) values(current_timestamp(), current_timestamp(), 5, 4, 3, 'text43');
insert into review(moddate, regdate, grade, movie_mno, movie_member_mid, text) values(current_timestamp(), current_timestamp(), 5, 4, 4, 'text44');
insert into review(moddate, regdate, grade, movie_mno, movie_member_mid, text) values(current_timestamp(), current_timestamp(), 4, 4, 5, 'text45');
insert into review(moddate, regdate, grade, movie_mno, movie_member_mid, text) values(current_timestamp(), current_timestamp(), 3, 5, 1, 'text51');
insert into review(moddate, regdate, grade, movie_mno, movie_member_mid, text) values(current_timestamp(), current_timestamp(), 3, 5, 5, 'text55');

insert into club_member (moddate, regdate, from_social, name, password, email) values(current_timestamp(), current_timestamp(), false, 'name', '$2a$10$p10DGyVLMLQgrKI26SMGqul9b3bXO5Co1OJBP1Y8MNSEMa36kOWjO', 'user@zerock.org');
insert into club_member_role_set (club_member_email, role_set) values('user@zerock.org', 2);

