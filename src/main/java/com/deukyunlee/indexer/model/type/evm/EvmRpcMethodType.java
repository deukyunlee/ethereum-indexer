package com.deukyunlee.indexer.model.type.evm;

import com.deukyunlee.indexer.exception.CustomErrorType;
import com.deukyunlee.indexer.exception.ErrorTypeException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 22.
 */
@Getter
@AllArgsConstructor
public enum EvmRpcMethodType implements EvmRpcCodeEnum {
    ETH_GET_TRANSACTION_RECEIPT("eth_getTransactionReceipt", "\"%s\"", 5),
    ETH_CALL("eth_call", "{\"to\":\"%s\",\"data\":\"%s\"},\"%s\"", 5),
    ETH_GET_BLOCK_BY_NUMBER("eth_getBlockByNumber", "\"%s\", %b", 5),
    ETH_GET_BLOCK_RECEIPTS("eth_getBlockReceipts", "\"%s\"", 5),
    ETH_GET_CODE("eth_getCode", "\"%s\", \"%s\"", 5),
    ETH_GET_BALANCE("eth_getBalance", "\"%s\", \"%s\"", 5),
    ETH_BLOCK_NUMBER("eth_blockNumber", "", 5),

    ;

    private String code;
    private String paramsFormat;
    private int maxRetryCount;

    @Override
    public String getCode() {
        return code;
    }

    public static String getJsonParam(EvmRpcMethodType type, String id, String params) {
        for (EvmRpcMethodType value : values()) {
            if (value == type) {
                return String.format(DEFAULT_JSON_PARAM, value.getCode(), id, params);
            }
        }
        throw new ErrorTypeException("WRONG_RPC_METHOD_TYPE", CustomErrorType.WRONG_RPC_METHOD_TYPE);
    }

}

