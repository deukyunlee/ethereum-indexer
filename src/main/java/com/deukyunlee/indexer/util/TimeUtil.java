package com.deukyunlee.indexer.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 26.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeUtil {
    private static final String ZERO_X = "0x";
    private static final String UTC = "UTC";


    /**
     * Converts a hexadecimal UNIX timestamp string to an Instant.
     *
     * @param hex the hexadecimal string representing a UNIX timestamp
     * @return the Instant representation of the timestamp
     */
    public static Instant convertHexToInstant(String hex) {
        if (hex.startsWith(ZERO_X)) {
            hex = hex.substring(2);
        }

        long unixTimestamp = Long.parseLong(hex, 16);

        return Instant.ofEpochSecond(unixTimestamp);
    }

    /**
     * Converts {@link LocalDate} to an {@link Instant}
     * @param localDate the {@link LocalDate} to convert
     * @return the corresponding {@link Instant} at the start of the day in UTC
     */
    public static Instant convertLocalDateToInstant(LocalDate localDate) {
        return localDate.atStartOfDay(ZoneId.of(UTC)).toInstant();
    }
}
