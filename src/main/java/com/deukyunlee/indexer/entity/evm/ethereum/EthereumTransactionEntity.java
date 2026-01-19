package com.deukyunlee.indexer.entity.evm.ethereum;

import com.deukyunlee.indexer.entity.BaseEntity;
import com.deukyunlee.indexer.model.rpc.evm.result.block.EvmBlockReceiptsRpcDetailResult;
import com.deukyunlee.indexer.model.rpc.evm.result.block.EvmGetBlockRpcDetailTransactionResult;
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

import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;


/**
 * Created by dufqkd1004@naver.com on 2024. 12. 25.
 */
@Entity(name = "ethereum_transactions")
@Getter
@Table(
        name = "ethereum_transactions",
        indexes = {
                @Index(name = "uidx_eth_transactions_block_no_hash", columnList = "block_number, hash", unique = true),
                @Index(name = "idx_eth_transactions_block_date_success_from", columnList = "block_date, success, from_address"),
                @Index(name = "idx_eth_transactions_block_date_success_to", columnList = "block_date, success, to_address")

        }
)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EthereumTransactionEntity extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @jakarta.persistence.Id
    private long id;

    @Column(name = "block_number", nullable = false)
    private long blockNumber;

    @Column(name = "block_time", columnDefinition = "TIMESTAMP", nullable = false)
    private Instant blockTime;

    @Column(name = "value", precision = 78, scale = 0, nullable = true)
    private BigInteger value;

    @Column(name = "gas_limit", precision = 78, scale = 0, nullable = false)
    private BigInteger gasLimit;

    @Column(name = "gas_price", precision = 78, scale = 0, nullable = true)
    private BigInteger gasPrice;

    @Column(name = "gas_used", precision = 78, scale = 0, nullable = true)
    private BigInteger gasUsed;

    @Column(name = "max_fee_per_gas", precision = 78, scale = 0, nullable = true)
    private BigInteger maxFeePerGas;

    @Column(name = "max_priority_fee_per_gas", precision = 78, scale = 0, nullable = true)
    private BigInteger maxPriorityFeePerGas;

    @Column(name = "nonce", nullable = false)
    private BigInteger nonce;

    @Column(name = "index", nullable = false)
    private long index;

    @Column(name = "success", nullable = false)
    private boolean success;

    @Column(name = "from_address", nullable = false)
    private String from;

    @Column(name = "to_address", nullable = true)
    private String to;

    @Column(name = "block_hash", nullable = false)
    private String blockHash;

    @Column(name = "data", columnDefinition = "TEXT", nullable = true)
    private String data;

    @Column(name = "hash", nullable = false)
    private String hash;

    @Column(name = "type", nullable = true)
    private String type;

    @Column(name = "block_date", columnDefinition = "TIMESTAMP", nullable = false)
    private Instant blockDate;

    public EthereumTransactionEntity(long blockNumber, Instant blockTime, EvmBlockReceiptsRpcDetailResult transactionReceipt, EvmGetBlockRpcDetailTransactionResult transactionDetail) {
        this.blockNumber = blockNumber;
        this.blockTime = blockTime;
        this.value = NumberUtil.convertHexToBigInteger(transactionDetail.getValue());
        this.gasLimit = NumberUtil.convertHexToBigInteger(transactionDetail.getGasLimit());
        this.gasPrice = NumberUtil.convertHexToBigInteger(transactionDetail.getGasPrice());
        this.gasUsed = NumberUtil.convertHexToBigInteger(transactionReceipt.getGasUsed()); // transactionReceipt
        this.maxFeePerGas = Optional.ofNullable(transactionDetail.getMaxFeePerGas())
                .map(NumberUtil::convertHexToBigInteger)
                .orElse(null);
        this.maxPriorityFeePerGas = Optional.ofNullable(transactionDetail.getMaxPriorityFeePerGas())
                .map(NumberUtil::convertHexToBigInteger)
                .orElse(null);
        this.nonce = NumberUtil.convertHexToBigInteger(transactionDetail.getNonce());
        this.index = NumberUtil.convertHexToLong(transactionDetail.getTransactionIndex());
        this.success = NumberUtil.convertHexToBoolean(transactionReceipt.getStatus()); // transactionReceipt
        this.from = transactionDetail.getFrom();
        this.to = transactionDetail.getTo();
        this.blockHash = transactionDetail.getBlockHash();
        this.data = transactionDetail.getInput();
        this.hash = transactionDetail.getHash();
        this.type = transactionDetail.getType();
        this.blockDate = blockTime.truncatedTo(ChronoUnit.DAYS);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EthereumTransactionEntity that = (EthereumTransactionEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
