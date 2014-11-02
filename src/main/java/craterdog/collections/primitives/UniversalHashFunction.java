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

    /**
     * This constructor creates a new hash function with the specified hash width and the default
     * word width (32 bits).
     *
     * @param hashWidth The number of bits required of the hash for the index size.  In general,
     * this will be the log base 2 of the number of buckets in the hash table (must be a power of
     * 2).
     */
    public UniversalHashFunction(int hashWidth) {
        this(Integer.SIZE, hashWidth);
    }


    /**
     * This constructor creates a new hash function with the specified hash width and the default
     * word width (32 bits).
     *
     * @param wordWidth The number of bits in the word size for the environment (32 for Java ints).
     * @param hashWidth The number of bits required of the hash for the index size.  In general,
     * this will be the log base 2 of the number of buckets in the hash table (must be a power of
     * 2).
     */
    public UniversalHashFunction(int wordWidth, int hashWidth) {
        this.w = wordWidth;
        this.d = wordWidth - hashWidth;
        this.a = (RandomUtils.nextInt() & 0x7FFFFFFF) | 0x00000001;  // make sure a is positive and odd
        this.b = RandomUtils.nextInt(1 << d);  // in range [0..2^d)
    }

    /**
     * This method generates a hash value for the specified object using the universal hash
     * function parameters specified in the constructor.  The result will be a hash value
     * containing the hash width number of bits.
     *
     * @param object The object to be hashed.
     * @return A universal hash of the object.
     */
    public int hashValue(Object object) {
        int x = object.hashCode();
        int hash = (a * x + b) >>> d;  // unsigned shift right
        return hash;
    }

}

