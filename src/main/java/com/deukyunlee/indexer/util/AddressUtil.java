package com.deukyunlee.indexer.util;

import com.deukyunlee.indexer.exception.CustomErrorType;
import com.deukyunlee.indexer.exception.ErrorTypeException;
import com.deukyunlee.indexer.model.type.evm.EvmChainType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 22.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressUtil {
    private static final String ZERO_X = "0x";
    public static final int ERC_20_TRANSFER_ADDRESS_TOPIC_BEGIN_INDEX = 24;

    /**
     * Removes zero padding from an Ethereum address or topic value,
     * but ensures the length remains correct for valid Ethereum addresses.
     *
     * @param paddedValue the padded topic value (e.g., "0x0000000000000000000000001a63b9aed91e03d77944d6f16c81346543bbbded")
     * @return the unpadded address or value (e.g., "0x1a63b9aed91e03d77944d6f16c81346543bbbded")
     */
    public static String removeZeroPadding(String paddedValue, int beginIndex) {
        if (paddedValue == null || !paddedValue.startsWith(ZERO_X)) {
            log.error("Invalid padded value: {}", paddedValue);
            throw new ErrorTypeException("WRONG_TOPIC_VALUE", CustomErrorType.WRONG_TOPIC_VALUE);
        }

        String withoutPrefix = paddedValue.substring(2);

        return ZERO_X + withoutPrefix.substring(beginIndex);
    }
}
