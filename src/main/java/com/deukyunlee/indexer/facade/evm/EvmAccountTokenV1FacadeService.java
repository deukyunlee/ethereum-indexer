package com.deukyunlee.indexer.facade.evm;

import com.deukyunlee.indexer.config.redis.RedisPrefix;
import com.deukyunlee.indexer.model.account.dto.AccountTokenTransferDto;
import com.deukyunlee.indexer.model.account.response.AccountTokenV1Response;
import com.deukyunlee.indexer.model.type.evm.EvmChainType;
import com.deukyunlee.indexer.model.type.evm.EvmTokenType;
import com.deukyunlee.indexer.service.chain.evm.EvmChainV1Service;
import com.deukyunlee.indexer.service.chain.evm.factory.EvmChainV1StrategyFactory;
import com.deukyunlee.indexer.service.chain.evm.strategy.EvmChainV1Strategy;
import com.deukyunlee.indexer.service.token.evm.EvmTokenV1Service;
import com.deukyunlee.indexer.service.token.evm.factory.EvmTokenV1StrategyFactory;
import com.deukyunlee.indexer.service.token.evm.strategy.EvmTokenV1Strategy;
import com.deukyunlee.indexer.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 22.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EvmAccountTokenV1FacadeService {
    private final EvmTokenV1StrategyFactory evmTokenV1StrategyFactory;
    private final EvmTokenV1Service evmTokenV1Service;
    private final EvmChainV1StrategyFactory evmChainV1StrategyFactory;
    private final EvmChainV1Service evmChainV1Service;

    @Cacheable(value = RedisPrefix.ACCOUNT_TOKEN_DATA, key = "#evmChainType + ':' + #account + ':' + #tokenAddress + ':' + #date", unless = "#result == null")
    public AccountTokenV1Response getAccountTokenData(EvmChainType evmChainType, String account, String tokenAddress, LocalDate date) {

        Instant dateInstant = TimeUtil.convertLocalDateToInstant(date);

        // Retrieve the appropriate strategy for the specified EVM-compatible chain type (e.g. ETHEREUM_MAINNET, POLYGON_MAINNET)
        EvmChainV1Strategy evmChainV1Strategy = evmChainV1StrategyFactory.getStrategy(evmChainType);

        long blockNumber = evmChainV1Service.getDailyLastBlockNumber(evmChainV1Strategy, dateInstant);

        EvmTokenType evmTokenType = EvmTokenType.getTokenAddress(evmChainType, tokenAddress);

        // Retrieve a strategy for the specified token type (e.g. ERC20, NATIVE_TOKEN)
        EvmTokenV1Strategy evmTokenV1Strategy = evmTokenV1StrategyFactory.getStrategy(evmTokenType);

        evmTokenV1Service.checkContractDeployed(evmTokenV1Strategy, evmChainType, tokenAddress, blockNumber);

        BigDecimal balance = evmTokenV1Service.getBalance(evmTokenV1Strategy, evmChainType, account, tokenAddress, blockNumber);

        long decimal = evmTokenV1Service.getDecimals(evmTokenV1Strategy, evmChainType, tokenAddress, blockNumber);

        List<AccountTokenTransferDto> accountTokenTransfers = EvmTokenType.NATIVE == evmTokenType ?
                evmChainV1Service.getTransactions(evmChainV1Strategy, dateInstant, account) :
                evmChainV1Service.getErc20Transfers(evmChainV1Strategy, dateInstant, account, tokenAddress);

        return new AccountTokenV1Response(decimal, balance, accountTokenTransfers);
    }
}