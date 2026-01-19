package com.deukyunlee.indexer.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 26.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SleepUtil {
    /**
     * Pauses the current thread for the specified number of milliseconds.
     * @param ms the duration to sleep in milliseconds
     */

    public static void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
