package com.deukyunlee.indexer.service.token.evm.strategy;

import com.deukyunlee.indexer.model.type.evm.EvmChainType;
import com.deukyunlee.indexer.model.type.evm.EvmTokenType;

import java.math.BigDecimal;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 23.
 */
public interface EvmTokenV1Strategy {
    EvmTokenType getStrategyName();

    void checkContractDeployed(EvmChainType evmChainType, String tokenAddress, long blockNumber);

    BigDecimal getBalance(EvmChainType evmChainType, String address, String tokenAddress, long blockNumber);

    long getDecimals(EvmChainType evmChainType, String tokenAddress, long blockNumber);
}
