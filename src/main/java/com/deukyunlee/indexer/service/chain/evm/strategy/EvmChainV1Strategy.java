package com.deukyunlee.indexer.service.chain.evm.strategy;

import com.deukyunlee.indexer.model.account.dto.AccountTokenTransferDto;
import com.deukyunlee.indexer.model.rpc.evm.result.block.EvmBlockReceiptsRpcDetailResult;
import com.deukyunlee.indexer.model.rpc.evm.result.block.EvmGetBlockRpcDetailResult;
import com.deukyunlee.indexer.model.rpc.evm.result.block.EvmGetBlockRpcDetailTransactionResult;
import com.deukyunlee.indexer.model.type.evm.EvmChainType;

import java.time.Instant;
import java.util.List;


/**
 * Created by dufqkd1004@naver.com on 2024. 12. 25.
 */
public interface EvmChainV1Strategy {
    EvmChainType getStrategyName();

    long getDailyLastBlockNumber(Instant date);

    long getLatestProcessedBlockNumber();

    long getLatestBlockNumber();

    void storeBlocks(EvmGetBlockRpcDetailResult evmGetBlockRpcDetailResult);

    List<AccountTokenTransferDto> getErc20Transfers(Instant date, String accountAddress, String tokenAddress);

    List<AccountTokenTransferDto> getTransactions(Instant date, String accountAddress);

    void storeTransactions(long blockNumber, Instant blockTime, List<EvmBlockReceiptsRpcDetailResult> transactionReceipts, List<EvmGetBlockRpcDetailTransactionResult> transactionDetails);

    void storeLogs(long blockNumber, Instant blockTime, List<EvmBlockReceiptsRpcDetailResult> transactionReceipts);

    void processDailyLastBlocks();
}
