package com.deukyunlee.indexer.scheduler.evm;

import com.deukyunlee.indexer.facade.evm.EvmBlockV1FacadeService;
import com.deukyunlee.indexer.model.type.evm.EvmChainType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 25.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class EthereumScheduler {
    private final EvmBlockV1FacadeService evmBlockV1FacadeService;

    @Scheduled(fixedDelay = 12000)
    public void processEthereumBlocks() {
        evmBlockV1FacadeService.processBlocks(EvmChainType.ETHEREUM_MAINNET);
    }
}