package com.jongtix.zerock.domain.board;

import com.jongtix.zerock.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "writer")
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;

    private String content;

    /**
     * Eager Loading(즉시 로딩) vs Lazy Loading(지연 로딩)
     * Eager Loading:   특정한 Entity를 조회할 때 연관관계를 가진 모든 Entity를 같이 로딩하는 것
     *                  한 번에 연관관계에 있는 모든 Entity를 가져올 수 있다는 장점
     *                  여러 연관관계를 맺고 있거나 연관관계가 복잡할수록 조인으로 인한 성능 저하
     * Lazy Loading:    단순하게 하나의 테이블을 이용하는 경우에는 빠른 속도의 처리가 가능
     *                  필요한 순간에 쿼리를 실행해야하기 때문에 연관관계가 복잡한 경우에는 여러 번의 쿼리가 실행
     *
     * '지연 로딩을 기본으로 사용하고, 상황에 맞게 필요한 방법을 찾는다'
     */
    @ManyToOne(fetch = FetchType.LAZY)  //지연 로딩 설정
    private Member writer;  //연관관계 지정

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

}
