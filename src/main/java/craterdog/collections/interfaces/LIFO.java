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
 * supports a "last in, first out" access pattern.  This pattern is usually used by
 * a single thread to implement a stack.
 *
 * @author Derk Norton
 * @param <E> The type of element managed by the collection.
 */
public interface LIFO<E> extends Iteratable<E> {

    /**
     * This method pushes a new element onto the top of the stack.
     *
     * @param element The new element to be added.
     */
    void pushTopElement(E element);

    /**
     * This method pops the top element off of the stack.  If the stack is empty
     * an exception is thrown.
     *
     * @return The top element from the stack.
     */
    E popTopElement();

    /**
     * This method returns a reference to the top element on the stack without
     * removing it from the stack.  If the stack is empty an exception is thrown.
     *
     * @return The top element on the stack.
     */
    E getTopElement();

}
