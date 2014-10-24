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

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;


/**
 * This class provides an implementation of dynamic array that scales up and down geometrically
 * as the number of elements increases and decreases.  When the number of elements in the array
 * reaches its current capacity, it automatically doubles its capacity.  When the number of
 * elements drops below 1/4th of its current capacity, it automatically halves its capacity.
 * This ensures a hysteresis of 1/2 its capacity so that it won't oscillate up and down with
 * small changes in number of elements near a boundary condition.
 *
 * @author Derk Norton
 * @param <E> The type of the elements in the array.
 */
public final class DynamicArray<E> extends AbstractCollection<E> implements List<E> {

    // the capacity cannot get any smaller than this value
    static private final int MINIMUM_CAPACITY = 16;

    // the current number of elements in the array
    private int size;

    // the storage for the elements
    private Object[] array;


    /**
     * This default constructor creates an instance of a dynamic array with the minimum
     * capacity (16 elements).
     */
    public DynamicArray() {
        this(MINIMUM_CAPACITY);
    }


    /**
     * This constructor creates an instance of a dynamic array with at least the specified
     * minimum capacity.  The actual capacity will be a power of two that is greater or
     * equal to the specified minimum capacity.
     *
     * @param minimumCapacity The minimum initial size of the array.
     */
    public DynamicArray(int minimumCapacity) {
        int actualSize = MINIMUM_CAPACITY;
        while (actualSize < minimumCapacity) actualSize *= 2;  // make sure it is a power of two
        this.array = new Object[actualSize];
    }


    /**
     * This constructor creates an instance of a dynamic array that contains the elements from
     * the specified collection.
     *
     * @param elements The elements that should be used to seed the array.
     */
    public DynamicArray(Collection<? extends E> elements) {
        this(elements.size());
        for (E element : elements) {
            this.add(element);
        }
    }


    @Override
    public int size() {
        return size;
    }


    @Override
    public boolean contains(Object element) {
        @SuppressWarnings("unchecked")
        int index = indexOf((E) element);
        boolean result = index > -1;
        return result;
    }


    @Override
    public int indexOf(Object element) {
        for (int index = 0; index < size; index++) {
            @SuppressWarnings("unchecked")
            E candidate = (E) array[index];
            if (candidate == null) {
                if (element == null) return index;
            } else {
                if (candidate.equals(element)) return index;
            }
        }
        return -1;  // not found
    }


    @Override
    public int lastIndexOf(Object element) {
        for (int index = size - 1; index >= 0; index--) {
            @SuppressWarnings("unchecked")
            E candidate = (E) array[index];
            if (candidate == null) {
                if (element == null) return index;
            } else {
                if (candidate.equals(element)) return index;
            }
        }
        return -1;  // not found
    }


    @Override
    public E get(int index) {
        @SuppressWarnings("unchecked")
        E element = (E) array[index];
        return element;
    }


    @Override
    public E set(int index, E element) {
        @SuppressWarnings("unchecked")
        E oldElement = (E) array[index];
        array[index] = element;
        return oldElement;
    }


    @Override
    public boolean add(E newElement) {
        if (array.length == size) doubleCapacity();
        array[size++] = newElement;
        return true;
    }


    @Override
    public void add(int index, E element) {
        if (array.length == size) doubleCapacity();
        System.arraycopy(array, index, array, index + 1, size++ - index);
        array[index] = element;
    }


    @Override
    public E remove(int index) {
        @SuppressWarnings("unchecked")
        E element = (E) array[index];
        System.arraycopy(array, index + 1, array, index, --size - index);
        array[size] = null;
        if (size < array.length >> 2) halveCapacity();  // use 1/4th to ensure hysteresis
        return element;
    }


    /**
     * This method removes the element at the specified index and shifts the existing elements
     * down to fill in the gap.  This method returns the element that was removed.
     *
     * @param firstIndex The index of the first element to be removed.
     * @param numberOfElements The number of elements to be removed.
     * @return The elements that were removed from the array.
     */
    public DynamicArray<E> remove(int firstIndex, int numberOfElements) {
        DynamicArray<E> results = new DynamicArray<>(numberOfElements);
        for (int i = firstIndex; i < firstIndex + numberOfElements; i++) {
            @SuppressWarnings("unchecked")
            E element = (E) array[i];
            results.add(element);
        }
        System.arraycopy(array, firstIndex + numberOfElements, array, firstIndex, size - firstIndex - numberOfElements);
        Arrays.fill(array, size - numberOfElements, size, null);
        size -= numberOfElements;
        if (size < array.length / 4) halveCapacity();  // use 1/4th to ensure hysteresis
        return results;
    }


    @Override
    public boolean remove(Object oldElement) {
        @SuppressWarnings("unchecked")
        E element = (E) oldElement;
        int index = indexOf(element);
        if (index < 0) return false;
        remove(index);
        return true;
    }


    @Override
    public void clear() {
        Arrays.fill(array, null);
        size = 0;
    }


    @Override
    public ListIterator<E> iterator() {
        return new ArrayIterator();
    }


    @Override
    public boolean addAll(int index, Collection<? extends E> collection) {
        boolean result = !collection.isEmpty();
        ListIterator<E> iterator = iterator(index);
        for (E element : collection) {
            iterator.add(element);
        }
        return result;
    }


    @Override
    public ListIterator<E> listIterator() {
        return new ArrayIterator();
    }


    @Override
    public ListIterator<E> listIterator(int index) {
        return new ArrayIterator(index);
    }


    @Override
    public DynamicArray<E> subList(int fromIndex, int toIndex) {
        int newSize = toIndex - fromIndex + 1;
        DynamicArray<E> results = new DynamicArray<>(newSize);
        System.arraycopy(array, fromIndex, results.array, 0, newSize);
        return results;
    }


    /**
     * This method returns an iterator for the collection which is currently pointing
     * at the slot right before the specified index.
     *
     * @param index The index before the next element in the collection to be returned by the iterator.
     * @return A list iterator pointing at the slot before the element referenced by the specified index.
     */
    public ListIterator<E> iterator(int index) {
        return new ArrayIterator(index);
    }


    private void doubleCapacity() {
        Object[] newArray = new Object[array.length << 1];  // multiply current length by 2
        System.arraycopy(array, 0, newArray, 0, size);
        array = newArray;
    }


    private void halveCapacity() {
        if (array.length == MINIMUM_CAPACITY) return;  // make sure we don't shrink too much
        Object[] newArray = new Object[array.length >> 1];  // divide current length by 2
        System.arraycopy(array, 0, newArray, 0, size);
        array = newArray;
    }


    private final class ArrayIterator implements ListIterator<E> {

        int index;
        int lastIndex;

        private ArrayIterator() {
            this.index = 0;
            this.lastIndex = -1;
        }

        private ArrayIterator(int index) {
            this.index = index;
            this.lastIndex = -1;
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public E next() {
            if (index == size) throw new NoSuchElementException();
            E element = DynamicArray.this.get(lastIndex = index++);
            return element;
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public E previous() {
            if (index == 0) throw new NoSuchElementException();
            E element = DynamicArray.this.get(lastIndex = --index);
            return element;
        }

        @Override
        public void add(E element) {
            DynamicArray.this.add(lastIndex = index++, element);
        }

        @Override
        public void remove() {
            if (lastIndex < 0) throw new IllegalStateException();
            DynamicArray.this.remove(lastIndex);
            index = lastIndex;
            lastIndex = -1;
        }

        @Override
        public void set(E element) {
            if (lastIndex < 0) throw new IllegalStateException();
            DynamicArray.this.set(lastIndex, element);
        }

    }

}
