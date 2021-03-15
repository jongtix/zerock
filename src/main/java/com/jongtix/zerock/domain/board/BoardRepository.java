package com.jongtix.zerock.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    //JPQL의 조인을 이용하여 복합적인 결과 조회
    //Main Entity 클래스 내부에 Sub Entity와의 연관관계가 있는 경우
    //한개의 로우(Object) 내에 Object[]로 나옴
    @Query("select b, w from Board b left join b.writer w where b.bno =:bno")
    Object getBoardWithWriter(@Param("bno") Long bno);

    //JPQL의 조인을 이용하여 복합적인 결과 조회
    //Main Entity 클래스 내부에 Sub Entity와의 연관관계가 없는 조인 처리에는 on
//    @Query("select b, r from board b left join reply r on r.board = b where b.bno =:bno") //Table명의 첫 글자는 대문자가 와야하는 듯
    @Query("select b, r from Board b left join Reply r on r.board = b where b.bno =:bno")
    List<Object[]> getBoardWIthReply(@Param("bno") Long bno);

}
