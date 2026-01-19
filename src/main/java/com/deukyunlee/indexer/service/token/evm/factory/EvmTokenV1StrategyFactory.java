package com.deukyunlee.indexer.service.token.evm.factory;

import com.deukyunlee.indexer.model.type.evm.EvmTokenType;
import com.deukyunlee.indexer.service.token.evm.strategy.EvmTokenV1Strategy;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 28.
 */
@Component
@RequiredArgsConstructor
public class EvmTokenV1StrategyFactory {
    private Map<EvmTokenType, EvmTokenV1Strategy> strategyMap;
    private final List<EvmTokenV1Strategy> strategyList;

    @PostConstruct
    public void afterPropertiesSet() {
        strategyMap = strategyList.stream()
                .collect(Collectors.toMap(EvmTokenV1Strategy::getStrategyName, Function.identity()));
    }

    public EvmTokenV1Strategy getStrategy(EvmTokenType evmTokenType) {
        return strategyMap.get(evmTokenType);
    }
}