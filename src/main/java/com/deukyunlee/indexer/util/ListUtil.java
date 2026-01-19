package com.deukyunlee.indexer.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by dufqkd1004@naver.com on 2024. 12. 27.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ListUtil {
    /**
     * Safely retrieves an element from a list by index. Returns null if the list is null
     * or if the index is out of bounds.
     *
     * @param list  the list to retrieve the element from
     * @param index the index of the element
     * @param <T>   the type of elements in the list
     * @return the element at the specified index, or null if the list is null or the index is out of bounds
     */
    public static <T> T getOrNull(List<T> list, int index) {
        return (list != null && list.size() > index) ? list.get(index) : null;
    }
}
