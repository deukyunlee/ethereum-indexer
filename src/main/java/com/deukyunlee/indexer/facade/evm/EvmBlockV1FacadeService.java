package com.deukyunlee.indexer.facade.evm;

import com.deukyunlee.indexer.model.type.evm.EvmChainType;
import com.deukyunlee.indexer.service.chain.evm.EvmChainV1Service;
import com.deukyunlee.indexer.service.chain.evm.factory.EvmChainV1StrategyFactory;
import com.deukyunlee.indexer.service.chain.evm.strategy.EvmChainV1Strategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.LongStream;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 25.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EvmBlockV1FacadeService {
    private final EvmChainV1StrategyFactory evmChainV1StrategyFactory;
    private final EvmChainV1Service evmChainV1Service;
    private static final long BLOCKS_CREATED_IN_EIGHT_DAYS = 57600;

    public void processBlocks(EvmChainType evmChainType) {
        log.info("*** Start process blocks ***");
        EvmChainV1Strategy evmChainV1Strategy = evmChainV1StrategyFactory.getStrategy(evmChainType);

        long latestBlockNumber = evmChainV1Service.getLatestBlockNumber(evmChainV1Strategy);
        long latestProcessedBlockNumber = evmChainV1Service.getLatestProcessedBlockNumber(evmChainV1Strategy);

        long latestFinalizedBlockNumber = (latestBlockNumber - evmChainType.getBlocksToBeFinalized());
        if (latestFinalizedBlockNumber > latestProcessedBlockNumber) {
            long startBlockNumber = (latestProcessedBlockNumber == 0) ?
                    latestFinalizedBlockNumber - BLOCKS_CREATED_IN_EIGHT_DAYS :
                    latestProcessedBlockNumber + 1;

            List<Long> blockNumbers = LongStream.rangeClosed(startBlockNumber, latestFinalizedBlockNumber)
                    .boxed()
                    .toList();

            log.info("start blockNumber: {}, end blockNumber: {}", startBlockNumber, latestFinalizedBlockNumber);

            blockNumbers.forEach(blockNumber -> evmChainV1Service.processBlockRelated(evmChainV1Strategy, blockNumber, latestFinalizedBlockNumber));

            evmChainV1Service.processDailyLastBlocks(evmChainV1Strategy);
        }

        log.info("*** End process blocks ***");
    }
}
