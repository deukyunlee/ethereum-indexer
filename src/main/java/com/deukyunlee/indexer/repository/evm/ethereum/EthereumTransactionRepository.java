package com.deukyunlee.indexer.repository.evm.ethereum;

import com.deukyunlee.indexer.entity.evm.ethereum.EthereumTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 25.
 */
public interface EthereumTransactionRepository extends JpaRepository<EthereumTransactionEntity, Long> {
    List<EthereumTransactionEntity> findAllByBlockDateAndSuccessAndFrom(Instant blockDate, boolean success, String accountAddress);
    List<EthereumTransactionEntity> findAllByBlockDateAndSuccessAndTo(Instant blockDate, boolean success, String accountAddress);
}