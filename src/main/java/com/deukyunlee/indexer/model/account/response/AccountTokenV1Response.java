package com.deukyunlee.indexer.model.account.response;

import com.deukyunlee.indexer.model.account.dto.AccountTokenTransferDto;
import com.deukyunlee.indexer.util.NumberUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 22.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonView()
public class AccountTokenV1Response {
    @JsonProperty(required = true, value = "balance")
    private String balance;

    @JsonProperty(required = true, value = "transfer_history")
    private List<AccountTokenTransferHistoryV1Response> transferHistories;

    public AccountTokenV1Response(long decimal, BigDecimal balance, List<AccountTokenTransferDto> transferHistoryDtos) {
        this.balance = (BigDecimal.ZERO.compareTo(balance) == 0) ?
                balance.toPlainString() :
                NumberUtil.getBalanceDividedByDecimal(balance, decimal)
                        .setScale(4, RoundingMode.DOWN).toPlainString();

        this.transferHistories = transferHistoryDtos.stream()
                .map(transferHistoryDto -> new AccountTokenTransferHistoryV1Response(decimal, transferHistoryDto))
                .sorted(Comparator.comparing(AccountTokenTransferHistoryV1Response::getTimestamp))
                .toList();
    }
}
