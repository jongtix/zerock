package com.jongtix.zerock.domain.moviereview;

import com.jongtix.zerock.domain.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Movie extends BaseEntity {    //JPA M:N 관계를 처리할 때 매핑 테이블은 반드시 설계의 마지막 단계에서 처리하고, '명사'에 해당하는 Entity를 먼저 설계

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    private String title;

    public void update(String title) {
        this.title = title;
    }

}
