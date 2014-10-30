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
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;


/**
 * This class provides an implementation of a doubly linked list.
 *
 * @author Derk Norton
 * @param <E> The type of the elements in the linked list.
 */
public final class LinkedList<E> extends AbstractCollection<E> implements List<E>, RandomAccess, Cloneable {

    // the current number of elements in the list
    private int size = 0;

    // the head of the list
    private Link head = null;

    /**
     * This default constructor creates an empty instance of a linked list.
     */
    public LinkedList() {
    }


    /**
     * This constructor creates an instance of a linked list that contains the elements from
     * the specified collection.
     *
     * @param elements The elements that should be used to seed the list.
     */
    public LinkedList(Collection<? extends E> elements) {
        for (E element : elements) {
            this.add(element);
        }
    }


    @Override
    public int size() {
        return size;
    }


    @Override
    public int indexOf(Object element) {
        ListIterator<E> iterator = listIterator();
        for (int index = 0; index < size; index++) {
            E candidate = iterator.next();
            if (candidate == null ? element == null : candidate.equals(element)) return index;
        }
        return -1;  // not found
    }


    @Override
    public int lastIndexOf(Object element) {
        ListIterator<E> iterator = listIterator(size);
        for (int index = size - 1; index >= 0; index--) {
            E candidate = iterator.previous();
            if (candidate == null ? element == null : candidate.equals(element)) return index;
        }
        return -1;  // not found
    }


    @Override
    public E get(int index) {
        Link link = getLinkAtIndex(index);
        E element = link.value;
        return element;
    }


    @Override
    public E set(int index, E element) {
        Link link = getLinkAtIndex(index);
        E oldElement = link.value;
        link.value = element;
        return oldElement;
    }


    @Override
    public boolean add(E element) {
        add(size, element);
        return true;
    }


    @Override
    public void add(int index, E newElement) {
        if (size == 0) {
            insertElementIntoEmptyList(newElement);
        } else if (index == size) {
            appendElementToList(newElement);
        } else {
            Link existingLink = getLinkAtIndex(index);
            insertElementIntoListBeforeLink(existingLink, newElement);
        }
    }


    @Override
    public boolean addAll(int index, Collection<? extends E> collection) {
        ListIterator<E> iterator = listIterator(index);
        for (E element : collection) {
            iterator.add(element);
        }
        return !collection.isEmpty();
    }


    @Override
    public E remove(int index) {
        Link existingLink = getLinkAtIndex(index);
        E element = existingLink.value;
        removeLinkFromList(existingLink);
        return element;
    }


    /**
     * This method removes the elements in the specified range [firstIndex..lastIndex) and shifts
     * the existing elements down to fill in the gap.  This method returns a new list of the
     * elements that were removed.
     *
     * @param firstIndex The index of the first element to be removed.
     * @param lastIndex The index of the last element after the range to be removed.
     * @return The elements that were removed from the list.
     */
    public LinkedList<E> remove(int firstIndex, int lastIndex) {
        int numberRemoved = lastIndex - firstIndex;
        LinkedList<E> results = new LinkedList<>();

        if (numberRemoved == size) {
            // remove all links
            results.head = head;
            results.size = size;
            head = null;
            size = 0;
        } else {
            // retrieve the links
            Link firstLink = getLinkAtIndex(firstIndex);
            Link lastLink = getLinkAtIndex(lastIndex);  // the link past the range

            // remove the links in the range
            Link temp = lastLink.previous;
            firstLink.previous.next = lastLink;
            lastLink.previous.next = firstLink;
            lastLink.previous = firstLink.previous;
            firstLink.previous = temp;
            if (firstIndex == 0) head = lastLink;
            size -= numberRemoved;

            // initialize the new linked list
            results.head = firstLink;
            results.size = numberRemoved;
        }

        return results;
    }


    @Override
    public boolean remove(Object object) {
        // check for an empty list
        if (size == 0) return false;

        // search the list for the element
        Link link = head;
        do {
            if (link.value == null ? object == null : link.value.equals(object)) {
                removeLinkFromList(link);
                return true;
            }
            link = link.next;
        } while (link != head);

        return false;
    }


    @Override
    public void clear() {
        if (head != null) {
            // break the chain to help the garbage collector
            head.previous.next = null;
            head.previous = null;
            size = 0;
        }
    }


