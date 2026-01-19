package com.deukyunlee.indexer.service.chain.evm.strategy;

import com.deukyunlee.indexer.entity.evm.ethereum.EthereumBlockEntity;
import com.deukyunlee.indexer.entity.evm.ethereum.EthereumDailyBlockEntity;
import com.deukyunlee.indexer.entity.evm.ethereum.EthereumErc20TransferEntity;
import com.deukyunlee.indexer.entity.evm.ethereum.EthereumLogEntity;
import com.deukyunlee.indexer.entity.evm.ethereum.EthereumTransactionEntity;
import com.deukyunlee.indexer.exception.CustomErrorType;
import com.deukyunlee.indexer.exception.ErrorTypeException;
import com.deukyunlee.indexer.model.account.dto.AccountTokenTransferDto;
import com.deukyunlee.indexer.model.rpc.evm.result.block.EvmBlockReceiptsRpcDetailResult;
import com.deukyunlee.indexer.model.rpc.evm.result.block.EvmGetBlockRpcDetailResult;
import com.deukyunlee.indexer.model.rpc.evm.result.block.EvmGetBlockRpcDetailTransactionResult;
import com.deukyunlee.indexer.model.rpc.evm.result.GeneralEvmRpcResult;
import com.deukyunlee.indexer.model.type.evm.EvmChainType;
import com.deukyunlee.indexer.model.type.evm.EvmRpcMethodType;
import com.deukyunlee.indexer.repository.evm.ethereum.EthereumBlockRepository;
import com.deukyunlee.indexer.repository.evm.ethereum.EthereumDailyBlockRepository;
import com.deukyunlee.indexer.repository.evm.ethereum.EthereumErc20TransferRepository;
import com.deukyunlee.indexer.repository.evm.ethereum.EthereumLogRepository;
import com.deukyunlee.indexer.repository.evm.ethereum.EthereumTransactionRepository;
import com.deukyunlee.indexer.spec.RpcSpec;
import com.deukyunlee.indexer.util.NumberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Created by dufqkd1004@naver.com on 2024. 12. 25.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EvmEthereumMainnetChainV1Strategy implements EvmChainV1Strategy {
    private static final String INITIAL_ID = "0";
    private static final String Erc20TransferEvent = "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef";
    private static final boolean transctionSuccess = true;

    private final RpcSpec rpcSpec;
    private final EthereumBlockRepository ethereumBlockRepository;
    private final EthereumTransactionRepository ethereumTransactionRepository;
    private final EthereumDailyBlockRepository ethereumDailyBlockRepository;
    private final EthereumLogRepository ethereumLogRepository;
    private final EthereumErc20TransferRepository ethereumErc20TransferRepository;

    @Override
    public EvmChainType getStrategyName() {
        return EvmChainType.ETHEREUM_MAINNET;
    }

    @Override
    public long getDailyLastBlockNumber(Instant date) {
        return ethereumDailyBlockRepository.findByDate(date)
                .map(EthereumDailyBlockEntity::getBlockNumber)
                .orElseThrow(() -> new ErrorTypeException("DAILY_LAST_BLOCK_NOT_FOUND", CustomErrorType.DAILY_LAST_BLOCK_NOT_FOUND));
    }

    @Override
    public long getLatestProcessedBlockNumber() {
        return ethereumBlockRepository.findFirstByOrderByNumberDesc()
                .map(EthereumBlockEntity::getNumber)
                .orElse(0L);
    }

    @Override
    public long getLatestBlockNumber() {
        String param = String.format(EvmRpcMethodType.ETH_BLOCK_NUMBER.getParamsFormat());

        String jsonInput = EvmRpcMethodType.getJsonParam(EvmRpcMethodType.ETH_BLOCK_NUMBER, INITIAL_ID, param);

        GeneralEvmRpcResult generalEvmRpcResult = rpcSpec.callPost(EvmChainType.ETHEREUM_MAINNET, jsonInput, EvmRpcMethodType.ETH_BLOCK_NUMBER.getMaxRetryCount(), GeneralEvmRpcResult.class);

        return NumberUtil.convertHexToLong(generalEvmRpcResult.getResult().toString());
    }

    @Override
    public void storeBlocks(EvmGetBlockRpcDetailResult evmGetBlockRpcDetailResult) {
        EthereumBlockEntity ethereumBlockEntity = new EthereumBlockEntity(evmGetBlockRpcDetailResult);

        ethereumBlockRepository.save(ethereumBlockEntity);
    }

    @Override
    public List<AccountTokenTransferDto> getErc20Transfers(Instant date, String accountAddress, String tokenAddress) {

        List<EthereumErc20TransferEntity> fromTransfers = ethereumErc20TransferRepository.findAllByBlockDateAndContractAddressAndFrom(date, tokenAddress, accountAddress);

        List<EthereumErc20TransferEntity> toTransfers = ethereumErc20TransferRepository.findAllByBlockDateAndContractAddressAndTo(date, tokenAddress, accountAddress);

        List<EthereumErc20TransferEntity> combinedTransfers = new ArrayList<>();
        combinedTransfers.addAll(fromTransfers);
        combinedTransfers.addAll(toTransfers);


        return combinedTransfers.stream()
                .distinct()
                .map(AccountTokenTransferDto::new)
                .toList();
    }

    @Override
    public List<AccountTokenTransferDto> getTransactions(Instant date, String accountAddress) {
        List<EthereumTransactionEntity> fromTransactions = ethereumTransactionRepository.findAllByBlockDateAndSuccessAndFrom(date, transctionSuccess, accountAddress);

        List<EthereumTransactionEntity> toTransactions = ethereumTransactionRepository.findAllByBlockDateAndSuccessAndTo(date, transctionSuccess, accountAddress);

        List<EthereumTransactionEntity> combinedTransactions = new ArrayList<>();
        combinedTransactions.addAll(fromTransactions);
        combinedTransactions.addAll(toTransactions);


        return combinedTransactions.stream()
                .distinct()
                .map(AccountTokenTransferDto::new)
                .toList();

    }

    @Override
    public void storeTransactions(long blockNumber, Instant blockTime, List<EvmBlockReceiptsRpcDetailResult> transactionReceipts, List<EvmGetBlockRpcDetailTransactionResult> transactionDetails) {

        Map<String, EvmGetBlockRpcDetailTransactionResult> transactionDetailByHash = transactionDetails.stream()
                .collect(Collectors.toMap(EvmGetBlockRpcDetailTransactionResult::getHash, Function.identity()));

        List<EthereumTransactionEntity> ethereumTransactionEntities = transactionReceipts.stream()
                .map(transactionReceipt -> new EthereumTransactionEntity(blockNumber, blockTime, transactionReceipt, transactionDetailByHash.get(transactionReceipt.getTransactionHash())))
                .toList();

        ethereumTransactionRepository.saveAll(ethereumTransactionEntities);
    }

    @Override
    public void storeLogs(long blockNumber, Instant blockTime, List<EvmBlockReceiptsRpcDetailResult> transactionReceipts) {
        List<EthereumLogEntity> ethereumLogEntities = transactionReceipts.stream()
                .flatMap(transactionReceipt ->
                        transactionReceipt.getLogs().stream()
                                .map(logData -> new EthereumLogEntity(blockNumber, blockTime, transactionReceipt, logData))).toList();

        ethereumLogRepository.saveAll(ethereumLogEntities);

        List<EthereumErc20TransferEntity> ethereumErc20TransferEntities = ethereumLogEntities.stream()
                .filter(logEntity -> Erc20TransferEvent.equalsIgnoreCase(logEntity.getTopic0()) && logEntity.getTopic1() != null && logEntity.getTopic2() != null)
                .map(EthereumErc20TransferEntity::new)
                .toList();

        ethereumErc20TransferRepository.saveAll(ethereumErc20TransferEntities);
    }

    @Override
    public void processDailyLastBlocks() {
        EthereumBlockEntity firstBlockEntity = ethereumBlockRepository.findFirstByOrderByNumberAsc()
                .orElseThrow(() -> new ErrorTypeException("FIRST_BLOCK_NOT_FOUND", CustomErrorType.FIRST_BLOCK_NOT_FOUND));

        EthereumBlockEntity lastBlockEntity = ethereumBlockRepository.findFirstByOrderByNumberDesc()
                .orElseThrow(() -> new ErrorTypeException("LAST_BLOCK_NOT_FOUND", CustomErrorType.LAST_BLOCK_NOT_FOUND));

        Instant startDate = getStartDate(firstBlockEntity);
        Instant endDate = lastBlockEntity.getDate();

        processBlocksInRange(startDate, endDate);
    }

    private Instant getStartDate(EthereumBlockEntity firstBlockEntity) {
        return ethereumDailyBlockRepository.findFirstByOrderByDateDesc()
                .map(EthereumDailyBlockEntity::getDate)
                .orElse(firstBlockEntity.getDate())
                .plus(1, ChronoUnit.DAYS);
    }

    private void processBlocksInRange(Instant startDate, Instant endDate) {
        List<EthereumDailyBlockEntity> ethereumDailyBlockEntities = new ArrayList<>();
        while (startDate.isBefore(endDate)) {

            EthereumDailyBlockEntity dailyBlockEntity = ethereumBlockRepository.findFirstByDateOrderByTimeDesc(startDate)
                    .map(EthereumDailyBlockEntity::new)
                    .orElseThrow(() -> new ErrorTypeException("BLOCK_NOT_FOUND_FOR_DATE", CustomErrorType.BLOCK_NOT_FOUND_FOR_DATE));

            ethereumDailyBlockEntities.add(dailyBlockEntity);
            startDate = startDate.plus(1, ChronoUnit.DAYS);
        }

        ethereumDailyBlockRepository.saveAll(ethereumDailyBlockEntities);
    }
}
