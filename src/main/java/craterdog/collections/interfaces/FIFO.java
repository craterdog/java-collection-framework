/************************************************************************
 * Copyright (c) Crater Dog Technologies(TM).  All Rights Reserved.     *
 ************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.        *
 *                                                                      *
 * This code is free software; you can redistribute it and/or modify it *
 * under the terms of The MIT License (MIT), as published by the Open   *
 * Source Initiative. (See http://opensource.org/licenses/MIT)          *
 ************************************************************************/
package craterdog.collections.interfaces;


/**
 * This interface defines the methods that must be implemented by each collection that
 * supports a "first in, first out" access pattern.  This pattern is usually used by
 * multiple threads to implement a shared queue.
 *
 * @author Derk Norton
 * @param <E> The type of element managed by the collection.
 */
public interface FIFO<E> extends Iteratable<E> {

    /**
     * This method adds a new element to the tail of the queue.  If the queue
     * is currently at capacity this method will block until the capacity
     * drops.
     *
     * @param element The new element to be added.
     * @throws java.lang.InterruptedException The thread that was waiting to add an element
     * was interrupted.
     */
    void addTailElement(E element) throws InterruptedException;

    /**
     * This method removes the next element from the head of the queue.  If
     * the queue is empty, the method blocks until an element is available.
     *
     * @return The head element in the queue.
     * @throws java.lang.InterruptedException The thread that was waiting to remove an element
     * was interrupted.
     */
    E removeHeadElement() throws InterruptedException;

    /**
     * This method returns a reference to the element at the head of the queue
     * without removing the element from the queue.
     *
     * @return The head element in the queue, or null if the queue is empty.
     */
    E getHeadElement();

}
