package com.deukyunlee.indexer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 25.
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    @Column(name = "created_at", columnDefinition = "TIMESTAMP", nullable = false)
    private Instant createTimestamp;

    @LastModifiedDate
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP", nullable = false)
    private Instant modifyTimestamp;
}
