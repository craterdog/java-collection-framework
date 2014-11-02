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


/**
 * This class provides an implementation of a link that makes up a doubly linked list.  The
 * first element of the list is connected to the last element in the list to make a ring.
 *
 * @author Derk Norton
 * @param <T> The type of the elements in the linked list.
 */
public final class Link<T> implements Cloneable {

    /**
     * This attribute contains the value encapsulated by this link.
     */
    public T value;

    /**
     * This attribute points to the previous link in the list.
     */
    public Link<T> previous;

    /**
     * This attribute points to the next link in the list.
     */
    public Link<T> next;


    /**
     * This constructor takes a value and creates a <code>Link</code> that encapsulates it.
     *
     * @param value The value to be encapsulated in a link.
     */
    public Link(T value) {
        this.value = value;
        this.previous = null;
        this.next = null;
    }


    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Link)) return false;
        Link<?> that = (Link<?>) object;
        return this.value.equals(that.value);
    }


    @Override
    public int hashCode() {
        return value.hashCode();
    }


    @Override
    public Object clone() {
        try {
            @SuppressWarnings("unchecked")
            Link<T> copy = (Link<T>) super.clone();
            copy.previous = previous;
            copy.next = next;
            copy.value = value;
            return copy;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }


    /**
     * This utility method inserts a new link in a linked list before the specified existing link.
     *
     * @param <T> The type of element encapsulated by the link.
     * @param newLink The new link to be inserted.
     * @param existingLink The existing link before which the new link will be inserted.
     */
    static public <T> void insertBeforeLink(Link<T> newLink, Link<T> existingLink) {
        newLink.next = existingLink;
        newLink.previous = existingLink.previous;
        existingLink.previous.next = newLink;
        existingLink.previous = newLink;
    }


    /**
     * This utility method removes the specified link from a linked list.
     *
     * @param <T> The type of element encapsulated by the link.
     * @param link The link to be removed.
     */
    static public <T> void removeLink(Link<T>  link) {
        link.previous.next = link.next;
        link.next.previous = link.previous;
        link.previous = null;
        link.next = null;
    }


    /**
     * This utility method removes a set of links from a linked list, starting with the first
     * link and including the link before the lastLink. Note, that this means the last link
     * is not removed from the list.
     *
     * @param <T> The type of element encapsulated by the link.
     * @param firstLink The first link in the sub chain to be removed.
     * @param lastLink The link after the last link in the sub chain to be removed.
     */
    static public <T> void removeLinks(Link<T>  firstLink, Link<T>  lastLink) {
        Link<T>  temp = lastLink.previous;
        firstLink.previous.next = lastLink;
        lastLink.previous.next = firstLink;
        lastLink.previous = firstLink.previous;
        firstLink.previous = temp;
    }

}
