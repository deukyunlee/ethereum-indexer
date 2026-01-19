package com.deukyunlee.indexer.facade.evm;

import com.deukyunlee.indexer.model.type.evm.EvmChainType;
import com.deukyunlee.indexer.service.chain.evm.EvmChainV1Service;
import com.deukyunlee.indexer.service.chain.evm.factory.EvmChainV1StrategyFactory;
import com.deukyunlee.indexer.service.chain.evm.strategy.EvmChainV1Strategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 28.
 */
@ExtendWith(MockitoExtension.class)
class EvmBlockV1FacadeServiceTest {

    @Mock
    private EvmChainV1StrategyFactory evmChainV1StrategyFactory;

    @Mock
    private EvmChainV1Service evmChainV1Service;

    @Mock
    private EvmChainV1Strategy evmChainV1Strategy;

    @InjectMocks
    private EvmBlockV1FacadeService evmBlockV1FacadeService;

    @BeforeEach
    void setUp() {
        when(evmChainV1StrategyFactory.getStrategy(EvmChainType.ETHEREUM_MAINNET)).thenReturn(evmChainV1Strategy);
    }

    @Test
    void whenNoNewFinalizedBlock_thenDoNothing() {
        when(evmChainV1Service.getLatestBlockNumber(evmChainV1Strategy)).thenReturn(200L); // finalized -> 140
        when(evmChainV1Service.getLatestProcessedBlockNumber(evmChainV1Strategy)).thenReturn(140L);

        evmBlockV1FacadeService.processBlocks(EvmChainType.ETHEREUM_MAINNET);

        verify(evmChainV1Service, never()).processBlockRelated(eq(evmChainV1Strategy), anyLong(), anyLong());
        verify(evmChainV1Service, never()).processDailyLastBlocks(evmChainV1Strategy);
    }

    @Test
    void processNewFinalizedBlocksAndDailyLastBlock() {
        when(evmChainV1Service.getLatestBlockNumber(evmChainV1Strategy)).thenReturn(110L); // finalized -> 50
        when(evmChainV1Service.getLatestProcessedBlockNumber(evmChainV1Strategy)).thenReturn(45L);

        evmBlockV1FacadeService.processBlocks(EvmChainType.ETHEREUM_MAINNET);

        ArgumentCaptor<Long> blockCaptor = ArgumentCaptor.forClass(Long.class);
        verify(evmChainV1Service, times(5))
                .processBlockRelated(eq(evmChainV1Strategy), blockCaptor.capture(), eq(50L));

        assertEquals(List.of(46L, 47L, 48L, 49L, 50L), blockCaptor.getAllValues());
        verify(evmChainV1Service).processDailyLastBlocks(evmChainV1Strategy);
    }
}
