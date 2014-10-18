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

import craterdog.collections.abstractions.Iterator;
import craterdog.core.Composite;
import craterdog.core.Sequential;


/**
 * This interface defines the methods that must be implemented by each collection
 * whose elements can be iterated over.
 *
 * @author Derk Norton
 * @param <E> The type of element managed by the collection.
 */
public interface Iteratable<E> extends Sequential<E>, Composite {

    /**
     * This method creates a new default iterator for the collection.
     *
     * @return A new default iterator for the collection.
     */
    Iterator<E> createDefaultIterator();

    /**
     * This method removes all elements from the collection.
     */
    void removeAllElements();

}
