package com.deukyunlee.indexer.entity.evm.ethereum;

import com.deukyunlee.indexer.entity.BaseEntity;
import com.deukyunlee.indexer.util.AddressUtil;
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
import java.util.Objects;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 26.
 */
@Entity(name = "ethereum_erc_20_transfers")
@Table(
        name = "ethereum_erc_20_transfers",
        indexes = {
                @Index(name = "uidx_eth_erc_20_transfer_tx_hash_log_index", columnList = "transaction_hash, log_index", unique = true),
                @Index(name = "idx_eth_erc_20_transfer_date_ca_from", columnList = "block_date, contract_address, from_address"),
                @Index(name = "idx_eth_erc_20_transfer_date_ca_to", columnList = "block_date, contract_address, to_address"),
        }
)
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EthereumErc20TransferEntity extends BaseEntity {
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

    @Column(name = "contract_address", nullable = true)
    private String contractAddress;

    @Column(name = "from_address", nullable = true)
    private String from;

    @Column(name = "to_address", nullable = true)
    private String to;

    @Column(name = "amount", precision = 78, scale = 0, nullable = false)
    private BigInteger amount;

    @Column(name = "blockDate", columnDefinition = "TIMESTAMP", nullable = false)
    private Instant blockDate;

    public EthereumErc20TransferEntity(EthereumLogEntity ethereumLogEntity) {
        this.blockNumber = ethereumLogEntity.getBlockNumber();
        this.blockTime = ethereumLogEntity.getBlockTime();
        this.transactionHash = ethereumLogEntity.getTransactionHash();
        this.logIndex = ethereumLogEntity.getLogIndex();
        this.contractAddress = ethereumLogEntity.getContractAddress();
        this.from = AddressUtil.removeZeroPadding(ethereumLogEntity.getTopic1(), AddressUtil.ERC_20_TRANSFER_ADDRESS_TOPIC_BEGIN_INDEX);
        this.to = AddressUtil.removeZeroPadding(ethereumLogEntity.getTopic2(), AddressUtil.ERC_20_TRANSFER_ADDRESS_TOPIC_BEGIN_INDEX);
        this.amount = NumberUtil.convertHexToBigInteger(ethereumLogEntity.getData());
        this.blockDate = ethereumLogEntity.getBlockDate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EthereumErc20TransferEntity that = (EthereumErc20TransferEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
