package com.deukyunlee.indexer.model.type.evm;

import com.deukyunlee.indexer.model.type.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 25.
 */
@Getter
@AllArgsConstructor
public enum EvmChainType implements CodeEnum {
    ETHEREUM_MAINNET("https://mainnet.infura.io/v3/%s", "ETH", 18, 60),
    ;

    private final String infuraUrl;
    private final String nativeTokenSymbol;
    private final long nativeTokenDecimal;
    private final long blocksToBeFinalized;

    @Override
    public String getCode() {
        return this.name();
    }
}
