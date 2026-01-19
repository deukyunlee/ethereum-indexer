package com.deukyunlee.indexer.entity.evm.ethereum;

import com.deukyunlee.indexer.entity.BaseEntity;
import com.deukyunlee.indexer.model.rpc.evm.result.block.EvmGetBlockRpcDetailResult;
import com.deukyunlee.indexer.util.NumberUtil;
import com.deukyunlee.indexer.util.TimeUtil;
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

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 24.
 */
@Entity(name = "ethereum_blocks")
@Getter
@Table(
        name = "ethereum_blocks",
        indexes = {
                @Index(name = "uidx_eth_blocks_number", columnList = "number", unique = true)
        }
)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EthereumBlockEntity extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @jakarta.persistence.Id
    private long id;

    @Column(name = "number", nullable = false)
    private long number;

    @Column(name = "hash", nullable = false)
    private String hash;

    @Column(name = "time", columnDefinition = "TIMESTAMP", nullable = false)
    private Instant time;

    @Column(name = "gas_limit",precision = 78, scale = 0, nullable = false)
    private BigInteger gasLimit;

    @Column(name = "gas_used",precision = 78, scale = 0, nullable = false)
    private BigInteger gasUsed;

    @Column(name = "difficulty", precision = 78, scale = 0,nullable = true)
    private BigInteger difficulty;

    @Column(name = "size", precision = 78, scale = 0,nullable = true)
    private BigInteger size;

    @Column(name = "base_fee_per_gas", precision = 78, scale = 0,nullable = true)
    private BigInteger baseFeePerGas;

    @Column(name = "parent_hash", nullable = false)
    private String parentHash;

    @Column(name = "miner", nullable = false)
    private String miner;

    @Column(name = "nonce", nullable = true)
    private String nonce;

    @Column(name = "date", columnDefinition = "TIMESTAMP", nullable = false)
    private Instant date;

    @Column(name = "blob_gas_used", precision = 78, scale = 0,nullable = true)
    private BigInteger blobGasUsed;

    @Column(name = "parent_beacon_block_root", nullable = true)
    private String parentBeaconBlockRoot;

    public EthereumBlockEntity(EvmGetBlockRpcDetailResult evmGetBlockRpcDetailResult) {
        this.hash = evmGetBlockRpcDetailResult.getHash();
        this.time = TimeUtil.convertHexToInstant(evmGetBlockRpcDetailResult.getTime());
        this.number = NumberUtil.convertHexToLong(evmGetBlockRpcDetailResult.getNumber());
        this.gasLimit = NumberUtil.convertHexToBigInteger(evmGetBlockRpcDetailResult.getGasLimit());
        this.gasUsed = NumberUtil.convertHexToBigInteger(evmGetBlockRpcDetailResult.getGasUsed());
        this.difficulty = NumberUtil.convertHexToBigInteger(evmGetBlockRpcDetailResult.getDifficulty());
        this.size = NumberUtil.convertHexToBigInteger(evmGetBlockRpcDetailResult.getSize());
        this.baseFeePerGas = NumberUtil.convertHexToBigInteger(evmGetBlockRpcDetailResult.getBaseFeePerGas());
        this.parentHash = evmGetBlockRpcDetailResult.getParentHash();
        this.miner = evmGetBlockRpcDetailResult.getMiner();
        this.nonce = evmGetBlockRpcDetailResult.getNonce();
        this.date = TimeUtil.convertHexToInstant(evmGetBlockRpcDetailResult.getTime()).truncatedTo(ChronoUnit.DAYS);
        this.blobGasUsed = NumberUtil.convertHexToBigInteger(evmGetBlockRpcDetailResult.getGasUsed());
        this.parentBeaconBlockRoot = evmGetBlockRpcDetailResult.getParentBeaconBlockRoot();
    }
}