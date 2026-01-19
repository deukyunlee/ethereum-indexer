package com.deukyunlee.indexer.spec;

import com.deukyunlee.indexer.model.type.evm.EvmChainType;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 25.
 */
public interface RpcSpec {
    <T> T callPost(EvmChainType evmChainType, String jsonInput, int maxRetryCount, Class<T> classes);
}
