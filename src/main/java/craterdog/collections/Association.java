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

import craterdog.core.Composite;
import java.util.Objects;


/**
 * This class defines an association between a key and a value.
 *
 * @author Derk Norton
 * @param <K> The type of the key in the association.
 * @param <V> The type of the value in the association.
 */
public class Association<K, V> implements Comparable<Association<K, V>>, Composite {

    /**
     * The key in the association.
     */
    public final K key;

    /**
     * The value in the association.
     */
    public V value;


    /**
     * This constructor creates a new key-value association.
     * @param key The key in the association.
     * @param value The value in the association.
     */
    public Association(K key, V value) {
        this.key = key;
        this.value = value;
    }


    @Override
    public String toString() {
        return toString("");
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Association<?, ?> that = (Association<?, ?>) obj;
        if (!Objects.equals(this.key, that.key)) return false;
        return Objects.equals(this.value, that.value);
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.key);
        hash = 13 * hash + Objects.hashCode(this.value);
        return hash;
    }


    @Override
    public int compareTo(Association<K, V> association) {
        @SuppressWarnings("unchecked")
        Comparable<K> comparable = (Comparable<K>) key;  // may throw ClassCastException
        return comparable.compareTo(association.key);
    }


    @Override
    public String toString(String indentation) {
        StringBuilder builder = new StringBuilder();
        builder.append(key);
        builder.append(": ");
        if (value instanceof Composite) {
            Composite composite = (Composite) value;
            builder.append(composite.toString(indentation));
        } else {
            builder.append(value);
        }
        return builder.toString();
    }

}
