package org.jcoderz.m3server.library.search;

import java.util.List;
import org.jcoderz.m3server.library.Item;

/**
 * This is a marker interface for search providers.
 *
 * @author mrumpf
 */
public interface Searchable {
    /**
     * Search with the specified query string.
     *
     * @param query the query string
     * @return a list of items
     */
    List<Item> search(String query);
}
