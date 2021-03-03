package com.jongtix.zerock.domain.memo;


import lombok.*;

import javax.persistence.*;

@Entity //Entity: 해당 클래스가 엔티티를 위한 클래스이며, 해당 클래스의 인스턴스들이 JPA로 관리되는 엔티티 객체라는 것을 의미
        //        자동으로 테이블을 생설할 수 있는 옵션 있음
@Table(name = "tbl_memo")   //Table: @Entity와 함께 사용할 수 있는 어노테이션
                            //       데이터베이스상에서 엔티티 클래스를 어떤 테이블로 생성할 것인지에 대한 정보를 담고 있음
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor  //@Builder를 이용하기 위해서는 @AllArgsConstructor와 @NoArgsConstructor를 항상 같이 처리해야 컴파일 에러가 발생하지 않음
/**
 * 출처: https://dingue.tistory.com/14
 * @NoArgsContructor를 사용할 때 주의점
 * 1. 필드들이 final로 생성되어 있는 경우에는 필드를 초기화 할 수 없기 때문에 생성자를 만들 수 없고 에러가 발생하게 됩니다.
 *      이 때는 @NoArgsConstructor(force = true) 옵션을 이용해서 final 필드를 0, false, null 등으로 초기화를 강제로 시켜서 생성자를 만들 수 있습니다.
 * 2. @NonNull 같이 필드에 제약조건이 설정되어 있는 경우, 생성자내 null-check 로직이 생성되지 않습니다.
 *      후에 초기화를 진행하기 전까지 null-check 로직이 발생하지 않는 점을 염두하고 코드를 개발해야 합니다.
 */
public class Memo {

    /**
     * 키 생성 전략
     * AUTO: JPA 구현체가 생성 방식을 결정
     * IDENTITY: 사용하는 데이터베이스가 키 생성을 결정(Oracle - 번호를 위한 별도의 테이블 생성 / MySQL & MariaDB - auto_increment
     * SEQUENCE: 데이터베이스의 sequence를 이용해 키 생성(@SequenceGenerator와 함께 사용)
     * TABLE: 키 생성 전용 테이블을 생성해서 키 생성(@TableGenerator와 함께 사용)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    @Column(length = 200, nullable = false)
    private String memoText;

//    @Column(columnDefinition = "varchar(255) default 'Yes'")  //기본값 추가하기 위한 방법

}
