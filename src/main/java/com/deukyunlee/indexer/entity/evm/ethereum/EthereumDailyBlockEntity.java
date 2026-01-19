package com.deukyunlee.indexer.entity.evm.ethereum;

import com.deukyunlee.indexer.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 25.
 */
@Entity(name = "ethereum_daily_blocks")
@Table(
        name = "ethereum_daily_blocks",
        indexes = {
                @Index(name = "uidx_eth_daily_blocks_date_no", columnList = "date, block_number", unique = true),
        }
)
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EthereumDailyBlockEntity extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @jakarta.persistence.Id
    private long id;

    @Column(name = "date", columnDefinition = "TIMESTAMP")
    private Instant date;

    @Column(name = "block_number")
    private long blockNumber;

    public EthereumDailyBlockEntity(Instant date, long blockNumber) {
        this.date = date;
        this.blockNumber = blockNumber;
    }

    public EthereumDailyBlockEntity(EthereumBlockEntity ethereumBlockEntity) {
        this.date = ethereumBlockEntity.getDate();
        this.blockNumber = ethereumBlockEntity.getNumber();
    }
}
