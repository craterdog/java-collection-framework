/************************************************************************
 * Copyright (c) Crater Dog Technologies(TM).  All Rights Reserved.     *
 ************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.        *
 *                                                                      *
 * This code is free software; you can redistribute it and/or modify it *
 * under the terms of The MIT License (MIT), as published by the Open   *
 * Source Initiative. (See http://opensource.org/licenses/MIT)          *
 ************************************************************************/
package craterdog.collections.primitives;

import craterdog.utils.UniversalHashFunction;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;


/**
 * This class provides an implementation of hash table that scales up and down geometrically
 * as the number of elements increases and decreases.  When the number of elements in the table
 * reaches its current capacity, it automatically doubles its capacity.  When the number of
 * elements drops below 1/4th of its current capacity, it automatically halves its capacity.
 * This ensures a hysteresis of 1/2 its capacity so that it won't oscillate up and down with
 * small changes in number of elements near a boundary condition.
 *
 * @author Derk Norton
 * @param <K> The type of keys in the table.
 * @param <V> The type of values in the table.
 */
public final class HashTable<K, V> extends AbstractMap<K, V> implements Map<K, V>, Cloneable {

    // the capacity cannot get any smaller than this value
    static private final int MINIMUM_CAPACITY = 16;

    // the current number of elements in the table
    private int size;

    // the storage for the elements, the size of the table must be a power of 2
    private DynamicArray<Entry<K, V>>[] table;

    // the number of bits required in the hash: log2(table.length)
    private int hashWidth = 4;  // starts out at: log2(MINIMUM_CAPACITY)

    // the universal hash function for this table
    private UniversalHashFunction function;

    // a set providing a "live view" into the entries that are in this hash table
    private final EntrySet entries = new EntrySet();

    /**
     * This default constructor creates an instance of a hash table with the minimum
     * capacity (16 elements).
     */
    public HashTable() {
        this(MINIMUM_CAPACITY);
    }


    /**
     * This constructor creates an instance of a hash table with at least the specified
     * minimum capacity.  The actual capacity will be a power of two that is greater or
     * equal to the specified minimum capacity.
     *
     * @param minimumCapacity The minimum initial size of the table.
     */
    public HashTable(int minimumCapacity) {
        int actualSize = MINIMUM_CAPACITY;
        while (actualSize < minimumCapacity) {
            actualSize <<= 1;
            hashWidth++;
        }  // make sure it is a power of two
        this.table = createTable(actualSize);
        this.function = new UniversalHashFunction(hashWidth);
        this.size = 0;  // no elements in the table yet
    }


