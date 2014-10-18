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

import craterdog.collections.abstractions.Iterator;
import craterdog.collections.abstractions.Manipulator;
import craterdog.collections.abstractions.SortableCollection;
import craterdog.collections.interfaces.Iteratable;
import craterdog.collections.interfaces.Associative;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This collection class implements a sortable collection containing key-value associations.
 *
 * @author Derk Norton
 * @param <K> The type of the key in the association.
 * @param <V> The type of the value in the association.
 */
public class Map<K, V> extends SortableCollection<Association<K, V>> implements Associative<K, V> {

    static private final XLogger logger = XLoggerFactory.getXLogger(Map.class);

    private final java.util.Map<K, Integer> indexes = new java.util.TreeMap<>();
    private final List<Association<K, V>> associations = new List<>();


    /**
     * This constructor creates a new empty map.
     */
    public Map() {
    }


    /**
     * This constructor creates a new map using the specified key and value arrays.  The
     * number of keys must equal the number of values or an exception is thrown.
     *
     * @param keys The array of keys that should be used to create the map.
     * @param values The array of values that should be used to create the map.
     */
    public Map(K[] keys, V[] values) {
        logger.entry(keys, values);
        int size = keys.length;
        if (values.length != size) throw new IllegalArgumentException("The number of keys is different than the number of values.");
        for (int i = 0; i < size; i++) {
            K key = keys[i];
            V value = values[i];
            logger.debug("Associating key: {} with value: {}", key, value);
            Association<K, V> association = new Association<>(key, value);
            addElement(association);
        }
        logger.exit();
    }


    /**
     * This constructor creates a new map using the specified keys and values.  The
     * number of keys must equal the number of values or an exception is thrown.
     *
     * @param keys The keys that should be used to create the map.
     * @param values The values that should be used to create the map.
     */
    public Map(Iteratable<K> keys, Iteratable<V> values) {
        logger.entry(keys, values);
        int size = keys.getNumberOfElements();
        if (values.getNumberOfElements() != size) throw new IllegalArgumentException("The number of keys is different than the number of values.");
        Iterator<K> keyIterator = keys.createDefaultIterator();
        Iterator<V> valueIterator = values.createDefaultIterator();
        while (keyIterator.hasNextElement()) {
            K key = keyIterator.getNextElement();
            V value = valueIterator.getNextElement();
            logger.debug("Associating key: {} with value: {}", key, value);
            Association<K, V> association = new Association<>(key, value);
            addElement(association);
        }
        logger.exit();
    }


    /**
     * This constructor creates a new map using the elements provided in the specified map.
     *
     * @param elements The map containing the key-value pairs to be mapped.
     */
    public Map(Map<K, V> elements) {
        logger.entry(elements);
        Iteratable<Association<K, V>> entries = elements.getAssociations();
        for (Association<K, V> association : entries) {
            K key = association.key;
            V value = association.value;
            logger.debug("Associating key: {} with value: {}", key, value);
            associations.addElement(new Association<>(key, value));
        }
        logger.exit();
    }


    /**
     * This constructor creates a new map using the elements provided in a java map.
     *
     * @param elements The java map containing the key-value pairs to be mapped.
     */
    public Map(java.util.Map<K, V> elements) {
        logger.entry(elements);
        java.util.Set<? extends java.util.Map.Entry<K, V>> entries = elements.entrySet();
        for (java.util.Map.Entry<K, V> entry : entries) {
            K key = entry.getKey();
            V value = entry.getValue();
            logger.debug("Associating key: {} with value: {}", key, value);
            Association<K, V> association = new Association<>(key, value);
            addElement(association);
        }
        logger.exit();
    }


    @Override
    public final int getNumberOfElements() {
        logger.entry();
        int result = associations.getNumberOfElements();
        logger.exit(result);
        return result;
    }


    @Override
    public final boolean containsElement(Association<K, V> element) {
        logger.entry();
        boolean result = associations.containsElement(element);
        logger.exit(result);
        return result;
    }


    @Override
    public Iterator<Association<K, V>> createDefaultIterator() {
        logger.entry();
        Iterator<Association<K, V>> result = new MapManipulator();
        logger.exit(result);
        return result;
    }


    /*
    NOTE: This method has different semantics from the associateKeyWithValue() method.  This
    method only inserts a new value if the key does not already exist in the map.  Otherwise
    it does nothing.
    */
    @Override
    public final boolean addElement(Association<K, V> element) {
        logger.entry(element);
        boolean result = false;
        if (!indexes.containsKey(element.key)) {
            associations.addElement(element);
            indexes.put(element.key, associations.getNumberOfElements());
            result = true;
        }
        logger.exit(result);
        return result;
    }


    @Override
    public final boolean removeElement(Association<K, V> element) {
        logger.entry(element);
        boolean result = false;
        Integer index = indexes.remove(element.key);
        if (index != null) {
            int size = associations.getNumberOfElements();
            associations.removeElementAtIndex(index);
            if (index < size) recalculateIndexes();  // optimized to not reindex if last element removed
            result = true;
        }
        logger.exit(result);
        return result;
    }


    @Override
    public final void removeAllElements() {
        logger.entry();
        indexes.clear();
        associations.removeAllElements();
        logger.exit();
    }


    @Override
    public Manipulator<Association<K, V>> createDefaultManipulator() {
        logger.entry();
        Manipulator<Association<K, V>> result = new MapManipulator();
        logger.exit(result);
        return result;
    }


    @Override
    public final Iteratable<? super K> getKeys() {
        logger.entry();
        List<K> keys = new List<>();
        for (Association<K, V> association : associations) {
            K key = association.key;
            logger.debug("Found key: {}", key);
            keys.addElement(key);
        }
        logger.exit(keys);
        return keys;
    }


