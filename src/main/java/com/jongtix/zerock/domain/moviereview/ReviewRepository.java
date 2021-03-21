package com.jongtix.zerock.domain.moviereview;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * @EntityGraph
     * 엔티티의 특정한 속성을 같이 로딩하도록 표시하는 Annotation
     * 기본적으로 JPA를 이용하는 경우, 연관 관계의 FETCH 속성값은 LAZY로 지정하는 것이 일반적
     * 이런 상황에서 특정 기능을 수행할 때만 EAGER 로딩을 하도록 지정 가능
     * attributePaths: 로딩 설정을 변경하고 싶은 속성의 이름을 배열로 명시
     * type: @EntityGraph를 어떤 방식으로 적용할 것인지 설정
     *      - FETCH: attributePaths에 명시한 속성은 EAGER로 처리하고, 나머지는 LAZY로 처리
     *      - LOAD: attributePaths에 명시한 속성은 EAGER로 처리하고, 나머지는 엔티티 클래스에 명시되거나 기본 방식으로 처리
     */
    //특정 영화에 대한 모든 리뷰
    @EntityGraph(attributePaths = {"movieMember"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Review> findByMovie(Movie movie);

    //Member를 이용해서 Review 삭제1
//    void deleteByMovieMember(MovieMember member);

//    //Member를 이용해서 Review 삭제2
//    //1에서 발생하는 비효율성(review의 수에 따라 delete 쿼리가 반복하여 실행 됨)을 개선
//    //(수정 필요) 이 방식으로 테스트 수행할 경우 review가 지워지지 않음
//    @Modifying
//    @Query("delete from Review mr where mr.movieMember =:member")
//    void deleteByMovieMember(MovieMember member);

    //Member를 이용해서 Review 삭제3
    //1에서 발생하는 비효율성(review의 수에 따라 delete 쿼리가 반복하여 실행 됨)을 개선
    //2에서 발생하는 문제(review가 지워지지 않음) 해결
    @Transactional
    @Modifying
    @Query("delete from Review mr where mr.movieMember =:member")
    void deleteByMovieMember(MovieMember member);


}
