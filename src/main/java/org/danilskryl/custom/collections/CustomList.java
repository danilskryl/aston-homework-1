package org.danilskryl.custom.collections;

import java.util.Iterator;

/**
 * The CustomList interface represents a custom list data structure.
 * It defines basic operations for adding, removing, accessing elements,
 * and performing other common list operations.
 *
 * @param <E> the type of elements in the list
 * @author Danil Skryl
 * @see org.danilskryl.custom.collections.arraylist.CustomArrayList
 * @see org.danilskryl.custom.collections.linkedlist.CustomLinkedList
 */
public interface CustomList<E> {

    /**
     * Adds the specified element to the end of this list.
     *
     * @param e the element to be added to the list
     */
    void add(E e);

    /**
     * Inserts the specified element at the specified position in this list.
     *
     * @param e     the element to be added to the list
     * @param index the index at which the specified element is to be inserted
     */
    void add(E e, int index);

    /**
     * Removes the element at the specified position in this list.
     *
     * @param index the index of the element to be removed
     */
    void remove(int index);

    /**
     * Removes the first occurrence of the specified element from this list.
     *
     * @param e the element to be removed
     */
    void remove(E e);

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index the index of the element to return
     * @return the element at the specified position in this list
     */
    E get(int index);

    /**
     * Removes all elements from this list.
     */
    void clear();

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    int size();

    /**
     * Sorts the elements of this list in their natural order.
     */
    void sort();

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * @return an iterator over the elements in this list
     */
    Iterator<E> iterator();
}
