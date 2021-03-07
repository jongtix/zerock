package com.jongtix.zerock.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass   //JPA Entity 클래스들이 BaseTimeEntity를 상속할 경우 필드들도 칼럼으로 인식하도록 설정
                    //해당 어노테이션이 적용된 클래스는 테이블로 생성되지 않음
@EntityListeners(value = { AuditingEntityListener.class })  //BaseTimeEntity 클래스에 Auditing 기능을 포함
/**
 * MyBatis와 다르게 JPA에서는 엔티티 객체를 만든 후 Persistence Context에 유지시키고 필요할 때 꺼내서 재사용하는 방식을 사용
 * 이때 엔티티 객체에 변화가 일어나는 것을 감지하는 Listener가 존재하고 AuditingEntityListener는 그 중 하나
 * AuditingEntityListener:  JPA 내부에서 엔티티 객체가 생성/변경되는 것을 감지하는 역할
 *                          활성화 하기 위해 @EnableJpaAuditing 필요
 */
@Getter
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "regdate", updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate
    @Column(name = "moddate")
    private LocalDateTime modDate;

}
