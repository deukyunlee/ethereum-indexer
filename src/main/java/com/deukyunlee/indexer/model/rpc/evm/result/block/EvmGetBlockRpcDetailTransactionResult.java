package com.deukyunlee.indexer.model.rpc.evm.result.block;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 26.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EvmGetBlockRpcDetailTransactionResult {

    @JsonProperty(value = "from")
    private String from;

    @JsonProperty(value = "to")
    private String to;

    @JsonProperty(value = "gas")
    private String gasLimit;

    @JsonProperty(value = "gasPrice")
    private String gasPrice;

    @JsonProperty(value = "maxFeePerGas")
    private String maxFeePerGas;

    @JsonProperty(value = "maxPriorityFeePerGas")
    private String maxPriorityFeePerGas;

    @JsonProperty(value = "blockHash")
    private String blockHash;

    @JsonProperty(value = "hash")
    private String hash;

    @JsonProperty(value = "input")
    private String input;

    @JsonProperty(value = "nonce")
    private String nonce;

    @JsonProperty(value = "transactionIndex")
    private String transactionIndex;

    @JsonProperty(value = "value")
    private String value;

    @JsonProperty(value = "type")
    private String type;
}
