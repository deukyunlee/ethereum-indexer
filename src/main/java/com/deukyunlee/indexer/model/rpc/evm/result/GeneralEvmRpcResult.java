package com.deukyunlee.indexer.model.rpc.evm.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 23.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GeneralEvmRpcResult {
   private String jsonrpc;
   private String id;
   private Object result;
}