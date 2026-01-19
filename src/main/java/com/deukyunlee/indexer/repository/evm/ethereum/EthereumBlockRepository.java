package com.deukyunlee.indexer.repository.evm.ethereum;

import com.deukyunlee.indexer.entity.evm.ethereum.EthereumBlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 25.
 */
public interface EthereumBlockRepository extends JpaRepository<EthereumBlockEntity, Long> {
    Optional<EthereumBlockEntity> findFirstByOrderByNumberDesc();
    Optional<EthereumBlockEntity> findFirstByOrderByNumberAsc();

    Optional<EthereumBlockEntity> findFirstByDateOrderByTimeDesc(Instant date);
}
