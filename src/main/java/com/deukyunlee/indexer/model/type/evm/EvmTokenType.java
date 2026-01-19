package com.deukyunlee.indexer.model.type.evm;


import com.deukyunlee.indexer.model.type.CodeEnum;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 23.
 */
public enum EvmTokenType implements CodeEnum {
    NATIVE,
    ERC_20;

    @Override
    public String getCode() {
        return this.name();
    }

    public static EvmTokenType getTokenAddress(EvmChainType evmChainType, String tokenAddress) {
        if (evmChainType.getNativeTokenSymbol().equalsIgnoreCase(tokenAddress)) {
            return EvmTokenType.NATIVE;
        } else {
            return EvmTokenType.ERC_20;
        }
    }
}
