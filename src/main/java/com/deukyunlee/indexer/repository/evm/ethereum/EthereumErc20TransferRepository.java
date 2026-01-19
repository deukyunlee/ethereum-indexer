package com.deukyunlee.indexer.repository.evm.ethereum;

import com.deukyunlee.indexer.entity.evm.ethereum.EthereumErc20TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 25.
 */
public interface EthereumErc20TransferRepository extends JpaRepository<EthereumErc20TransferEntity, Long> {
    List<EthereumErc20TransferEntity> findAllByBlockDateAndContractAddressAndFrom(Instant date, String contractAddress, String from);
    List<EthereumErc20TransferEntity> findAllByBlockDateAndContractAddressAndTo(Instant date, String contractAddress, String to);
}
