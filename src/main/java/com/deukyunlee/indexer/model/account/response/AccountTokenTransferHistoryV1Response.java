package com.deukyunlee.indexer.model.account.response;

import com.deukyunlee.indexer.model.account.dto.AccountTokenTransferDto;
import com.deukyunlee.indexer.util.NumberUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 27.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountTokenTransferHistoryV1Response {
    @JsonProperty(required = true, value = "from")
    private String from;

    @JsonProperty(required = true, value = "to")
    private String to;

    @JsonProperty(required = true, value = "value")
    private String value;

    @JsonProperty(required = true, value = "timestamp")
    private long timestamp;

    public AccountTokenTransferHistoryV1Response(long decimal, AccountTokenTransferDto accountTokenTransferDto) {
        BigDecimal transferValue = new BigDecimal(accountTokenTransferDto.getValue());

        this.from = accountTokenTransferDto.getFrom();
        this.to = accountTokenTransferDto.getTo();
        this.value = (BigDecimal.ZERO.compareTo(transferValue) == 0) ?
                transferValue.toPlainString() :
                NumberUtil.getBalanceDividedByDecimal(transferValue, decimal)
                        .setScale(4, RoundingMode.DOWN).toPlainString();

        this.timestamp = accountTokenTransferDto.getTimestamp().getEpochSecond();
    }
}
