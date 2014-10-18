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


/**
 * This collection class implements a sortable collection containing key-value associations.
 *
 * @author Derk Norton
 * @param <K> The type of the key in the association.
 * @param <V> The type of the value in the association.
 */
public final class Map<K, V> extends SortableCollection<Association<K, V>> implements Associative<K, V> {

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
        int size = keys.length;
        if (values.length != size) throw new IllegalArgumentException("The number of keys is different than the number of values.");
        for (int i = 0; i < size; i++) {
            K key = keys[i];
            V value = values[i];
            Association<K, V> association = new Association<>(key, value);
            addElement(association);
        }
    }


    /**
     * This constructor creates a new map using the specified keys and values.  The
     * number of keys must equal the number of values or an exception is thrown.
     *
     * @param keys The keys that should be used to create the map.
     * @param values The values that should be used to create the map.
     */
    public Map(Iteratable<K> keys, Iteratable<V> values) {
        int size = keys.getNumberOfElements();
        if (values.getNumberOfElements() != size) throw new IllegalArgumentException("The number of keys is different than the number of values.");
        Iterator<K> keyIterator = keys.createDefaultIterator();
        Iterator<V> valueIterator = values.createDefaultIterator();
        while (keyIterator.hasNextElement()) {
            K key = keyIterator.getNextElement();
            V value = valueIterator.getNextElement();
            Association<K, V> association = new Association<>(key, value);
            addElement(association);
        }
    }


    /**
     * This constructor creates a new map using the elements provided in the specified map.
     *
     * @param elements The map containing the key-value pairs to be mapped.
     */
    public Map(Map<K, V> elements) {
        Iteratable<Association<K, V>> entries = elements.getAssociations();
        for (Association<K, V> association : entries) {
            K key = association.key;
            V value = association.value;
            associations.addElement(new Association<>(key, value));
        }
    }


    /**
     * This constructor creates a new map using the elements provided in a java map.
     *
     * @param elements The java map containing the key-value pairs to be mapped.
     */
    public Map(java.util.Map<K, V> elements) {
        java.util.Set<? extends java.util.Map.Entry<K, V>> entries = elements.entrySet();
        for (java.util.Map.Entry<K, V> entry : entries) {
            K key = entry.getKey();
            V value = entry.getValue();
            Association<K, V> association = new Association<>(key, value);
            addElement(association);
        }
    }


    @Override
    public int getNumberOfElements() {
        return associations.getNumberOfElements();
    }


    @Override
    public final boolean containsElement(Association<K, V> element) {
        return associations.containsElement(element);
    }


    @Override
    public Iterator<Association<K, V>> createDefaultIterator() {
        return new MapManipulator();
    }


    /*
    NOTE: This method has different semantics from the associateKeyWithValue() method.  This
    method only inserts a new value if the key does not already exist in the map.  Otherwise
    it does nothing.
    */
    @Override
    public boolean addElement(Association<K, V> element) {
        if (!indexes.containsKey(element.key)) {
            associations.addElement(element);
            indexes.put(element.key, associations.getNumberOfElements());
            return true;
        }
        return false;
    }


    @Override
    public boolean removeElement(Association<K, V> element) {
        Integer index = indexes.remove(element.key);
        if (index != null) {
            int size = associations.getNumberOfElements();
            associations.removeElementAtIndex(index);
            if (index < size) recalculateIndexes();  // optimized to not reindex if last element removed
            return true;
        }
        return false;
    }


    @Override
    public void removeAllElements() {
        indexes.clear();
        associations.removeAllElements();
    }


    @Override
    public Manipulator<Association<K, V>> createDefaultManipulator() {
        return new MapManipulator();
    }


    @Override
    public Iteratable<? super K> getKeys() {
        List<K> keys = new List<>();
        for (Association<K, V> association : associations) {
            K key = association.key;
            keys.addElement(key);
        }
        return keys;
    }


    @Override
    public Iteratable<? super V> getValues() {
        List<V> values = new List<>();
        for (Association<K, V> association : associations) {
            V value = association.value;
            values.addElement(value);
        }
        return values;
    }


    @Override
    public Iteratable<Association<K, V>> getAssociations() {
        Iteratable<Association<K, V>> associationList = new List<>(this.associations);
        return associationList;
    }


    @Override
    public V getValueForKey(K key) {
        V value = null;
        Integer index = indexes.get(key);
        if (index != null) {
            Association<K, V> association = associations.getElementAtIndex(index);
            value = association.value;
        }
        return value;
    }


    @Override
    public void associateKeyWithValue(K key, V value) {
        Integer index = indexes.get(key);
        if (index != null) {
            Association<K, V> association = associations.getElementAtIndex(index);
            association.value = value;
        } else {
            Association<K, V> association = new Association<>(key, value);
            associations.addElement(association);
            indexes.put(key, associations.getNumberOfElements());
        }
    }


    @Override
    public V removeValueForKey(K key) {
        V value = null;
        Integer index = indexes.remove(key);
        if (index != null) {
            int size = associations.getNumberOfElements();
            Association<K, V> association = associations.removeElementAtIndex(index);
            if (index < size) recalculateIndexes();
            value = association.value;
        }
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
        Map<K, V> result = new Map<>(map1);
        result.addElements(map2);
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
        Map<K, V> result = new Map<>();
        for (K key : keys) {
            V value = map.getValueForKey(key);
            if (value != null) {
                Association<K, V> association = new Association<>(key, value);
                result.addElement(association);
            }
        }
        return result;
    }


    private void recalculateIndexes() {
        indexes.clear();
        int index = 1;
        for (Association<K, V> association : associations) {
            indexes.put(association.key, index++);
        }
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
            manipulator.goToStart();
        }

        @Override
        public void goToIndex(int index) {
            index = normalizedIndex(index);
            manipulator.goToIndex(index);
        }

        @Override
        public void goToEnd() {
            manipulator.goToEnd();
        }

        @Override
        public boolean hasPreviousElement() {
            return manipulator.hasPreviousElement();
        }

        @Override
        public boolean hasNextElement() {
            return manipulator.hasNextElement();
        }

        @Override
        public Association<K, V> getNextElement() {
            if (!hasNextElement()) throw new IllegalStateException("The iterator is at the end of the map.");
            return manipulator.getNextElement();
        }

        @Override
        public Association<K, V> getPreviousElement() {
            if (!hasPreviousElement()) throw new IllegalStateException("The iterator is at the beginning of the map.");
            return manipulator.getPreviousElement();
        }

        @Override
        public void insertElement(Association<K, V> element) {
            manipulator.insertElement(element);
            recalculateIndexes();
        }

        @Override
        public Association<K, V> removeNextElement() {
            if (!hasNextElement()) throw new IllegalStateException("The iterator is at the end of the map.");
            Association<K, V> result = manipulator.removeNextElement();
            recalculateIndexes();
            return result;
        }

        @Override
        public Association<K, V> removePreviousElement() {
            if (!hasPreviousElement()) throw new IllegalStateException("The iterator is at the beginning of the map.");
            Association<K, V> result = manipulator.removePreviousElement();
            recalculateIndexes();
            return result;
        }

    }

}
