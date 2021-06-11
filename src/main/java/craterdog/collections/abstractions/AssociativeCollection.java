/************************************************************************
 * Copyright (c) Crater Dog Technologies(TM).  All Rights Reserved.     *
 ************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.        *
 *                                                                      *
 * This code is free software; you can redistribute it and/or modify it *
 * under the terms of The MIT License (MIT), as published by the Open   *
 * Source Initiative. (See http://opensource.org/licenses/MIT)          *
 ************************************************************************/
package craterdog.collections.abstractions;

import com.fasterxml.jackson.annotation.JsonValue;
import craterdog.collections.Association;
import craterdog.collections.List;
import craterdog.collections.primitives.DynamicArray;
import craterdog.collections.primitives.HashTable;
import craterdog.core.Iterator;
import craterdog.core.Manipulator;
import java.util.Objects;
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
public abstract class AssociativeCollection<K, V> extends SortableCollection<Association<K, V>> {

    static private final XLogger logger = XLoggerFactory.getXLogger(AssociativeCollection.class);

    // a map of all keys to their associated values
    private final java.util.Map<K, Association<K, V>> map = new HashTable<>();

    // a list containing the key value associations in their proper order
    private final java.util.List<Association<K, V>> list = new DynamicArray<>();


    /**
     * This constructor creates a new empty associative collection.
     */
    protected AssociativeCollection() {
        logger.entry();
        logger.exit();
    }


    /**
     * This constructor creates a new associative collection using the specified key and
     * value arrays.  The number of keys must equal the number of values or an exception is thrown.
     *
     * @param keys The array of keys that should be used to create the associative collection.
     * @param values The array of values that should be used to create the associative collection.
     */
    protected AssociativeCollection(K[] keys, V[] values) {
        logger.entry(keys, values);
        int size = keys.length;
        if (values.length != size) throw new IllegalArgumentException("The number of keys is different than the number of values.");
        for (int i = 0; i < size; i++) {
            K key = keys[i];
            V value = values[i];
            logger.debug("Associating key: {} with value: {}", key, value);
            setValue(key, value);
        }
        logger.exit();
    }


    /**
     * This constructor creates a new associative collection using the specified keys and
     * values.  The number of keys must equal the number of values or an exception is thrown.
     *
     * @param keys The keys that should be used to create the associative collection.
     * @param values The values that should be used to create the associative collection.
     */
    protected AssociativeCollection(Collection<K> keys, Collection<V> values) {
        logger.entry(keys, values);
        int size = keys.getSize();
        if (values.getSize() != size) throw new IllegalArgumentException("The number of keys is different than the number of values.");
        Iterator<K> keyIterator = keys.createIterator();
        Iterator<V> valueIterator = values.createIterator();
        while (keyIterator.hasNext()) {
            K key = keyIterator.getNext();
            V value = valueIterator.getNext();
            logger.debug("Associating key: {} with value: {}", key, value);
            setValue(key, value);
        }
        logger.exit();
    }


    /**
     * This constructor creates a new associative collection using the elements provided in
     * the specified associative collection.
     *
     * @param elements The associative collection containing the key-value pairs to be associated.
     */
    protected AssociativeCollection(AssociativeCollection<K, V> elements) {
        logger.entry(elements);
        Collection<Association<K, V>> entries = elements.getAssociations();
        for (Association<K, V> association : entries) {
            K key = association.key;
            V value = association.value;
            logger.debug("Associating key: {} with value: {}", key, value);
            setValue(key, value);
        }
        logger.exit();
    }


    /**
     * This constructor creates a new associative collection using the elements provided in
     * a java associative collection.
     *
     * @param elements The java map containing the key-value pairs to be associated.
     */
    protected AssociativeCollection(java.util.Map<K, V> elements) {
        logger.entry(elements);
        java.util.Set<? extends java.util.Map.Entry<K, V>> entries = elements.entrySet();
        for (java.util.Map.Entry<K, V> entry : entries) {
            K key = entry.getKey();
            V value = entry.getValue();
            logger.debug("Associating key: {} with value: {}", key, value);
            setValue(key, value);
        }
        logger.exit();
    }


