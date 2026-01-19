package com.deukyunlee.indexer.model.rpc.evm.result.block;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 26.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EvmGetBlockRpcDetailResult {
    @JsonProperty(value = "hash")
    private String hash;

    @JsonProperty(value ="timestamp")
    private String time;

    @JsonProperty(value ="number")
    private String number;

    @JsonProperty(value ="gasLimit")
    private String gasLimit;

    @JsonProperty(value ="gasUsed")
    private String gasUsed;

    @JsonProperty(value ="difficulty")
    private String difficulty;

    @JsonProperty(value ="size")
    private String size;

    @JsonProperty(value ="baseFeePerGas")
    private String baseFeePerGas;

    @JsonProperty(value ="parentHash")
    private String parentHash;

    @JsonProperty(value ="miner")
    private String miner;

    @JsonProperty(value ="nonce")
    private String nonce;

    @JsonProperty(value ="blobGasUsed")
    private String blobGasUsed;

    @JsonProperty(value ="parentBeaconBlockRoot")
    private String parentBeaconBlockRoot;

    @JsonProperty(value ="transactions")
    private List<EvmGetBlockRpcDetailTransactionResult> transactions;
}
