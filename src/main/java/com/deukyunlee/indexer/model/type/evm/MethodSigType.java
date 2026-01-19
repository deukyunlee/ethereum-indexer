package com.deukyunlee.indexer.model.type.evm;

import com.deukyunlee.indexer.model.type.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 26.
 */
@Getter
@AllArgsConstructor
public enum MethodSigType implements CodeEnum {
    BALANCE_OF("0x70a08231"),
    DECIMALS("0x313ce567")
    ;

    private final String methodSig;

    @Override
    public String getCode() {
        return this.name();
    }
}