    /**
     * This method returns this collection as a Java map.
     *
     * @return A map containing the values for this collection.
     */
    @JsonValue
    public final java.util.LinkedHashMap<K, V> toMap() {
        java.util.LinkedHashMap<K, V> result = new java.util.LinkedHashMap<>();
        for (Association<K, V> association : list) {
            K key = association.key;
            V value = association.value;
            result.put(key, value);
        }
        return result;
    }


    @Override
    public final int getSize() {
        logger.entry();
        int result = map.size();
        logger.exit(result);
        return result;
    }


    @Override
    public final boolean containsElement(Association<K, V> element) {
        logger.entry();
        Association<K, V> association = map.get(element.key);
        boolean result = association != null && Objects.equals(association.value, element.value);
        logger.exit(result);
        return result;
    }


    @Override
    public final int getIndex(Association<K, V> element) {
        logger.entry(element);
        int index = 0;
        Iterator<Association<K, V>> iterator = createIterator();
        while (iterator.hasNext()) {
            Association<K, V> association = iterator.getNext();
            index++;
            if (Objects.equals(element, association)) break;
        }
        logger.exit(index);
        return index;
    }


    @Override
    public final Association<K, V> getElement(int index) {
        logger.entry(index);
        index = normalizedIndex(index) - 1;  // change to zero based indexing
        Association<K, V> element = list.get(index);
        logger.exit(element);
        return element;
    }


    @Override
    public final Collection<Association<K, V>> getElements(int firstIndex, int lastIndex) {
        logger.entry(firstIndex, lastIndex);
        firstIndex = normalizedIndex(firstIndex) - 1;  // change to zero based indexing
        lastIndex = normalizedIndex(lastIndex) - 1;  // change to zero based indexing
        AssociativeCollection<K, V> elements = emptyCopy();
        java.util.List<Association<K, V>> associations = list.subList(firstIndex, lastIndex);
        for (Association<K, V> association : associations) {
            logger.debug("Including element: {}", association);
            elements.addElement(association);
        }
        logger.exit(elements);
        return elements;
    }


    /*
     * NOTE: This method has different semantics from the setValue() method.  This
     * method only inserts a new value if the key does not already exist in the associative
     * collection.  Otherwise, it does nothing.
     */
    @Override
    public final boolean addElement(Association<K, V> element) {
        logger.entry(element);
        boolean result = false;
        K key = element.key;
        V value = element.value;
        if (!map.containsKey(element.key)) {
            setValue(key, value);
            result = true;
        }
        logger.exit(result);
        return result;
    }


    @Override
    public final boolean removeElement(Association<K, V> element) {
        logger.entry(element);
        boolean result = removeValue(element.key) != null;
        logger.exit(result);
        return result;
    }


    @Override
    public final void removeAll() {
        logger.entry();
        map.clear();
        list.clear();
        logger.exit();
    }


    /**
     * This method returns the value associated with the specified key.
     *
     * @param key The key for the value to be retrieved.
     * @return The value associated with the key.
     */
    public final V getValue(K key) {
        logger.entry(key);
        V value = null;
        Association<K, V> association = map.get(key);
        if (association != null) {
            value = association.value;
            logger.debug("Found value: {} at key: {}", value, key);
        }
        logger.exit(value);
        return value;
    }


    /**
     * This method associates in this collection a new value with a key.  If there is already
     * a value associated with the specified key, the new value replaces the old value.
     *
     * @param key The key for the new value.
     * @param value The new value to be associated with the key.
     */
    public final void setValue(K key, V value) {
        logger.entry(key, value);
        Association<K, V> association = map.get(key);
        if (association != null) {
            association.value = value;
        } else {
            association = new Association<>(key, value);
            list.add(association);
            map.put(key, association);
        }
        logger.exit();
    }


    /**
     * This method removes from this collection the value associated with a key.  If no value
     * is associated with the specified key then <code>null</code> is returned.
     *
     * @param key The key for the value to be removed.
     * @return The value associated with the key.
     */
    public final V removeValue(K key) {
        logger.entry(key);
        V value = null;
        Association<K, V> association = map.remove(key);
        if (association != null) {
            list.remove(association);
            value = association.value;
        }
        logger.exit(value);
        return value;
    }


