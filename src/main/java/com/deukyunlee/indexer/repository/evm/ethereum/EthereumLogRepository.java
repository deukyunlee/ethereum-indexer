package com.deukyunlee.indexer.repository.evm.ethereum;

import com.deukyunlee.indexer.entity.evm.ethereum.EthereumLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 27.
 */
public interface EthereumLogRepository extends JpaRepository<EthereumLogEntity, Long> {

}