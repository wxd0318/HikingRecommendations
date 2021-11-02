package org.zerock.board.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass //해당 어노테이션이 적용된 클래스는 테이블로 생성되지 않는다., BaseEntity(추상 클래스)를 상속한 엔티티의 클래스로 데이터베이스 테이블이 생성된다.
@EntityListeners(value = {AuditingEntityListener.class}) // 엔티티 객체의 변화를 감지, JPA 내부에서 엔티티 객체를 감지하는 역할은 AuditingEntityListener.class
@Getter
abstract class BaseEntity {
    @CreatedDate // JPA에서 엔티티의 생성 시간을 처리
    @Column(name = "regdate", updatable = false) // updatable = false: 해당 엔티티 객체를 데이터베이스에 반영할 때 regdate 칼럼값은 변경 안됨
    private LocalDateTime regDate;

    @LastModifiedDate // 최종 수정 시간을 자동으로 처리하는 용도
    @Column(name = "moddate")
    private LocalDateTime modDate;
}
