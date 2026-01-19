package com.deukyunlee.indexer.model.rpc.evm.result.block;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 26.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EvmBlockReceiptsRpcResult {
    private String jsonrpc;
    private String id;
    private List<EvmBlockReceiptsRpcDetailResult> result;
}
