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

import org.apache.commons.lang.math.RandomUtils;


/**
 * This class implements the universal hash function algorithm described by
 * Dietzfelbinger, et al, (1997). "A Reliable Randomized Algorithm for the Closest-Pair Problem".
 * The algorithm provides a very efficient (mod free) way to create and use a universal hash
 * function that creates an evenly distributed hash value for hash tables that have a number of
 * buckets that are a power of two.  This function works even for data types that have poor builtin
 * hash functions like are often seen for strings.
 *
 * @author Derk Norton
 */
public final class UniversalHashFunction {

    private final int w;  // width of a word (defaults to int)
    private final int d;  // difference between word width and hash width
    private final int a;  // random positive even integer < 2^w
    private final int b;  // random non-negative integer < 2^d


    public UniversalHashFunction(int hashWidth) {
        this(Integer.SIZE, hashWidth);
    }


    public UniversalHashFunction(int wordWidth, int hashWidth) {
        this.w = wordWidth;
        this.d = wordWidth - hashWidth;
        this.a = (RandomUtils.nextInt() & 0x7FFFFFFF) | 0x00000001;  // make sure a is positive and odd
        this.b = RandomUtils.nextInt(1 << d);  // in range [0..2^d)
    }


    public int hashValue(Object value) {
        int x = value.hashCode();
        int hash = (a * x + b) >>> d;  // unsigned shift right
        return hash;
    }

}