    @Override
    public ListIterator<E> iterator() {
        return new LinkedListIterator();
    }


    @Override
    public ListIterator<E> listIterator() {
        return new LinkedListIterator();
    }


    @Override
    public ListIterator<E> listIterator(int index) {
        return new LinkedListIterator(index);
    }


    @Override
    public LinkedList<E> subList(int fromIndex, int toIndex) {
        int numberOfElements = toIndex - fromIndex;
        LinkedList<E> results = new LinkedList<>();
        ListIterator<E> iterator = listIterator(fromIndex);
        for (int i = 0; i < numberOfElements; i++) {
            E element = iterator.next();
            results.add(element);
        }
        return results;
    }


    @Override
    // NOTE: Only ordered collections whose elements are in the same order will be equal.
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!(object instanceof List)) return false;
        List<?> that = (List<?>) object;
        if (this.size != that.size()) return false;
        ListIterator<E> e1 = this.listIterator();
        ListIterator<?> e2 = that.listIterator();
        while(e1.hasNext()) {
            E element1 = e1.next();
            Object element2 = e2.next();
            if (!(element1 == null ? element2 == null : element1.equals(element2))) return false;
        }
        return true;
    }


    @Override
    // NOTE: Only ordered collections whose elements are in the same order will have equal hash codes.
    public int hashCode() {
        int hashCode = 1;
        for (E element : this)
            hashCode = 31 * hashCode + (element == null ? 0 : element.hashCode());
        return hashCode;
    }


    @Override
    public Object clone() {
        try {
            @SuppressWarnings("unchecked")
            LinkedList<E> copy = (LinkedList<E>) super.clone();
            copy.head = null;
            copy.size = 0;
            for (E element : this) {
                copy.add(element);
            }
            return copy;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }


    private final class LinkedListIterator implements ListIterator<E> {

        int index;
        Link link;
        Link lastLink;

        private LinkedListIterator() {
            this.index = 0;
            this.link = head;
            this.lastLink = null;
        }

        private LinkedListIterator(int index) {
            this.index = index;
            this.link = LinkedList.this.getLinkAtIndex(index);
            this.lastLink = null;
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
            E element = link.value;
            lastLink = link;
            link = link.next;
            index++;
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
            link = link.previous;
            lastLink = link;
            E element = link.value;
            index--;
            return element;
        }

        @Override
        public void add(E element) {
            if (size == 0) {
                insertElementIntoEmptyList(element);
            } else if (index == size) {
                appendElementToList(element);
            } else {
                insertElementIntoListBeforeLink(link, element);
            }
            lastLink = null;
        }

        @Override
        public void remove() {
            if (lastLink == null) throw new IllegalStateException();
            removeLinkFromList(lastLink);
            lastLink = null;
        }

        @Override
        public void set(E element) {
            if (lastLink == null) throw new IllegalStateException();
            lastLink.value = element;
        }

    }


    // the structure to hold each element in the list
    private class Link {
        E value;
        Link previous;
        Link next;
        Link(E value) {
            this.value = value;
        }
    }


    private Link getLinkAtIndex(int index) {
        Link link = head;
        if (index < size / 2) {
            for (int i = 0; i < index; i++) {
                link = link.next;
            }
        } else {  // it is in the second half
            for (int i = size; i > index; i--) {
                link = link.previous;
            }
        }
        return link;
    }


    private void insertElementIntoEmptyList(E element) {
        head = new Link(element);
        head.next = head;
        head.previous = head;
        size = 1;
    }


    private void appendElementToList(E element) {
        Link newLink = new Link(element);
        newLink.next = head;
        newLink.previous = head.previous;
        head.previous.next = newLink;
        head.previous = newLink;
        size++;
    }


    private void insertElementIntoListBeforeLink(Link existingLink, E element) {
        Link newLink = new Link(element);
        newLink.next = existingLink;
        newLink.previous = existingLink.previous;
        existingLink.previous.next = newLink;
        existingLink.previous = newLink;
        if (head == existingLink) head = newLink;
        size++;
    }

    private void removeLinkFromList(Link existingLink) {
        if (head == existingLink) head = existingLink.next;
        existingLink.previous.next = existingLink.next;
        existingLink.next.previous = existingLink.previous;
        existingLink.previous = null;
        existingLink.next = null;
        if (--size == 0) head = null;
    }

}