    /**
     * This constructor creates an instance of a hash table that contains the elements from
     * the specified collection.
     *
     * @param elements The elements that should be used to seed the table.
     */
    public HashTable(Map<? extends K, ? extends V> elements) {
        this(elements.size());
        for (Entry<? extends K, ? extends V> entry : elements.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }


    @Override
    public int size() {
        return size;
    }


    @Override
    public void clear() {
        Arrays.fill(table, null);  // assist with garbage collection
        table = createTable(MINIMUM_CAPACITY);
        hashWidth = 4;
        function = new UniversalHashFunction(hashWidth);
        size = 0;  // no elements in the table yet
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public boolean containsKey(Object key) {
        int hash = function.hashValue(key);
        DynamicArray<Entry<K, V>> bucket = table[hash];
        for (Entry<K, V> entry : bucket) {
            K entryKey = entry.getKey();
            if (entryKey != null && entryKey.equals(key)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean containsValue(Object value) {
        for (DynamicArray<Entry<K, V>> bucket : table) {
            for (Entry<K, V> entry : bucket) {
                V entryValue = entry.getValue();
                if (entryValue != null && entryValue.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public V get(Object key) {
        int hash = function.hashValue(key);
        DynamicArray<Entry<K, V>> bucket = table[hash];
        for (Entry<K, V> entry : bucket) {
            K entryKey = entry.getKey();
            if (entryKey != null && entryKey.equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }


    @Override
    public V put(K key, V value) {
        if (size == table.length) doubleCapacity();
        int hash = function.hashValue(key);
        DynamicArray<Entry<K, V>> bucket = table[hash];
        for (Entry<K, V> entry : bucket) {
            K entryKey = entry.getKey();
            if (entryKey != null && entryKey.equals(key)) {
                V oldValue = entry.setValue(value);
                return oldValue;
            }
        }
        bucket.add(new SimpleEntry<>(key, value));
        size++;
        return null;
    }


    @Override
    public V remove(Object key) {
        int hash = function.hashValue(key);
        DynamicArray<Entry<K, V>> bucket = table[hash];
        for (int i = 0; i < bucket.size(); i++) {
            Entry<K, V> entry = bucket.get(i);
            K entryKey = entry.getKey();
            if (entryKey != null && entryKey.equals(key)) {
                bucket.remove(i);
                if (--size < table.length >>> 2) halveCapacity();  // less than 1/4th to ensure hysteresis
                return entry.getValue();
            }
        }
        return null;
    }


    @Override
    public Set<Entry<K, V>> entrySet() {
        return entries;
    }


    @Override
    @SuppressWarnings("unchecked")
    public Object clone() {
        try {
            HashTable<K, V> copy = (HashTable<K, V>) super.clone();
            int length = table.length;
            copy.table = Arrays.copyOf(table, length);
            for (int i = 0; i < length; i++) {
                copy.table[i] = (DynamicArray<Entry<K, V>>) table[i].clone();
            }
            return copy;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }


    private DynamicArray<Entry<K, V>>[] createTable(int numberOfBuckets) {
        @SuppressWarnings({"rawtypes", "unchecked"})
        DynamicArray<Entry<K, V>>[] newTable = (DynamicArray<Entry<K, V>>[]) new DynamicArray[numberOfBuckets];
        for (int i = 0; i < numberOfBuckets; i++) {
            newTable[i] = new DynamicArray<>();
        }
        return newTable;
    }


    private void doubleCapacity() {
        DynamicArray<Entry<K, V>>[] newTable = createTable(table.length << 1);  // multiply current length by 2
        function = new UniversalHashFunction(++hashWidth);
        rehashTo(newTable);
    }


    private void halveCapacity() {
        if (table.length == MINIMUM_CAPACITY) return;  // make sure we don't shrink too much
        DynamicArray<Entry<K, V>>[] newTable = createTable(table.length >>> 1);  // divide current length by 2
        function = new UniversalHashFunction(--hashWidth);
        rehashTo(newTable);
    }


    private void rehashTo(DynamicArray<Entry<K, V>>[] newTable) {
        for (DynamicArray<Entry<K, V>> array : table) {
            for (Entry<K, V> entry : array) {
                int hash = function.hashValue(entry.getKey());
                newTable[hash].add(entry);
            }
        }
        Arrays.fill(table, null);  // to aid in garbage collection
        table = newTable;
    }


    /*
    This set class provides a set "view" on the entries that are stored in the hash table.  It
    is a live view so all modifications made on the set directly affect the hash table and its
    entries.
    */
    private final class EntrySet extends AbstractSet<Entry<K,V>> {

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean contains(Object object) {
            if (!(object instanceof Entry)) return false;
            @SuppressWarnings("unchecked")
            Entry<K,V> entry = (Entry<K,V>) object;
            V value = HashTable.this.get(entry.getKey());
            return value != null && value.equals(entry.getValue());
        }

        @Override
        public boolean remove(Object object) {
            if (!(object instanceof Entry)) return false;
            @SuppressWarnings("unchecked")
            Entry<K,V> entry = (Entry<K,V>) object;
            return HashTable.this.remove(entry.getKey()) != null;
        }

        @Override
        public void clear() {
            HashTable.this.clear();
        }

        @Override
        public Iterator<Entry<K,V>> iterator() {
            return new EntryIterator();
        }

    }


    /*
    This iterator supports the EntrySet class above by operating on the hash table entries
    directly.
    */
    private final class EntryIterator implements Iterator<Entry<K, V>> {

        int tableIndex = 0;
        int bucketIndex = 0;
        int lastTableIndex = -1;
        int lastBucketIndex = -1;

        @Override
        public boolean hasNext() {
            while (tableIndex < table.length) {
                DynamicArray<Entry<K, V>> bucket = table[tableIndex];
                if (bucketIndex < bucket.size()) {
                    return true;
                }
                tableIndex++;
                bucketIndex = 0;
            }
            return false;
        }

        @Override
        public Entry<K, V> next() {
            if (!hasNext()) throw new NoSuchElementException();
            lastTableIndex = tableIndex;
            lastBucketIndex = bucketIndex;
            DynamicArray<Entry<K, V>> bucket = table[tableIndex];
            Entry<K, V> entry = bucket.get(bucketIndex++);
            return entry;
        }

        @Override
        public void remove() {
            if (lastBucketIndex < 0) throw new IllegalStateException();
            DynamicArray<Entry<K, V>> bucket = table[lastTableIndex];
            bucket.remove(lastBucketIndex);
            // NOTE: do not rehash the table here!
            tableIndex = lastTableIndex;
            bucketIndex = lastBucketIndex;
            lastTableIndex = -1;
            lastBucketIndex = -1;
        }

    }

}
