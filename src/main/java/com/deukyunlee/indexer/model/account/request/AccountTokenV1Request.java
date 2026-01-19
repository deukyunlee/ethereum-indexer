package com.deukyunlee.indexer.model.account.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 28.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountTokenV1Request {
    @NotBlank(message = "Account address is required")
    @Pattern(regexp = "^0x[a-fA-F0-9]{40}$", message = "Invalid Ethereum account address")
    private String account;

    @NotBlank(message = "Token address is required")
    @Pattern(regexp = "^0x[a-fA-F0-9]{40}$|^ETH$", message = "Invalid token address. Must be ETH or a valid address")
    private String tokenAddress;

    @PastOrPresent(message = "Date must be in the past or present")
    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate date;
}