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


/**
 * This abstract class defines a framework for each concrete iterator class
 * that allows the iteration over a collection's elements.  The iterator
 * points at the slots on either side of the elements in the collection:
 * <pre>
 *            [element 1]   [element 2]   [element 3]  ...  [element N]
 *          ^             ^             ^             ^   ^             ^
 *       at start                                                    at end
 * </pre>
 *
 * @author Derk Norton
 * @param <E> The type of the element being iterated over.
 */
public abstract class Iterator<E> implements java.util.Iterator<E> {

    /**
     * This method moves the iterator to just before the first element in this collection.
     * The next element to be returned will the first element in this collection.
     */
    public abstract void goToStart();

    /**
     * This method moves the iterator to the slot just before the specified index.  The
     * next element to be returned will then be the element with that index.
     *
     * @param index This index of element to be returned next by the iterator.
     */
    public abstract void goToIndex(int index);

    /**
     * This method moves the iterator to the slot just past the last element in this
     * collection.  There is no next element that can be returned from this position.
     */
    public abstract void goToEnd();

    /**
     * This method determines if the iterator is currently pointing at a slot just after
     * an element in this collection.  If the iterator is at the start of the collection
     * this method will return <code>false</code>.
     *
     * @return Whether or not there is an element just before the current position of
     * the iterator.
     */
    public abstract boolean hasPreviousElement();

    /**
     * This method determines if the iterator is currently pointing at a slot just before
     * an element in this collection.  If the iterator is at the end of the collection
     * this method will return <code>false</code>.
     *
     * @return Whether or not there is an element just after the current position of
     * the iterator.
     */
    public abstract boolean hasNextElement();

    /**
     * This method returns the element after the slot where the iterator is currently
     * pointing.  If the iterator is at the end of this collection an exception is thrown.
     *
     * @return The next element in this collection.
     */
    public abstract E getNextElement();

    /**
     * This method returns the element before the slot where the iterator is currently
     * pointing.  If the iterator is at the start of this collection an exception is thrown.
     *
     * @return The previous element in this collection.
     */
    public abstract E getPreviousElement();

    @Override
    public final boolean hasNext() {
        return hasNextElement();
    }

    @Override
    public final E next() {
        return getNextElement();
    }

    @Override
    public final void remove() {
        throw new UnsupportedOperationException("Modifying a collection with an iterator is not allowed.");
    }

}