    @Override
    public final Iteratable<? super V> getValues() {
        logger.entry();
        List<V> values = new List<>();
        for (Association<K, V> association : associations) {
            V value = association.value;
            logger.debug("Found value: {}", value);
            values.addElement(value);
        }
        logger.exit(values);
        return values;
    }


    @Override
    public final Iteratable<Association<K, V>> getAssociations() {
        logger.entry();
        Iteratable<Association<K, V>> associationList = new List<>(this.associations);
        logger.exit();
        return associationList;
    }


    @Override
    public final V getValueForKey(K key) {
        logger.entry(key);
        V value = null;
        Integer index = indexes.get(key);
        if (index != null) {
            Association<K, V> association = associations.getElementAtIndex(index);
            value = association.value;
            logger.debug("Found value: {} at key: {}", value, key);
        }
        logger.exit(value);
        return value;
    }


    @Override
    public final void associateKeyWithValue(K key, V value) {
        logger.entry(key, value);
        Integer index = indexes.get(key);
        if (index != null) {
            Association<K, V> association = associations.getElementAtIndex(index);
            logger.debug("Replacing value: {} with value: {} for key: {}", association.value, value, key);
            association.value = value;
        } else {
            Association<K, V> association = new Association<>(key, value);
            logger.debug("Associating value: {} with key: {}", value, key);
            associations.addElement(association);
            indexes.put(key, associations.getNumberOfElements());
        }
        logger.exit();
    }


    @Override
    public final V removeValueForKey(K key) {
        logger.entry(key);
        V value = null;
        Integer index = indexes.remove(key);
        if (index != null) {
            int size = associations.getNumberOfElements();
            Association<K, V> association = associations.removeElementAtIndex(index);
            if (index < size) recalculateIndexes();
            value = association.value;
            logger.debug("Removing value: {} associated with key: {}", value, key);
        }
        logger.exit(value);
        return value;
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
    static public <K, V> Map<K, V> concatenate(Map<K, V> map1, Map<K, V> map2) {
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
    static public <K, V> Map<K, V> reduce(Map<K, V> map, Set<K> keys) {
        logger.entry(map, keys);
        Map<K, V> result = new Map<>();
        for (K key : keys) {
            V value = map.getValueForKey(key);
            if (value != null) {
                Association<K, V> association = new Association<>(key, value);
                result.addElement(association);
            }
        }
        logger.exit(result);
        return result;
    }


    private void recalculateIndexes() {
        logger.entry();
        indexes.clear();
        int index = 1;
        for (Association<K, V> association : associations) {
            K key = association.key;
            logger.debug("Adding index: {} for key: {}", index, key);
            indexes.put(key, index++);
        }
        logger.exit();
    }


    /*
     * This manipulator class implements both the <code>Iterator</code> abstraction
     * and the <code>Manipulator</code> abstraction so it can be used as either depending
     * on how it is returned from the map.  It utilizes the iterator for the underlying
     * implementation.  Like most iterators, it should be used to access a map exclusively
     * without any other requests, especially requests that change the size of the map.
     */
    private class MapManipulator extends Manipulator<Association<K, V>> {

        private final Manipulator<Association<K, V>> manipulator = associations.createDefaultManipulator();

        @Override
        public void goToStart() {
            logger.entry();
            manipulator.goToStart();
            logger.exit();
        }

        @Override
        public void goToIndex(int index) {
            logger.entry(index);
            index = normalizedIndex(index);
            manipulator.goToIndex(index);
            logger.exit();
        }

        @Override
        public void goToEnd() {
            logger.entry();
            manipulator.goToEnd();
            logger.exit();
        }

        @Override
        public boolean hasPreviousElement() {
            logger.entry();
            boolean result = manipulator.hasPreviousElement();
            logger.exit(result);
            return result;
        }

        @Override
        public boolean hasNextElement() {
            logger.entry();
            boolean result = manipulator.hasNextElement();
            logger.exit(result);
            return result;
        }

        @Override
        public Association<K, V> getNextElement() {
            logger.entry();
            if (!hasNextElement()) {
                IllegalStateException exception = new IllegalStateException("The iterator is at the end of the map.");
                logger.throwing(exception);
                throw exception;
            }
            Association<K, V> result = manipulator.getNextElement();
            logger.exit(result);
            return result;
        }

        @Override
        public Association<K, V> getPreviousElement() {
            logger.entry();
            if (!hasPreviousElement()) {
                IllegalStateException exception = new IllegalStateException("The iterator is at the beginning of the map.");
                logger.throwing(exception);
                throw exception;
            }
            Association<K, V> result = manipulator.getPreviousElement();
            logger.exit(result);
            return result;
        }

        @Override
        public void insertElement(Association<K, V> element) {
            logger.entry(element);
            manipulator.insertElement(element);
            recalculateIndexes();
            logger.exit();
        }

        @Override
        public Association<K, V> removeNextElement() {
            logger.entry();
            if (!hasNextElement()) {
                IllegalStateException exception = new IllegalStateException("The iterator is at the end of the map.");
                logger.throwing(exception);
                throw exception;
            }
            Association<K, V> result = manipulator.removeNextElement();
            recalculateIndexes();
            logger.exit(result);
            return result;
        }

        @Override
        public Association<K, V> removePreviousElement() {
            logger.entry();
            if (!hasPreviousElement()) {
                IllegalStateException exception = new IllegalStateException("The iterator is at the beginning of the map.");
                logger.throwing(exception);
                throw exception;
            }
            Association<K, V> result = manipulator.removePreviousElement();
            recalculateIndexes();
            logger.exit(result);
            return result;
        }

    }

}
