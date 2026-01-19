package com.deukyunlee.indexer.service.token.evm.strategy;

import com.deukyunlee.indexer.exception.CustomErrorType;
import com.deukyunlee.indexer.exception.ErrorTypeException;
import com.deukyunlee.indexer.model.rpc.evm.result.GeneralEvmRpcResult;
import com.deukyunlee.indexer.model.type.evm.EvmChainType;
import com.deukyunlee.indexer.model.type.evm.EvmRpcMethodType;
import com.deukyunlee.indexer.model.type.evm.EvmTokenType;
import com.deukyunlee.indexer.spec.RpcSpec;
import com.deukyunlee.indexer.util.NumberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 23.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EvmNativeTokenV1Strategy implements EvmTokenV1Strategy {
    private static final String INITIAL_ID = "0";

    private final RpcSpec rpcSpec;

    @Override
    public EvmTokenType getStrategyName() {
        return EvmTokenType.NATIVE;
    }

    @Override
    public void checkContractDeployed(EvmChainType evmChainType, String tokenAddress, long blockNumber) {

        // For native tokens, assume they exist on the mainnet if the token symbol matches.
        if (!evmChainType.getNativeTokenSymbol().equalsIgnoreCase(tokenAddress)) {
            throw new ErrorTypeException("TOKEN_NOT_EXISTS_ON_CHAIN", CustomErrorType.TOKEN_NOT_EXISTS_ON_CHAIN);
        }
    }

    @Override
    public BigDecimal getBalance(EvmChainType evmChainType, String address, String tokenAddress, long blockNumber) {
        String param = String.format(EvmRpcMethodType.ETH_GET_BALANCE.getParamsFormat(), address, NumberUtil.convertLongToHex(blockNumber));

        String jsonInput = EvmRpcMethodType.getJsonParam(EvmRpcMethodType.ETH_GET_BALANCE, INITIAL_ID, param);

        GeneralEvmRpcResult generalEvmRpcResult = rpcSpec.callPost(evmChainType, jsonInput, EvmRpcMethodType.ETH_GET_BALANCE.getMaxRetryCount(), GeneralEvmRpcResult.class);

        return NumberUtil.convertHexToBigDecimal(generalEvmRpcResult.getResult().toString());
    }

    @Override
    public long getDecimals(EvmChainType evmChainType, String tokenAddress, long blockNumber) {
        return evmChainType.getNativeTokenDecimal();
    }
}