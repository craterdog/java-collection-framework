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

import craterdog.collections.interfaces.Iteratable;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This collection class is a convenience class that implements a <code>Map</code> whose keys
 * are always of type <code>String</code>.
 *
 * @author Derk Norton
 * @param <V> The type of the value in the association.
 */
public final class Dictionary<V> extends Map<String, V> {

    static private final XLogger logger = XLoggerFactory.getXLogger(Dictionary.class);


    /**
     * This constructor creates a new empty dictionary.
     */
    public Dictionary() {
        super();
    }


    /**
     * This constructor creates a new dictionary using the specified key and value arrays.  The
     * number of keys must equal the number of values or an exception is thrown.
     *
     * @param keys The array of keys that should be used to create the dictionary.
     * @param values The array of values that should be used to create the dictionary.
     */
    public Dictionary(String[] keys, V[] values) {
        super(keys, values);
    }


    /**
     * This constructor creates a new dictionary using the specified keys and values.  The
     * number of keys must equal the number of values or an exception is thrown.
     *
     * @param keys The keys that should be used to create the dictionary.
     * @param values The values that should be used to create the dictionary.
     */
    public Dictionary(Iteratable<String> keys, Iteratable<V> values) {
        super(keys, values);
    }


    /**
     * This constructor creates a new dictionary using the elements provided in the specified map.
     *
     * @param elements The dictionary containing the key-value pairs to be mapped.
     */
    public Dictionary(Map<String, V> elements) {
        super(elements);
    }


    /**
     * This constructor creates a new dictionary using the elements provided in a java map.
     *
     * @param elements The java map containing the key-value pairs to be mapped.
     */
    public Dictionary(java.util.Map<String, V> elements) {
        super(elements);
    }


    /**
     * This function returns a new dictionary that contains the all the associations from
     * both the specified dictionaries.
     *
     * @param <V> The type of value contained in the dictionaries.
     * @param dictionary1 The first dictionary whose elements are to be added.
     * @param dictionary2 The second dictionary whose elements are to be added.
     * @return The resulting dictionary.
     */
    static public <V> Dictionary<V> concatenate(Dictionary<V> dictionary1, Dictionary<V> dictionary2) {
        logger.entry(dictionary1, dictionary2);
        Dictionary<V> result = new Dictionary<>(dictionary1);
        result.addElements(dictionary2);
        logger.exit(result);
        return result;
    }


    /**
     * This function returns a new dictionary that contains only the associations with
     * the specified keys.
     *
     * @param <V> The type of value contained in the dictionaries.
     * @param dictionary The dictionary whose elements are to be reduced.
     * @param keys The set of keys for the associates to be saved.
     * @return The resulting dictionary.
     */
    static public <V> Dictionary<V> reduce(Dictionary<V> dictionary, Set<String> keys) {
        logger.entry(dictionary, keys);
        Dictionary<V> result = new Dictionary<>();
        for (String key : keys) {
            V value = dictionary.getValueForKey(key);
            if (value != null) {
                logger.debug("Associating key: {} with value: {}", key, value);
                Association<String, V> association = new Association<>(key, value);
                result.addElement(association);
            }
        }
        logger.exit(result);
        return result;
    }

}
