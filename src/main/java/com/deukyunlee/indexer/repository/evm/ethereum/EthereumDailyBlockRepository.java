package com.deukyunlee.indexer.repository.evm.ethereum;

import com.deukyunlee.indexer.entity.evm.ethereum.EthereumDailyBlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 25.
 */
public interface EthereumDailyBlockRepository extends JpaRepository<EthereumDailyBlockEntity, Long> {
    Optional<EthereumDailyBlockEntity> findByDate(Instant date);
    Optional<EthereumDailyBlockEntity> findFirstByOrderByDateDesc();
}

