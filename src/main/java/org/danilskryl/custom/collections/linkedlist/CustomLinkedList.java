package org.danilskryl.custom.collections.linkedlist;

import org.danilskryl.custom.collections.CustomList;

import java.util.Arrays;
import java.util.Iterator;

/**
 * CustomLinkedList is a custom implementation of a linked list that
 * implements the CustomList and Iterable interfaces.
 *
 * @param <E> the type of elements in the linked list
 * @author Danil Skryl
 * @see org.danilskryl.custom.collections.arraylist.CustomArrayList
 * @see CustomList
 */
public class CustomLinkedList<E> implements CustomList<E>, Iterable<E> {
    private Element<E> first;
    private Element<E> last;
    private int size;

    /**
     * Constructs an empty linked list.
     */
    public CustomLinkedList() {
    }

    /**
     * Returns a string representation of the linked list.
     *
     * @return a string representation of the linked list
     */
    @Override
    public String toString() {
        Object[] elements = new Object[size];
        int count = 0;
        for (Element<E> i = first; i != null; i = i.next) {
            elements[count] = i;
            count++;
        }
        return Arrays.toString(elements);
    }

    /**
     * Adds the specified element to the end of the linked list.
     *
     * @param e the element to be added
     */
    @Override
    public void add(E e) {
        Element<E> lastElement = last;
        Element<E> newElement = new Element<>(e, null, lastElement);
        last = newElement;

        if (lastElement == null)
            first = newElement;
        else
            lastElement.next = newElement;

        incrementSize();
    }

    /**
     * Inserts the specified element at the specified position in the linked list.
     *
     * @param e     the element to be added
     * @param index the position at which the element is to be inserted
     * @throws IllegalArgumentException if the index is out of bounds
     */
    @Override
    public void add(E e, int index) {
        checkIndex(index);
        if (size == 0 && index != 0)
            throw new IllegalArgumentException();

        Element<E> currentElement = first;

        for (int i = 1; i < index; i++) {
            currentElement = currentElement.next;
        }

        Element<E> newElement = new Element<>(e, currentElement.next, currentElement);
        if (currentElement.next != null) {
            currentElement.next.previous = newElement;
        } else {
            last = newElement;
        }
        currentElement.next = newElement;

        incrementSize();
    }

    /**
     * Removes the element at the specified position in the linked list.
     *
     * @param index the position of the element to be removed
     * @throws IllegalArgumentException if the index is out of bounds
     */
    @Override
    public void remove(int index) {
        checkIndex(index);

        if (index == 0) {
            first = first.next;
            if (first != null) {
                first.previous = null;
            }
            decrementSize();
            return;
        }

        Element<E> currentElement = first;

        for (int i = 0; i < index; i++) {
            currentElement = currentElement.next;
        }

        currentElement.previous.next = currentElement.next;

        if (currentElement.next != null) {
            currentElement.next.previous = currentElement.previous;
        } else {
            last = currentElement.previous;
        }

        decrementSize();
    }

    /**
     * Removes the first occurrence of the specified element from the linked list.
     *
     * @param e the element to be removed
     * @throws IllegalArgumentException if the element is not found
     */
    @Override
    public void remove(E e) {
        Element<E> currentElement = first;

        while (currentElement != null) {
            if (currentElement.element.equals(e)) {
                if (currentElement.previous != null) {
                    currentElement.previous.next = currentElement.next;
                } else {
                    first = currentElement.next;
                }

                if (currentElement.next != null) {
                    currentElement.next.previous = currentElement.previous;
                } else {
                    last = currentElement.previous;
                }

                decrementSize();
                return;
            }

            currentElement = currentElement.next;
        }

        throw new IllegalArgumentException("Element not found: " + e);
    }

    /**
     * Returns the element at the specified position in the linked list.
     *
     * @param index the position of the element to be retrieved
     * @return the element at the specified position
     * @throws IllegalArgumentException if the index is out of bounds
     */
    @Override
    public E get(int index) {
        checkIndex(index);

        Element<E> element = first;
        for (int i = 0; i < index; i++) {
            element = element.next;
        }
        return element.element;
    }

    /**
     * Removes all elements from the linked list.
     */
    @Override
    public void clear() {
        // Memory leak??
        first = null;
        last = null;
        size = 0;
    }

    /**
     * Returns the number of elements in the linked list.
     *
     * @return the number of elements in the linked list
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Increments the number of elements in the linked list
     */
    public void incrementSize() {
        size++;
    }

    /**
     * Decrements the number of elements in the linked list
     */
    public void decrementSize() {
        size--;
    }

    /**
     * Sorts the linked list in ascending order using a simple selection sort algorithm.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void sort() {
        for (Element<E> i = first; i != null; i = i.next) {
            Element<E> min = i;
            for (Element<E> j = i.next; j != null; j = j.next) {
                if (((Comparable<E>) j.element).compareTo(min.element) < 0) {
                    min = j;
                }
            }
            swap(i, min);
        }
    }

    /**
     * Swaps two elements in the linked list.
     *
     * @param a the first element to be swapped
     * @param b the second element to be swapped
     */
    private void swap(Element<E> a, Element<E> b) {
        if (a != b) {
            E temp = a.element;
            a.element = b.element;
            b.element = temp;
        }
    }

    /**
     * Returns an iterator over the elements in the linked list.
     *
     * @return an iterator over the elements in the linked list
     */
    @Override
    public Iterator<E> iterator() {
        return new CustomLinkedListIterator();
    }

    /**
     * Checks if the given index is within the bounds of the linked list.
     *
     * @param index the index to be checked
     * @throws ArrayIndexOutOfBoundsException if the index is out of bounds
     */
    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new ArrayIndexOutOfBoundsException("Size of list is " + size + ". Your index is " + index);
    }

    /**
     * Converts the linked list to an array.
     *
     * @return an array containing all the elements in the linked list
     */
    public Object[] toArray() {
        Object[] array = new Object[size];
        int index = 0;
        for (Element<E> current = first; current != null; current = current.next) {
            array[index++] = current.element;
        }
        return array;
    }

    /**
     * Represents a single element in the linked list.
     *
     * @param <E> the type of the element
     */
    private static class Element<E> {
        E element;
        Element<E> next;
        Element<E> previous;

        /**
         * Constructs an Element with the specified element, next, and previous references.
         *
         * @param element  the data element
         * @param next     reference to the next element
         * @param previous reference to the previous element
         */
        public Element(E element, Element<E> next, Element<E> previous) {
            this.element = element;
            this.next = next;
            this.previous = previous;
        }

        /**
         * Returns a string representation of the element.
         *
         * @return a string representation of the element
         */
        @Override
        public String toString() {
            return String.valueOf(element);
        }
    }

    /**
     * CustomLinkedListIterator is an iterator for traversing the linked list.
     */
    private class CustomLinkedListIterator implements Iterator<E> {
        private Element<E> current = first;

        /**
         * Checks if there are more elements in the iteration.
         *
         * @return true if there are more elements, false otherwise
         */
        @Override
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element
         */
        @Override
        public E next() {
            E value = current.element;
            current = current.next;
            return value;
        }
    }
}
