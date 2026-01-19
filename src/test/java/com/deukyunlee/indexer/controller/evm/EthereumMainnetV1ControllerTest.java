package com.deukyunlee.indexer.controller.evm;

import com.deukyunlee.indexer.facade.evm.EvmAccountTokenV1FacadeService;
import com.deukyunlee.indexer.model.account.response.AccountTokenV1Response;
import com.deukyunlee.indexer.model.type.evm.EvmChainType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EthereumMainnetV1Controller.class)
@Import(ExceptionController.class)
class EthereumMainnetV1ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EvmAccountTokenV1FacadeService evmAccountTokenV1FacadeService;

    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Test
    void should_convert_addresses_to_lowercase_before_delegating() throws Exception {
        LocalDate date = LocalDate.of(2024, 12, 30);

        when(evmAccountTokenV1FacadeService.getAccountTokenData(
                eq(EvmChainType.ETHEREUM_MAINNET),
                eq("0xabcdef1234567890abcdef1234567890abcdef12"),
                eq("0xdeadbeef1234567890abcdef1234567890abcdef"),
                eq(date)
        )).thenReturn(new AccountTokenV1Response("10.0000", List.of()));

        mockMvc.perform(get("/v1/ethereum/mainnet/accounts/tokens/transactions")
                        .param("account", "0xABCDEF1234567890ABCDEF1234567890ABCDEF12")
                        .param("tokenAddress", "0xDEADBEEF1234567890ABCDEF1234567890ABCDEF")
                        .param("date", "20241230"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("10.0000"));

        verify(evmAccountTokenV1FacadeService).getAccountTokenData(
                eq(EvmChainType.ETHEREUM_MAINNET),
                eq("0xabcdef1234567890abcdef1234567890abcdef12"),
                eq("0xdeadbeef1234567890abcdef1234567890abcdef"),
                eq(date)
        );
    }

    @Test
    void should_return_422_when_request_is_invalid() throws Exception {
        mockMvc.perform(get("/v1/ethereum/mainnet/accounts/tokens/transactions")
                        .param("account", "INVALID_ACCOUNT")
                        .param("tokenAddress", "INVALID_TOKEN")
                        .param("date", "20241230"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errorCode").exists());

        verifyNoInteractions(evmAccountTokenV1FacadeService);
    }
}

