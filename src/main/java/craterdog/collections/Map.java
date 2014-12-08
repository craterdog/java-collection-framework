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

import craterdog.collections.abstractions.Collection;
import craterdog.collections.abstractions.Iterator;
import craterdog.collections.abstractions.Manipulator;
import craterdog.collections.abstractions.SortableCollection;
import craterdog.collections.interfaces.Associative;
import craterdog.collections.primitives.HashTable;
import craterdog.collections.primitives.Link;
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
public class Map<K, V> extends SortableCollection<Association<K, V>> implements Associative<K, V> {

    static private final XLogger logger = XLoggerFactory.getXLogger(Map.class);

    // a hash table mapping each association key to its corresponding link in the linked list
    private final HashTable<K, Link<Association<K, V>>> indexes = new HashTable<>();

    // a linked list containing the key value associations in their proper order
    private Link<Association<K, V>> associations = null;


    /**
     * This constructor creates a new empty map.
     */
    public Map() {
        logger.entry();
        logger.exit();
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
            associateKeyWithValue(key, value);
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
    public Map(Collection<K> keys, Collection<V> values) {
        logger.entry(keys, values);
        int size = keys.getNumberOfElements();
        if (values.getNumberOfElements() != size) throw new IllegalArgumentException("The number of keys is different than the number of values.");
        Iterator<K> keyIterator = keys.createDefaultIterator();
        Iterator<V> valueIterator = values.createDefaultIterator();
        while (keyIterator.hasNextElement()) {
            K key = keyIterator.getNextElement();
            V value = valueIterator.getNextElement();
            logger.debug("Associating key: {} with value: {}", key, value);
            associateKeyWithValue(key, value);
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
        Collection<Association<K, V>> entries = elements.getAssociations();
        for (Association<K, V> association : entries) {
            K key = association.key;
            V value = association.value;
            logger.debug("Associating key: {} with value: {}", key, value);
            associateKeyWithValue(key, value);
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
            associateKeyWithValue(key, value);
        }
        logger.exit();
    }


    @Override
    public final int getNumberOfElements() {
        logger.entry();
        int result = indexes.size();
        logger.exit(result);
        return result;
    }


    @Override
    public final boolean containsElement(Association<K, V> element) {
        logger.entry();
        boolean result = indexes.containsKey(element.key);
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


    @Override
    public final Association<K, V> getElementAtIndex(int index) {
        logger.entry(index);
        index = normalizedIndex(index);
        Iterator<Association<K, V>> iterator = new MapManipulator();
        iterator.goToIndex(index);
        Association<K, V> element = iterator.getNextElement();
        logger.exit(element);
        return element;
    }


    @Override
    public final int getIndexOfElement(Association<K, V> element) {
        logger.entry(element);
        int index = 0;
        Iterator<Association<K, V>> iterator = createDefaultIterator();
        while (iterator.hasNextElement()) {
            Association<K, V> association = iterator.getNextElement();
            index++;
            if (element.equals(association)) break;
        }
        logger.exit(index);
        return index;
    }


    @Override
    public final List<Association<K, V>> getElementsInRange(int firstIndex, int lastIndex) {
        logger.entry(firstIndex, lastIndex);
        firstIndex = normalizedIndex(firstIndex);
        lastIndex = normalizedIndex(lastIndex);
        List<Association<K, V>> result = new List<>();
        Iterator<Association<K, V>> iterator = createDefaultIterator();
        iterator.goToIndex(firstIndex);
        int numberOfElements = lastIndex - firstIndex + 1;
        while (numberOfElements-- > 0) {
            Association<K, V> element = iterator.getNextElement();
            logger.debug("Including element: {}", element);
            result.addElement(element);
        }
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
        K key = element.key;
        V value = element.value;
        if (!indexes.containsKey(element.key)) {
        associateKeyWithValue(key, value);
            result = true;
        }
        logger.exit(result);
        return result;
    }


    @Override
    public final boolean removeElement(Association<K, V> element) {
        logger.entry(element);
        boolean result = removeValueForKey(element.key) != null;
        logger.exit(result);
        return result;
    }


    @Override
    public final void removeAllElements() {
        logger.entry();
        indexes.clear();
        associations = null;
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
    public final List<K> getKeys() {
        logger.entry();
        List<K> keys = new List<>();
        Link<Association<K, V>> link = associations;
        for (int i = 0; i < indexes.size(); i++) {
            K key = link.value.key;
            logger.debug("Found key: {}", key);
            keys.addElement(key);
            link = link.next;
        }
        logger.exit(keys);
        return keys;
    }


    @Override
    public final List<V> getValues() {
        logger.entry();
        List<V> values = new List<>();
        Link<Association<K, V>> link = associations;
        for (int i = 0; i < indexes.size(); i++) {
            V value = link.value.value;
            logger.debug("Found value: {}", value);
            values.addElement(value);
            link = link.next;
        }
        logger.exit(values);
        return values;
    }


    @Override
    public final List<Association<K, V>> getAssociations() {
        logger.entry();
        List<Association<K, V>> results = new List<>();
        Link<Association<K, V>> link = associations;
        for (int i = 0; i < indexes.size(); i++) {
            Association<K, V> association = link.value;
            logger.debug("Found association: {}", association);
            results.addElement(association);
            link = link.next;
        }
        logger.exit();
        return results;
    }


    @Override
    public final V getValueForKey(K key) {
        logger.entry(key);
        V value = null;
        Link<Association<K, V>> link = indexes.get(key);
        if (link != null) {
            value = link.value.value;
            logger.debug("Found value: {} at key: {}", value, key);
        }
        logger.exit(value);
        return value;
    }


    @Override
    public final void associateKeyWithValue(K key, V value) {
        logger.entry(key, value);
        Link<Association<K, V>> link = indexes.get(key);
        if (link != null) {
            link.value.value = value;
        } else {
            Association<K, V> association = new Association<>(key, value);
            Link<Association<K, V>> newLink = new Link<>(association);
            if (associations == null) {
                // the map is currently empty
                newLink.previous = newLink;
                newLink.next = newLink;
                associations = newLink;
            } else {
                Link.insertBeforeLink(newLink, associations);
            }
            indexes.put(key, newLink);
        }
        logger.exit();
    }


    @Override
    public final V removeValueForKey(K key) {
        logger.entry(key);
        V value = null;
        Link<Association<K, V>> link = indexes.remove(key);
        if (link != null) {
            if (link == associations) {
                // the head link is about to be removed
                associations = link.next;
            }
            Link.removeLink(link);
            if (indexes.isEmpty()) {
                // the linked list is now empty
                associations = null;
            }
            value = link.value.value;
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
                result.associateKeyWithValue(key, value);
            }
        }
        logger.exit(result);
        return result;
    }


    /*
     * This manipulator class implements both the <code>Iterator</code> abstraction
     * and the <code>Manipulator</code> abstraction so it can be used as either depending
     * on how it is returned from the map.  It utilizes the iterator for the underlying
     * implementation.  Like most iterators, it should be used to access a map exclusively
     * without any other requests, especially requests that change the size of the map.
     */
    private class MapManipulator extends Manipulator<Association<K, V>> {

        private Link<Association<K, V>> currentLink = associations;
        private int currentIndex = 0;

        @Override
        public void goToStart() {
            logger.entry();
            currentLink = associations;
            logger.exit();
        }

        @Override
        public void goToIndex(int index) {
            logger.entry(index);
            index = normalizedIndex(index);
            if (currentIndex == index) return;
            if (currentIndex < index) {
                while (currentIndex++ < index) currentLink = currentLink.next;
            } else {
                while (currentIndex-- > index) currentLink = currentLink.previous;
            }
            logger.exit();
        }

        @Override
        public void goToEnd() {
            logger.entry();
            if (associations != null) {
                currentLink = associations.previous;
                currentIndex = indexes.size();
            }
            logger.exit();
        }

        @Override
        public boolean hasPreviousElement() {
            logger.entry();
            boolean result = currentIndex != 0;
            logger.exit(result);
            return result;
        }

        @Override
        public boolean hasNextElement() {
            logger.entry();
            boolean result = currentIndex < indexes.size();
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
            Association<K, V> result = currentLink.value;
            currentLink = currentLink.next;
            currentIndex++;
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
            currentIndex--;
            currentLink = currentLink.previous;
            Association<K, V> result = currentLink.value;
            logger.exit(result);
            return result;
        }

        @Override
        public void insertElement(Association<K, V> element) {
            logger.entry(element);
            K key = element.key;
            if (!indexes.containsKey(key)) {
                Link<Association<K, V>> newLink = new Link<>(element);
                if (associations == null) {
                    // the map is currently empty
                    newLink.previous = newLink;
                    newLink.next = newLink;
                    associations = newLink;
                } else {
                    Link.insertBeforeLink(newLink, currentLink);
                    if (associations == currentLink) associations = newLink;
                }
                currentLink = newLink;
                indexes.put(key, newLink);
            } else {
                String message = "Attempted to add a duplicate key with an iterator.";
                RuntimeException exception = new RuntimeException(message);
                logger.throwing(exception);
                throw exception;
            }
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
            if (associations == currentLink) associations = currentLink.next;
            Link<Association<K, V>> oldLink = currentLink;
            currentLink = currentLink.next;
            Link.removeLink(oldLink);
            Association<K, V> result = oldLink.value;
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
            Link<Association<K, V>> oldLink = currentLink.previous;
            if (associations == oldLink) associations = currentLink;
            Link.removeLink(oldLink);
            Association<K, V> result = oldLink.value;
            currentIndex--;
            logger.exit(result);
            return result;
        }

    }

}
