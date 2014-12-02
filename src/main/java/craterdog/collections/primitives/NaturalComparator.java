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

import java.util.Comparator;


/**
 * This class defines a comparator that compares two objects of the same type using their natural
 * <code>compareTo</code> method if the type implements the <code>Comparable</code> interface, or
 * by doing a lexical comparison on their string formats if not.  This comparator handles
 * <code>null</code> values as well.  A <code>null</code> value is always less than an non
 * <code>null</code> value and two <code>null</code> values are equal.
 *
 * @author Derk Norton
 * @param <T> The type of the objects being compared.
 */
public class NaturalComparator<T> implements Comparator<T> {

    @Override
    public int compare(T object1, T object2) {
        if (object1 != null) {
            if (object2 == null) return 1;  // the second object is null
            if (object1 instanceof Comparable) {
                // use the natural ordering
                @SuppressWarnings("unchecked")
                Comparable<T> comparable1 = (Comparable<T>) object1;
                return comparable1.compareTo(object2);
            } else {
                // use lexical ordering
                String string1 = object1.toString();
                String string2 = object2.toString();
                return string1.compareTo(string2);
            }
        }
        if (object2 != null) {
            return -1;  // first object is null
        }
        return 0;  // both objects are null
    }

}