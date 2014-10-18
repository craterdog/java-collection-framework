/************************************************************************
 * Copyright (c) Crater Dog Technologies(TM).  All Rights Reserved.     *
 ************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.        *
 *                                                                      *
 * This code is free software; you can redistribute it and/or modify it *
 * under the terms of The MIT License (MIT), as published by the Open   *
 * Source Initiative. (See http://opensource.org/licenses/MIT)          *
 ************************************************************************/
package craterdog.collections.interfaces;

import craterdog.collections.Association;


/**
 * This interface defines the methods that must be implemented by each collection that
 * associates a set of keys with a set of values.
 *
 * @author Derk Norton
 * @param <K> the key type.
 * @param <V> the value type.
 */
public interface Associative<K, V> extends Iteratable<Association<K, V>> {

    /**
     * This method returns the list of keys for the associations in this collection.
     *
     * @return The keys for this collection.
     */
    Iteratable<? super K> getKeys();

    /**
     * This method returns the list of values for the associations in this collection.
     *
     * @return The values for this collection.
     */
    Iteratable<? super V> getValues();

    /**
     * This method returns the list of associations between keys and values for this collection.
     *
     * @return A list of the key/value associations that make up this collection.
     */
    Iteratable<Association<K, V>> getAssociations();

    /**
     * This method returns the value associated with the specified key.
     *
     * @param key The key for the value to be retrieved.
     * @return The value associated with the key.
     */
    V getValueForKey(K key);

    /**
     * This method associates in this collection a new value with a key.  If there is already
     * a value associated with the specified key, the new value replaces the old value.
     *
     * @param key The key for the new value.
     * @param value The new value to be associated with the key.
     */
    void associateKeyWithValue(K key, V value);

    /**
     * This method removes from this collection the value associated with a key.  If no value
     * is associated with the specified key then <code>null</code> is returned.
     *
     * @param key The key for the value to be removed.
     * @return The value associated with the key.
     */
    V removeValueForKey(K key);

}
