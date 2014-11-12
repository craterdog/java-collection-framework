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

import java.util.Comparator;


/**
 * This interface defines the methods that must be implemented by each collection that
 * supports an implicit ordering of its elements.
 *
 * @author Derk Norton
 * @param <E> The type of element managed by the collection.
 */
public interface Ordered<E> extends Iteratable<E> {

    /**
     * This method returns the comparator that is used to order the elements in this collection.
     *
     * @return The comparator that is used to order the elements in this collection.
     */
    Comparator<? super E> getComparator();

}
