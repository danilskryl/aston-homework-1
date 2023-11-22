package org.danilskryl.custom.collections.arraylist;

import org.danilskryl.custom.collections.CustomList;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * CustomArrayList is an implementation of the CustomList interface using an array to store elements.
 * It also implements the Iterable interface, allowing for enhanced iteration capabilities.
 *
 * @param <E> the type of elements in this list
 * @author Danil Skryl
 * @see CustomList
 * @see org.danilskryl.custom.collections.linkedlist.CustomLinkedList
 */
public class CustomArrayList<E> implements CustomList<E>, Iterable<E> {
    /**
     * The array to store elements
     */
    private Object[] data;
    /**
     * The initial capacity of the data array.
     */
    private static final int INITIAL_CAPACITY_DATA = 8;
    /**
     * The current size of list
     */
    private int size;

    /**
     * Constructor without parameters with an initial capacity of 8
     */
    public CustomArrayList() {
        data = new Object[INITIAL_CAPACITY_DATA];
    }

    /**
     * Constructor with initial capacity
     *
     * @param capacity the initial capacity of the list
     * @throws IllegalArgumentException if capacity is negative
     */
    public CustomArrayList(int capacity) {
        if (capacity < 0)
            throw new IllegalArgumentException("Capacity must be greater than 0");

        data = new Object[capacity];
    }

    /**
     * Adds element to the end of list
     *
     * @param e the element to be added to the list
     */
    @Override
    public void add(E e) {
        if (size == data.length)
            increase();

        data[size] = e;
        incrementSize();
    }

    /**
     * Insert element at the specified position in this list
     *
     * @param e     the element to be added to the list
     * @param index the index at which the specified element is to be inserted
     */
    @Override
    public void add(E e, int index) {
        if (size == data.length)
            increase();
        checkIndex(index);

        for (int i = size - 1; i >= index; i--) {
            data[i + 1] = data[i];
        }

        data[index] = e;
        incrementSize();
    }

    /**
     * Removes the element at the specified position in this list.
     *
     * @param index the index of the element to be removed
     */
    @Override
    public void remove(int index) {
        checkIndex(index);

        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }

        data[size - 1] = null;
        decrementSize();
    }

    /**
     * Removes the first occurrence of the specified element from this list.
     *
     * @param e the element to be removed
     * @throws NoSuchElementException if the specified element is not found in the list
     */
    @Override
    public void remove(E e) {
        int index = indexOf(e);

        if (index == -1)
            throw new NoSuchElementException("Object not found");

        remove(index);
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index the index of the element to return
     * @return the element at the specified position in this list
     */
    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        checkIndex(index);

        return (E) data[index];
    }

    /**
     * Removes all elements from this list.
     */
    @Override
    public void clear() {
        data = new Object[INITIAL_CAPACITY_DATA];
        size = 0;
    }

    /**
     * Returns a string representation of this list.
     *
     * @return a string representation of this list
     */
    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOf(data, size));
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Sorts the elements of the list in their hashcode order using the Selection Sort algorithm.
     */
    @Override
    public void sort() {
        int n = size;

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;

            for (int j = i + 1; j < n; j++) {
                if (compare(j, minIndex) < 0) {
                    minIndex = j;
                }
            }

            swap(i, minIndex);
        }
    }

    /**
     * Compares elements in the list.
     *
     * @param index1 the index of the first element to compare
     * @param index2 the index of the second element to compare
     * @return a negative integer if the first element is less than the second,
     * zero if they are equal, and a positive integer if the first element is greater.
     */
    @SuppressWarnings("unchecked")
    private int compare(int index1, int index2) {
        E element1 = (E) data[index1];
        E element2 = (E) data[index2];

        return Integer.compare(element1.hashCode(), element2.hashCode());
    }

    /**
     * Swaps two elements in the list.
     *
     * @param index1 the index of the first element
     * @param index2 the index of the second element
     */
    @SuppressWarnings("unchecked")
    private void swap(int index1, int index2) {
        E temp = (E) data[index1];
        data[index1] = data[index2];
        data[index2] = temp;
    }

    /**
     * Returns the index of the first occurrence of the specified element in this list,
     * or -1 if this list does not contain the element.
     *
     * @param e the element to search for
     * @return the index of the first occurrence of the element, or -1 if not found
     */
    private int indexOf(E e) {
        for (int i = 0; i < size - 1; i++) {
            if (data[i].equals(e))
                return i;
        }

        return -1;
    }

    /**
     * Decrements the size of the list by 1.
     */
    private void decrementSize() {
        size--;
    }

    /**
     * Increments the size of the list by 1.
     */
    private void incrementSize() {
        size++;
    }

    /**
     * Increases the capacity of the list by 50%.
     */
    private void increase() {
        data = Arrays.copyOf(data, data.length + (data.length / 2));
    }

    /**
     * Checks if the specified index is within the bounds of the list.
     *
     * @param index the index to be checked
     * @throws ArrayIndexOutOfBoundsException if the index is out of bounds
     */
    private void checkIndex(int index) {
        if (index < 0 || index >= data.length)
            throw new ArrayIndexOutOfBoundsException("Size of list is " + size + ". Your index is " + index);
    }

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * @return an iterator over the elements in this list
     */
    @Override
    public Iterator<E> iterator() {
        return new CustomArrayListIterator();
    }

    /**
     * CustomListIterator is an iterator for the CustomArrayList.
     */
    private class CustomArrayListIterator implements Iterator<E> {
        /**
         * The current index of the iterator.
         */
        private int currentIndex = 0;

        /**
         * Returns true if the iteration has more elements.
         *
         * @return true if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         */
        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            return (E) data[currentIndex++];
        }

        /**
         * Removes from the underlying collection the last element returned by this iterator.
         * This method can be called only once per call to next().
         *
         * @throws IllegalStateException if the next method has not yet been called,
         *                               or the remove method has already been called after the last call to the next method
         */
        @Override
        public void remove() {
            if (currentIndex > 0) {
                CustomArrayList.this.remove(currentIndex - 1);
                currentIndex--;
            } else
                throw new IllegalStateException();
        }
    }
}