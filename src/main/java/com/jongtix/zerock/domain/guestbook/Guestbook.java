package com.jongtix.zerock.domain.guestbook;

import com.jongtix.zerock.domain.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Guestbook extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gno;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 1500, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    //Entity 클래스의 경우 가능하면 setter 관련 기능을 만들지 않는 것이 권장 사항
    //-> Entity 객체가 ㅇ어플리케이션 내부에서 변경되면 JPA를 관리하는 쪽이 복잡해질 우려가 있기 때문)
    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

}
