package com.deukyunlee.indexer.service.token.evm.strategy;

import com.deukyunlee.indexer.exception.CustomErrorType;
import com.deukyunlee.indexer.exception.ErrorTypeException;
import com.deukyunlee.indexer.model.rpc.evm.result.GeneralEvmRpcResult;
import com.deukyunlee.indexer.model.type.evm.EvmChainType;
import com.deukyunlee.indexer.model.type.evm.EvmRpcMethodType;
import com.deukyunlee.indexer.model.type.evm.EvmTokenType;
import com.deukyunlee.indexer.model.type.evm.MethodSigType;
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
public class EvmErc20TokenV1Strategy implements EvmTokenV1Strategy {
    private static final String ZERO_PADDING = "000000000000000000000000";
    private static final String ADDRESS_PREFIX = "0x";
    private static final String INITIAL_ID = "0";

    private final RpcSpec rpcSpec;

    @Override
    public EvmTokenType getStrategyName() {
        return EvmTokenType.ERC_20;
    }

    @Override
    public void checkContractDeployed(EvmChainType evmChainType, String tokenAddress, long blockNumber) {

        String hexBlockNumber = NumberUtil.convertLongToHex(blockNumber);

        String param = String.format(EvmRpcMethodType.ETH_GET_CODE.getParamsFormat(), tokenAddress, hexBlockNumber);

        String jsonInput = EvmRpcMethodType.getJsonParam(EvmRpcMethodType.ETH_GET_CODE, INITIAL_ID, param);

        GeneralEvmRpcResult generalEvmRpcResult = rpcSpec.callPost(evmChainType, jsonInput, EvmRpcMethodType.ETH_GET_CODE.getMaxRetryCount(), GeneralEvmRpcResult.class);

        // The eth_getCode method returns the bytecode of a given address.
        // - If the result is "0x", it means no code is deployed at the address.
        // - If the result is not "0x", it indicates that the address is a contract with deployed code.
        if (ADDRESS_PREFIX.equalsIgnoreCase(generalEvmRpcResult.getResult().toString())) {
            throw new ErrorTypeException("TOKEN_NOT_EXISTS_ON_CHAIN", CustomErrorType.TOKEN_NOT_EXISTS_ON_CHAIN);
        }
    }

    @Override
    public BigDecimal getBalance(EvmChainType evmChainType, String address, String tokenAddress, long blockNumber) {
        String hexBlockNumber = NumberUtil.convertLongToHex(blockNumber);

        String addressWithoutPrefix = address.startsWith(ADDRESS_PREFIX) ? address.substring(2) : address;

        String param = String.format(EvmRpcMethodType.ETH_CALL.getParamsFormat(), tokenAddress, MethodSigType.BALANCE_OF.getMethodSig() + ZERO_PADDING + addressWithoutPrefix, hexBlockNumber);

        String jsonInput = EvmRpcMethodType.getJsonParam(EvmRpcMethodType.ETH_CALL, INITIAL_ID, param);

        GeneralEvmRpcResult generalEvmRpcResult = rpcSpec.callPost(evmChainType, jsonInput, EvmRpcMethodType.ETH_CALL.getMaxRetryCount(), GeneralEvmRpcResult.class);

        return NumberUtil.convertHexToBigDecimal(generalEvmRpcResult.getResult().toString());
    }

    @Override
    public long getDecimals(EvmChainType evmChainType, String tokenAddress, long blockNumber) {
        String hexBlockNumber = NumberUtil.convertLongToHex(blockNumber);

        String param = String.format(EvmRpcMethodType.ETH_CALL.getParamsFormat(), tokenAddress, MethodSigType.DECIMALS.getMethodSig(), hexBlockNumber);

        String jsonInput = EvmRpcMethodType.getJsonParam(EvmRpcMethodType.ETH_CALL, INITIAL_ID, param);

        GeneralEvmRpcResult generalEvmRpcResult = rpcSpec.callPost(evmChainType, jsonInput, EvmRpcMethodType.ETH_CALL.getMaxRetryCount(), GeneralEvmRpcResult.class);

        return NumberUtil.convertHexToLong(generalEvmRpcResult.getResult().toString());
    }
}
