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
import craterdog.utils.NaturalComparator;
import java.util.Comparator;
import java.util.Objects;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This class defines an association between a key and a value.
 *
 * @author Derk Norton
 * @param <K> The type of the key in the association.
 * @param <V> The type of the value in the association.
 */
public class Association<K, V> implements Composite<Association<K, V>> {

    static private final XLogger logger = XLoggerFactory.getXLogger(Association.class);


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
        logger.entry(key, value);
        this.key = key;
        this.value = value;
        logger.exit();
    }


    @Override
    public boolean equals(Object object) {
        logger.entry(object);
        boolean result = false;
        if (object != null && getClass() == object.getClass()) {
            final Association<?, ?> that = (Association<?, ?>) object;
            result = Objects.equals(this.key, that.key) && Objects.equals(this.value, that.value);
        }
        logger.exit(result);
        return result;
    }


    @Override
    public int hashCode() {
        logger.entry();
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.key);
        hash = 13 * hash + Objects.hashCode(this.value);
        logger.exit(hash);
        return hash;
    }


    @Override
    public int compareTo(Association<K, V> that) {
        logger.entry(that);
        Comparator<K> comparator = new NaturalComparator<>();
        int result = comparator.compare(this.key, that.key);
        logger.exit(result);
        return result;
    }


    @Override
    public String toString() {
        logger.entry();
        String string = toString("");
        logger.exit(string);
        return string;
    }


    @Override
    public String toString(String indentation) {
        logger.entry(indentation);
        StringBuilder builder = new StringBuilder();
        builder.append(key);
        builder.append(": ");
        if (value instanceof Composite) {
            Composite<?> composite = (Composite<?>) value;
            builder.append(composite.toString(indentation));
        } else {
            builder.append(value);
        }
        String string = builder.toString();
        logger.exit(string);
        return string;
    }


    @Override
    public <T extends Composite<Association<K, V>>> T copy() {
        @SuppressWarnings("unchecked")
        T copy = (T) new Association<>(key, value);
        return copy;
    }

}
