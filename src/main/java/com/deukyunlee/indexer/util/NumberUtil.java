package com.deukyunlee.indexer.util;

import com.deukyunlee.indexer.exception.CustomErrorType;
import com.deukyunlee.indexer.exception.ErrorTypeException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 23.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class NumberUtil {
    private static final String ZERO_X = "0x";
    private static final String ZERO = "0";

    /**
     * Converts a sanitized hexadecimal string to a BigDecimal value.
     *
     * @param hexString the hexadecimal string to convert
     * @return the BigDecimal representation of the hexadecimal string
     * @throws ErrorTypeException if the hex string is invalid or conversion fails
     */
    public static BigDecimal convertHexToBigDecimal(String hexString) {
        String sanitizedHexString = sanitizeHexString(hexString);

        BigDecimal result;

        try {
            result = new BigDecimal(new BigInteger(sanitizedHexString, 16));
        } catch (NumberFormatException | ArithmeticException e) {
            log.error(e.getMessage(), e);
            throw new ErrorTypeException("CONVERT_HEX_TO_BIG_DECIMAL_ERROR", CustomErrorType.CONVERT_HEX_TO_BIG_DECIMAL_ERROR);
        }
        return result;
    }

    /**
     * Converts a BigInteger value to a hexadecimal string.
     *
     * @param value the BigInteger value to convert
     * @return the hexadecimal string representation
     */
    public static String convertBigIntegerToHex(BigInteger value) {
        return ZERO_X + value.toString(16);
    }

    /**
     * Converts a long value to a hexadecimal string.
     *
     * @param value the long value to convert
     * @return the hexadecimal string representation
     */
    public static String convertLongToHex(long value) {
        return ZERO_X + Long.toHexString(value);
    }

    /**
     * Converts a hexadecimal string to a long value.
     *
     * @param hex the hexadecimal string to convert
     * @return the long value representation
     */
    public static long convertHexToLong(@NonNull String hex) {
        String sanitizedHexString = sanitizeHexString(hex);

        return Long.parseLong(sanitizedHexString, 16);
    }

    /**
     * Converts a hexadecimal string to a boolean value.
     *
     * @param hex the hexadecimal string to convert
     * @return true if the hexadecimal value is non-zero, false otherwise
     */
    public static boolean convertHexToBoolean(@NonNull String hex) {
        String sanitizedHexString = sanitizeHexString(hex);

        long value = Long.parseLong(sanitizedHexString, 16);
        return value != 0;
    }

    /**
     * Converts a hexadecimal string to a BigInteger value.
     *
     * @param hex the hexadecimal string to convert
     * @return the BigInteger value representation
     */
    public static BigInteger convertHexToBigInteger(String hex) {
        String sanitizedHexString = sanitizeHexString(hex);

        return new BigInteger(sanitizedHexString, 16);
    }

    /**
     * Divides a balance by a decimal value represented as a power of ten.
     *
     * @param balance the balance to divide
     * @param decimal the number of decimal places as a long
     * @return the resulting BigDecimal value after division
     * @throws ErrorTypeException if the balance is null
     */
    public static BigDecimal getBalanceDividedByDecimal(BigDecimal balance, long decimal) {
        if (Optional.ofNullable(balance).isEmpty()) {
            throw new ErrorTypeException("NULL_BALANCE_NOT_ALLOWED", CustomErrorType.NULL_BALANCE_NOT_ALLOWED);
        }

        if (balance.equals(BigDecimal.ZERO)) {
            return BigDecimal.ZERO;
        } else {
            return balance.scaleByPowerOfTen((int) -decimal);
        }
    }

    /**
     * Sanitizes a hexadecimal string by removing the "0x" prefix if present
     * and validating that the string is not null or empty.
     *
     * @param hexString the hexadecimal string to sanitize
     * @return the sanitized hexadecimal string
     * @throws ErrorTypeException if the hex string is null
     */
    private static String sanitizeHexString(String hexString) {
        if (Optional.ofNullable(hexString).isEmpty()) {
            throw new ErrorTypeException("NULL_HEX_STRING_NOT_ALLOWED", CustomErrorType.NULL_HEX_STRING_NOT_ALLOWED);
        }

        if (ZERO_X.equalsIgnoreCase(hexString) || hexString.isEmpty()) {
            return ZERO;
        }

        return hexString.replace(ZERO_X, "");
    }
}

