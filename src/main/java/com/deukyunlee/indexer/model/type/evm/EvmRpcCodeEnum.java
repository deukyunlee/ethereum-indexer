package com.deukyunlee.indexer.model.type.evm;


import com.deukyunlee.indexer.model.type.CodeEnum;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 22.
 */
public interface EvmRpcCodeEnum extends CodeEnum {
    String DEFAULT_JSON_PARAM = "{\"jsonrpc\":\"2.0\",\"method\":\"%s\",\"id\":%s,\"params\":[%s]}";
}
