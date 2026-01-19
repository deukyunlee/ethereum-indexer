package com.deukyunlee.indexer.service.chain.evm.factory;

import com.deukyunlee.indexer.model.type.evm.EvmChainType;
import com.deukyunlee.indexer.service.chain.evm.strategy.EvmChainV1Strategy;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 25.
 */
@Component
@RequiredArgsConstructor
public class EvmChainV1StrategyFactory {
    private Map<EvmChainType, EvmChainV1Strategy> strategyMap;
    private final List<EvmChainV1Strategy> strategyList;

    @PostConstruct
    public void afterPropertiesSet() {
        strategyMap = strategyList.stream()
                .collect(Collectors.toMap(EvmChainV1Strategy::getStrategyName, Function.identity()));
    }

    public EvmChainV1Strategy getStrategy(EvmChainType evmChainType) {
        return strategyMap.get(evmChainType);
    }
}