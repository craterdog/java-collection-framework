/************************************************************************
 * Copyright (c) Crater Dog Technologies(TM).  All Rights Reserved.     *
 ************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.        *
 *                                                                      *
 * This code is free software; you can redistribute it and/or modify it *
 * under the terms of The MIT License (MIT), as published by the Open   *
 * Source Initiative. (See http://opensource.org/licenses/MIT)          *
 ************************************************************************/
package craterdog.collections;

import com.fasterxml.jackson.annotation.JsonCreator;
import craterdog.collections.abstractions.AssociativeCollection;
import craterdog.collections.abstractions.Collection;
import craterdog.collections.abstractions.OpenCollection;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This collection class implements a sortable collection containing key-value associations.  The
 * implementation is optimized for both inserting new associations and looking up values based on
 * their key.  The implementation also dynamically scales up and down the number of buckets as the
 * number of associations changes over time.
 *
 * @author Derk Norton
 * @param <K> The type of the key in the association.
 * @param <V> The type of the value in the association.
 */
public class Map<K, V> extends AssociativeCollection<K, V> {

    static private final XLogger logger = XLoggerFactory.getXLogger(Map.class);

    /**
     * This constructor creates a new empty map.
     */
    public Map() {
        super();
    }


    /**
     * This constructor creates a new map using the specified key and value arrays.  The
     * number of keys must equal the number of values or an exception is thrown.
     *
     * @param keys The array of keys that should be used to create the map.
     * @param values The array of values that should be used to create the map.
     */
    public Map(K[] keys, V[] values) {
        super(keys, values);
    }


    /**
     * This constructor creates a new map using the specified keys and values.  The
     * number of keys must equal the number of values or an exception is thrown.
     *
     * @param keys The keys that should be used to create the map.
     * @param values The values that should be used to create the map.
     */
    public Map(Collection<K> keys, Collection<V> values) {
        super(keys, values);
    }


    /**
     * This constructor creates a new map using the elements provided in the specified map.
     *
     * @param elements The map containing the key-value pairs to be mapped.
     */
    public Map(Map<K, V> elements) {
        super(elements);
    }


    /**
     * This constructor creates a new map using the elements provided in a java map.
     *
     * @param elements The java map containing the key-value pairs to be mapped.
     */
    @JsonCreator
    public Map(java.util.Map<K, V> elements) {
        super(elements);
    }


    @Override
    protected <T extends OpenCollection<Association<K, V>>> T emptyCopy() {
        @SuppressWarnings("unchecked")
        T copy = (T) new Map<>();
        return copy;
    }


    /**
     * This function returns a new map that contains the all the associations from
     * both the specified maps.
     *
     * @param <K> The type of key contained in the maps.
     * @param <V> The type of value contained in the maps.
     * @param map1 The first map whose elements are to be added.
     * @param map2 The second map whose elements are to be added.
     * @return The resulting map.
     */
    static public <K, V> Map<K, V> concatenation(Map<K, V> map1, Map<K, V> map2) {
        logger.entry(map1, map2);
        Map<K, V> result = new Map<>(map1);
        result.addElements(map2);
        logger.exit(result);
        return result;
    }


    /**
     * This function returns a new map that contains only the associations with
     * the specified keys.
     *
     * @param <K> The type of key contained in the maps.
     * @param <V> The type of value contained in the maps.
     * @param map The map whose elements are to be reduced.
     * @param keys The set of keys for the associates to be saved.
     * @return The resulting map.
     */
    static public <K, V> Map<K, V> reduction(Map<K, V> map, Set<K> keys) {
        logger.entry(map, keys);
        Map<K, V> result = new Map<>();
        for (K key : keys) {
            V value = map.getValue(key);
            if (value != null) {
                result.setValue(key, value);
            }
        }
        logger.exit(result);
        return result;
    }

}