    /**
     * This method returns the list of keys for the associations in this collection.
     *
     * @return The keys for this collection.
     */
    public final SortableCollection<K> getKeys() {
        logger.entry();
        SortableCollection<K> keys = new List<>();
        for (Association<K, V> association : list) {
            K key = association.key;
            logger.debug("Found key: {}", key);
            keys.addElement(key);
        }
        logger.exit(keys);
        return keys;
    }


    /**
     * This method returns the list of values for the associations in this collection.
     *
     * @return The values for this collection.
     */
    public final SortableCollection<V> getValues() {
        logger.entry();
        SortableCollection<V> values = new List<>();
        for (Association<K, V> association : list) {
            V value = association.value;
            logger.debug("Found value: {}", value);
            values.addElement(value);
        }
        logger.exit(values);
        return values;
    }


    /**
     * This method returns the list of associations between keys and values for this collection.
     *
     * @return A list of the key/value associations that make up this collection.
     */
    public final SortableCollection<Association<K, V>> getAssociations() {
        logger.entry();
        SortableCollection<Association<K, V>> associations = new List<>();
        for (Association<K, V> association : list) {
            logger.debug("Found association: {}", association);
            associations.addElement(association);
        }
        logger.exit();
        return associations;
    }


    @Override
    public Iterator<Association<K, V>> createIterator() {
        logger.entry();
        Iterator<Association<K, V>> result = new MapManipulator();
        logger.exit(result);
        return result;
    }


    @Override
    public Manipulator<Association<K, V>> createManipulator() {
        logger.entry();
        Manipulator<Association<K, V>> result = new MapManipulator();
        logger.exit(result);
        return result;
    }


    private class MapManipulator extends Manipulator<Association<K, V>> {

        private int currentIndex = 0;  // zero based indexing for underlying list

        @Override
        public void toStart() {
            logger.entry();
            currentIndex = 0;
            logger.exit();
        }

        @Override
        public void toIndex(int index) {
            logger.entry(index);
            currentIndex = normalizedIndex(index) - 1;  // change to zero based indexing
            logger.exit();
        }

        @Override
        public void toEnd() {
            logger.entry();
            currentIndex = list.size();
            logger.exit();
        }

        @Override
        public boolean hasPrevious() {
            logger.entry();
            boolean result = currentIndex > 0;
            logger.exit(result);
            return result;
        }

        @Override
        public boolean hasNext() {
            logger.entry();
            boolean result = currentIndex < map.size();
            logger.exit(result);
            return result;
        }

        @Override
        public Association<K, V> getNext() {
            logger.entry();
            if (hasNext()) {
                Association<K, V> result = list.get(currentIndex++);
                logger.exit(result);
                return result;
            }
            IllegalStateException exception = new IllegalStateException("The iterator is at the end of the collection.");
            throw logger.throwing(exception);
        }

        @Override
        public Association<K, V> getPrevious() {
            logger.entry();
            if (hasPrevious()) {
                Association<K, V> result = list.get(--currentIndex);
                logger.exit(result);
                return result;
            }
            IllegalStateException exception = new IllegalStateException("The iterator is at the beginning of the collection.");
            throw logger.throwing(exception);
        }

        @Override
        public void insertElement(Association<K, V> element) {
            logger.entry(element);
            K key = element.key;
            if (map.containsKey(key)) {
                String message = "Attempted to add a duplicate key with an iterator.";
                RuntimeException exception = new RuntimeException(message);
                throw logger.throwing(exception);
            } else {
                map.put(key, element);
                list.add(currentIndex++, element);
            }
            logger.exit();
        }

        @Override
        public Association<K, V> removeNext() {
            logger.entry();
            if (hasNext()) {
                Association<K, V> element = list.get(currentIndex);
                logger.exit(element);
                return element;
            }
            IllegalStateException exception = new IllegalStateException("The iterator is at the end of the collection.");
            throw logger.throwing(exception);
        }

        @Override
        public Association<K, V> removePrevious() {
            logger.entry();
            if (hasPrevious()) {
                Association<K, V> element = list.get(--currentIndex);
                logger.exit(element);
                return element;
            }
            IllegalStateException exception = new IllegalStateException("The iterator is at the beginning of the collection.");
            throw logger.throwing(exception);
        }

    }

}
