package org.danilskryl.custom.collections.linkedlist;

import org.danilskryl.custom.collections.CustomList;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
    private Node<E> first;
    private Node<E> last;
    private int size;

    /**
     * Returns a string representation of the linked list.
     *
     * @return a string representation of the linked list
     */
    @Override
    public String toString() {
        Object[] elements = new Object[size];
        int count = 0;
        for (Node<E> i = first; i != null; i = i.next) {
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
        Node<E> lastNode = last;
        Node<E> newNode = new Node<>(e, null, lastNode);
        last = newNode;

        if (lastNode == null) {
            first = newNode;
        } else {
            lastNode.next = newNode;
        }

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
        if (size == 0 && index != 0) {
            throw new IllegalArgumentException();
        }

        Node<E> currentNode = first;

        for (int i = 1; i < index; i++) {
            currentNode = currentNode.next;
        }

        Node<E> newNode = new Node<>(e, currentNode.next, currentNode);
        if (currentNode.next != null) {
            currentNode.next.previous = newNode;
        } else {
            last = newNode;
        }
        currentNode.next = newNode;

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

        Node<E> currentNode = first;

        for (int i = 0; i < index; i++) {
            currentNode = currentNode.next;
        }

        currentNode.previous.next = currentNode.next;

        if (currentNode.next != null) {
            currentNode.next.previous = currentNode.previous;
        } else {
            last = currentNode.previous;
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
        Node<E> currentNode = first;

        while (currentNode != null) {
            if (currentNode.element.equals(e)) {
                if (currentNode.previous != null) {
                    currentNode.previous.next = currentNode.next;
                } else {
                    first = currentNode.next;
                }

                if (currentNode.next != null) {
                    currentNode.next.previous = currentNode.previous;
                } else {
                    last = currentNode.previous;
                }

                decrementSize();
                return;
            }

            currentNode = currentNode.next;
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

        Node<E> node = first;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node.element;
    }

    /**
     * Removes all elements from the linked list.
     */
    @Override
    public void clear() {
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
        for (Node<E> i = first; i != null; i = i.next) {
            Node<E> min = i;
            for (Node<E> j = i.next; j != null; j = j.next) {
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
    private void swap(Node<E> a, Node<E> b) {
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
        if (index < 0 || index >= size) {
            throw new ArrayIndexOutOfBoundsException("Size of list is " + size + ". Your index is " + index);
        }
    }

    /**
     * Converts the linked list to an array.
     *
     * @return an array containing all the elements in the linked list
     */
    public Object[] toArray() {
        Object[] array = new Object[size];
        int index = 0;
        for (Node<E> current = first; current != null; current = current.next) {
            array[index++] = current.element;
        }
        return array;
    }

    /**
     * Represents a single element in the linked list.
     *
     * @param <E> the type of the element
     */
    private static class Node<E> {
        E element;
        Node<E> next;
        Node<E> previous;

        /**
         * Constructs an Element with the specified element, next, and previous references.
         *
         * @param element  the data element
         * @param next     reference to the next element
         * @param previous reference to the previous element
         */
        public Node(E element, Node<E> next, Node<E> previous) {
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
        private Node<E> current = first;

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
         * @throws NoSuchElementException - if the element doesn't exist
         */
        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E value = current.element;
            current = current.next;
            return value;
        }
    }
}
