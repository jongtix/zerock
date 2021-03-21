package com.jongtix.zerock.domain.moviereview;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    /**
     * N+1문제
     * 한번의 쿼리로 N개의 데이터를 가져왔는데
     * 그 N개의 데이터를 처리하기 위해서 필요한 추가적인 쿼리가 각 N개에 대해서 수행되는 상황
     */
    //영화 목록 페이징
//    @Query( "select m, max(mi), avg(coalesce(r.grade, 0)), count(distinct r) " +  //N+1문제 발생
    @Query( "select m, mi, avg(coalesce(r.grade, 0)), count(distinct r) " + //N+1문제 해결을 위해 max 삭제
                                                                            //이렇게 하면 가장 먼저 입력된 이미지 번호와 매핑되어 연결
                                                                            //=> 추가적인 쿼리 조회가 발생하지 않음(N+1문제 해결)
            "from Movie m " +
            "left outer join MovieImage mi on mi.movie = m " +
            "left outer join Review r on r.movie = m " +
            "group by m")
    Page<Object[]> getListPage(Pageable pageable);

    //특정 영화 조회
    @Query( "select m, mi, avg(coalesce(r.grade, 0)), count(r) " +
            "from Movie m " +
            "left outer join MovieImage mi on mi.movie = m " +
            "left outer join Review r on r.movie = m " +
            "where m.mno = :mno " +
            "group by mi")
    List<Object[]> getMovieWithAll(Long mno);

}
