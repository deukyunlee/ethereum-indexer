package com.deukyunlee.indexer.service.chain.evm;

import com.deukyunlee.indexer.exception.CustomErrorType;
import com.deukyunlee.indexer.exception.ErrorTypeException;
import com.deukyunlee.indexer.model.account.dto.AccountTokenTransferDto;
import com.deukyunlee.indexer.model.rpc.evm.result.block.EvmGetBlockRpcResult;
import com.deukyunlee.indexer.model.rpc.evm.result.block.EvmBlockReceiptsRpcResult;
import com.deukyunlee.indexer.model.type.evm.EvmChainType;
import com.deukyunlee.indexer.model.type.evm.EvmRpcMethodType;
import com.deukyunlee.indexer.service.chain.evm.strategy.EvmChainV1Strategy;
import com.deukyunlee.indexer.spec.RpcSpec;
import com.deukyunlee.indexer.util.NumberUtil;
import com.deukyunlee.indexer.util.TimeUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;


/**
 * Created by dufqkd1004@naver.com on 2024. 12. 25.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EvmChainV1Service {
    private static final boolean INCLUDE_TX_DETAIL = true;
    private final RpcSpec rpcSpec;

    public long getDailyLastBlockNumber(EvmChainV1Strategy strategy, Instant date) {
        return strategy.getDailyLastBlockNumber(date);
    }

    public long getLatestProcessedBlockNumber(EvmChainV1Strategy strategy) {
        return strategy.getLatestProcessedBlockNumber();
    }

    public long getLatestBlockNumber(EvmChainV1Strategy strategy) {
        return strategy.getLatestBlockNumber();
    }

    public void processDailyLastBlocks(EvmChainV1Strategy strategy) {
        strategy.processDailyLastBlocks();
    }

    public List<AccountTokenTransferDto> getErc20Transfers(EvmChainV1Strategy strategy, Instant date, String accountAddress, String tokenAddress) {
        return strategy.getErc20Transfers(date, accountAddress, tokenAddress);
    }

    public List<AccountTokenTransferDto> getTransactions(EvmChainV1Strategy strategy, Instant date, String accountAddress) {
        return strategy.getTransactions(date, accountAddress);
    }

    @Transactional
    public void processBlockRelated(EvmChainV1Strategy strategy, long blockNumber, long endBlockNumber) {

        log.info("Process block related block number : {}/{}", blockNumber, endBlockNumber);

        String getBlockParam = String.format(EvmRpcMethodType.ETH_GET_BLOCK_BY_NUMBER.getParamsFormat(), NumberUtil.convertLongToHex(blockNumber), INCLUDE_TX_DETAIL);
        String getBlockJsonInput = EvmRpcMethodType.getJsonParam(EvmRpcMethodType.ETH_GET_BLOCK_BY_NUMBER, String.valueOf(blockNumber), getBlockParam);
        EvmGetBlockRpcResult evmGetBlockRpcResult = rpcSpec.callPost(EvmChainType.ETHEREUM_MAINNET, getBlockJsonInput, EvmRpcMethodType.ETH_GET_BLOCK_BY_NUMBER.getMaxRetryCount(), EvmGetBlockRpcResult.class);

        String getBlockReceiptsParam = String.format(EvmRpcMethodType.ETH_GET_BLOCK_RECEIPTS.getParamsFormat(), NumberUtil.convertLongToHex(blockNumber));
        String getBlockReceiptsJsonInput = EvmRpcMethodType.getJsonParam(EvmRpcMethodType.ETH_GET_BLOCK_RECEIPTS, String.valueOf(blockNumber), getBlockReceiptsParam);
        EvmBlockReceiptsRpcResult evmBlockReceiptsRpcResult = rpcSpec.callPost(strategy.getStrategyName(), getBlockReceiptsJsonInput, EvmRpcMethodType.ETH_GET_BLOCK_RECEIPTS.getMaxRetryCount(), EvmBlockReceiptsRpcResult.class);

        Instant blockTime = TimeUtil.convertHexToInstant(evmGetBlockRpcResult.getResult().getTime());

        strategy.storeBlocks(evmGetBlockRpcResult.getResult());

        strategy.storeTransactions(blockNumber, blockTime, evmBlockReceiptsRpcResult.getResult(), evmGetBlockRpcResult.getResult().getTransactions());

        strategy.storeLogs(blockNumber, blockTime, evmBlockReceiptsRpcResult.getResult());

        log.info("End block related block number : {}/{}", blockNumber, endBlockNumber);
    }
}

