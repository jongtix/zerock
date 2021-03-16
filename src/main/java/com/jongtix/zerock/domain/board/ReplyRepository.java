package com.jongtix.zerock.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Modifying  //JPQL을 이용해서 update, delete를 실행하기 위해서 사용하는 어노테이션
    @Query("delete from Reply r  where r.board.bno =:bno")
    void deleteByBno(Long bno);

}
