package com.deukyunlee.indexer.model.account.dto;

import com.deukyunlee.indexer.entity.evm.ethereum.EthereumErc20TransferEntity;
import com.deukyunlee.indexer.entity.evm.ethereum.EthereumTransactionEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigInteger;
import java.time.Instant;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 22.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountTokenTransferDto {
    private String from;
    private String to;
    private BigInteger value;
    private Instant timestamp;

    public AccountTokenTransferDto(EthereumErc20TransferEntity ethereumErc20TransferEntity) {
        this.from = ethereumErc20TransferEntity.getFrom();
        this.to = ethereumErc20TransferEntity.getTo();
        this.value = ethereumErc20TransferEntity.getAmount();
        this.timestamp = ethereumErc20TransferEntity.getBlockTime();
    }

    public AccountTokenTransferDto(EthereumTransactionEntity ethereumTransactionEntity) {
        this.from = ethereumTransactionEntity.getFrom();
        this.to = ethereumTransactionEntity.getTo();
        this.value = ethereumTransactionEntity.getValue();
        this.timestamp = ethereumTransactionEntity.getBlockDate();
    }
}
