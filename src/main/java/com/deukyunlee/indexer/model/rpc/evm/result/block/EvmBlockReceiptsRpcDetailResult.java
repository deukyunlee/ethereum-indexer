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
public class EvmBlockReceiptsRpcDetailResult {
    @JsonProperty(value = "blockHash")
    private String blockHash;

    @JsonProperty(value = "blockNumber")
    private String blockNumber;

    @JsonProperty(value = "contractAddress")
    private String contractAddress;

    @JsonProperty(value = "cumulativeGasUsed")
    private String cumulativeGasUsed;

    @JsonProperty(value = "effectiveGasPrice")
    private String effectiveGasPrice;

    @JsonProperty(value = "from")
    private String from;

    @JsonProperty(value = "gasUsed")
    private String gasUsed;

    @JsonProperty(value = "logsBloom")
    private String logsBloom;

    @JsonProperty(value = "status")
    private String status;

    @JsonProperty(value = "to")
    private String to;

    @JsonProperty(value = "transactionHash")
    private String transactionHash;

    @JsonProperty(value = "transactionIndex")
    private String transactionIndex;

    @JsonProperty(value = "type")
    private String type;

    @JsonProperty(value = "logs")
    private List<EvmBlockReceiptsRpcDetailLogResult> logs;
}
