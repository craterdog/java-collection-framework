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
import craterdog.collections.*;
import craterdog.core.Iterator;
import craterdog.core.Manipulator;
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
public abstract class AssociativeCollection<K, V> extends SortableCollection<Association<K, V>> {

    static private final XLogger logger = XLoggerFactory.getXLogger(AssociativeCollection.class);

    // a hash table mapping each association key to its corresponding link in the linked list
    private final HashTable<K, Link<Association<K, V>>> indexes = new HashTable<>();

    // a linked list containing the key value associations in their proper order
    private Link<Association<K, V>> associations = null;


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


    @JsonValue
    public final java.util.LinkedHashMap<K, V> toMap() {
        java.util.LinkedHashMap<K, V> map = new java.util.LinkedHashMap<>();
        for (Association<K, V> association : this) {
            K key = association.key;
            V value = association.value;
            map.put(key, value);
        }
        return map;
    }


    @Override
    public final int getSize() {
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
    public Iterator<Association<K, V>> createIterator() {
        logger.entry();
        Iterator<Association<K, V>> result = new MapManipulator();
        logger.exit(result);
        return result;
    }


    @Override
    public final Association<K, V> getElement(int index) {
        logger.entry(index);
        index = normalizedIndex(index);
        Iterator<Association<K, V>> iterator = new MapManipulator();
        iterator.toIndex(index);
        Association<K, V> element = iterator.getNext();
        logger.exit(element);
        return element;
    }


    @Override
    public final int getIndex(Association<K, V> element) {
        logger.entry(element);
        int index = 0;
        Iterator<Association<K, V>> iterator = createIterator();
        while (iterator.hasNext()) {
            Association<K, V> association = iterator.getNext();
            index++;
            if (element.equals(association)) break;
        }
        logger.exit(index);
        return index;
    }


    @Override
    public final List<Association<K, V>> getElements(int firstIndex, int lastIndex) {
        logger.entry(firstIndex, lastIndex);
        firstIndex = normalizedIndex(firstIndex);
        lastIndex = normalizedIndex(lastIndex);
        List<Association<K, V>> result = new List<>();
        Iterator<Association<K, V>> iterator = createIterator();
        iterator.toIndex(firstIndex);
        int numberOfElements = lastIndex - firstIndex + 1;
        while (numberOfElements-- > 0) {
            Association<K, V> element = iterator.getNext();
            logger.debug("Including element: {}", element);
            result.addElement(element);
        }
        logger.exit(result);
        return result;
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
        if (!indexes.containsKey(element.key)) {
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
        indexes.clear();
        associations = null;
        logger.exit();
    }


    @Override
    public Manipulator<Association<K, V>> createManipulator() {
        logger.entry();
        Manipulator<Association<K, V>> result = new MapManipulator();
        logger.exit(result);
        return result;
    }


    /**
     * This method returns the list of keys for the associations in this collection.
     *
     * @return The keys for this collection.
     */
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


    /**
     * This method returns the list of values for the associations in this collection.
     *
     * @return The values for this collection.
     */
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


    /**
     * This method returns the list of associations between keys and values for this collection.
     *
     * @return A list of the key/value associations that make up this collection.
     */
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


    /**
     * This method returns the value associated with the specified key.
     *
     * @param key The key for the value to be retrieved.
     * @return The value associated with the key.
     */
    public final V getValue(K key) {
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


    /**
     * This method associates in this collection a new value with a key.  If there is already
     * a value associated with the specified key, the new value replaces the old value.
     *
     * @param key The key for the new value.
     * @param value The new value to be associated with the key.
     */
    public final void setValue(K key, V value) {
        logger.entry(key, value);
        Link<Association<K, V>> link = indexes.get(key);
        if (link != null) {
            link.value.value = value;
        } else {
            Association<K, V> association = new Association<>(key, value);
            Link<Association<K, V>> newLink = new Link<>(association);
            if (associations == null) {
                // the associative collection is currently empty
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


    private class MapManipulator extends Manipulator<Association<K, V>> {

        private Link<Association<K, V>> currentLink = associations;
        private int currentIndex = 0;

        @Override
        public void toStart() {
            logger.entry();
            currentLink = associations;
            logger.exit();
        }

        @Override
        public void toIndex(int index) {
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
        public void toEnd() {
            logger.entry();
            if (associations != null) {
                currentLink = associations.previous;
                currentIndex = indexes.size();
            }
            logger.exit();
        }

        @Override
        public boolean hasPrevious() {
            logger.entry();
            boolean result = currentIndex != 0;
            logger.exit(result);
            return result;
        }

        @Override
        public boolean hasNext() {
            logger.entry();
            boolean result = currentIndex < indexes.size();
            logger.exit(result);
            return result;
        }

        @Override
        public Association<K, V> getNext() {
            logger.entry();
            if (!hasNext()) {
                IllegalStateException exception = new IllegalStateException("The iterator is at the end of the collection.");
                throw logger.throwing(exception);
            }
            Association<K, V> result = currentLink.value;
            currentLink = currentLink.next;
            currentIndex++;
            logger.exit(result);
            return result;
        }

        @Override
        public Association<K, V> getPrevious() {
            logger.entry();
            if (!hasPrevious()) {
                IllegalStateException exception = new IllegalStateException("The iterator is at the beginning of the collection.");
                throw logger.throwing(exception);
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
                    // the associative collection is currently empty
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
                throw logger.throwing(exception);
            }
            logger.exit();
        }

        @Override
        public Association<K, V> removeNext() {
            logger.entry();
            if (!hasNext()) {
                IllegalStateException exception = new IllegalStateException("The iterator is at the end of the collection.");
                throw logger.throwing(exception);
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
        public Association<K, V> removePrevious() {
            logger.entry();
            if (!hasPrevious()) {
                IllegalStateException exception = new IllegalStateException("The iterator is at the beginning of the collection.");
                throw logger.throwing(exception);
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
