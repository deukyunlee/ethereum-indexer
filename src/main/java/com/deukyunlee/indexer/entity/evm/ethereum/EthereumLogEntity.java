package com.deukyunlee.indexer.entity.evm.ethereum;

import com.deukyunlee.indexer.entity.BaseEntity;
import com.deukyunlee.indexer.model.rpc.evm.result.block.EvmBlockReceiptsRpcDetailLogResult;
import com.deukyunlee.indexer.model.rpc.evm.result.block.EvmBlockReceiptsRpcDetailResult;
import com.deukyunlee.indexer.util.ListUtil;
import com.deukyunlee.indexer.util.NumberUtil;
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
import java.time.temporal.ChronoUnit;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 26.
 */
@Entity(name = "ethereum_logs")
@Table(
        name = "ethereum_logs",
        indexes = {
                @Index(name = "uidx_eth_logs_tx_hash_log_index", columnList = "transaction_hash, log_index", unique = true),
        }
)
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EthereumLogEntity extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @jakarta.persistence.Id
    private long id;

    @Column(name = "block_number", nullable = false)
    private long blockNumber;

    @Column(name = "block_time", columnDefinition = "TIMESTAMP", nullable = false)
    private Instant blockTime;

    @Column(name = "transaction_hash", nullable = false)
    private String transactionHash;

    @Column(name = "log_index", nullable = false)
    private long logIndex;

    @Column(name = "topic0", nullable = true)
    private String topic0;

    @Column(name = "topic1", nullable = true)
    private String topic1;

    @Column(name = "topic2", nullable = true)
    private String topic2;

    @Column(name = "topic3", nullable = true)
    private String topic3;

    @Column(name = "contract_address", nullable = false)
    private String contractAddress;

    @Column(name = "tx_from", nullable = false)
    private String txFrom;

    @Column(name = "tx_to", nullable = true)
    private String txTo;

    @Column(name = "data", columnDefinition = "TEXT", nullable = false)
    private String data;

    @Column(name = "block_date", columnDefinition = "TIMESTAMP", nullable = false)
    private Instant blockDate;

    public EthereumLogEntity(long blockNumber, Instant blockTime, EvmBlockReceiptsRpcDetailResult transactionReceipt, EvmBlockReceiptsRpcDetailLogResult logData) {
        this.blockNumber = blockNumber;
        this.blockTime = blockTime;
        this.transactionHash = transactionReceipt.getTransactionHash();
        this.logIndex = NumberUtil.convertHexToLong(logData.getLogIndex());
        this.topic0 = ListUtil.getOrNull(logData.getTopics(), 0);
        this.topic1 = ListUtil.getOrNull(logData.getTopics(), 1);
        this.topic2 = ListUtil.getOrNull(logData.getTopics(), 2);
        this.topic3 = ListUtil.getOrNull(logData.getTopics(), 3);
        this.contractAddress = logData.getContractAddress();
        this.txFrom = transactionReceipt.getFrom();
        this.txTo = transactionReceipt.getTo();
        this.data = logData.getData();
        this.blockDate = blockTime.truncatedTo(ChronoUnit.DAYS);
    }
}
