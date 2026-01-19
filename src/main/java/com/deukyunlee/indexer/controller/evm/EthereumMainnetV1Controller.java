package com.deukyunlee.indexer.controller.evm;

import com.deukyunlee.indexer.facade.evm.EvmAccountTokenV1FacadeService;
import com.deukyunlee.indexer.model.account.request.AccountTokenV1Request;
import com.deukyunlee.indexer.model.account.response.AccountTokenV1Response;
import com.deukyunlee.indexer.model.type.evm.EvmChainType;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 22.
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "Ethereum Mainnet V1", description = "이더리움 메인넷관련 API V1")
@RequestMapping("/v1/ethereum/mainnet")
public class EthereumMainnetV1Controller {
    private final EvmAccountTokenV1FacadeService evmAccountTokenV1FacadeService;

    @GetMapping("/accounts/tokens/transactions")
    public AccountTokenV1Response getAccountTokenData(@ModelAttribute @Valid AccountTokenV1Request request) {
        return evmAccountTokenV1FacadeService.getAccountTokenData(
                EvmChainType.ETHEREUM_MAINNET,
                request.getAccount().toLowerCase(),
                request.getTokenAddress().toLowerCase(),
                request.getDate()
        );
    }
}
