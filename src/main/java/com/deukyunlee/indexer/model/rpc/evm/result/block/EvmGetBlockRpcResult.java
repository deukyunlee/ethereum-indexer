package com.deukyunlee.indexer.model.rpc.evm.result.block;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 26.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EvmGetBlockRpcResult {
    private String jsonrpc;
    private String id;
    private EvmGetBlockRpcDetailResult result;
}
