package org.danilskryl.custom.collections.linkedlist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomLinkedListTest {
    private CustomLinkedList<Integer> listIntegers;
    private CustomLinkedList<String> listStrings;
    private CustomLinkedList<Double> listDoubles;
    @BeforeEach
    void setUp() {
        listIntegers = new CustomLinkedList<>();
        listStrings = new CustomLinkedList<>();
        listDoubles = new CustomLinkedList<>();
    }

    @Test
    void testAdd() {
        listIntegers.add(1);
        listIntegers.add(2);
        listIntegers.add(3);

        assertArrayEquals(new Integer[]{1, 2, 3}, listIntegers.toArray());
    }

    @Test
    void testAddAtIndex() {
        listStrings.add("A");
        listStrings.add("B");
        listStrings.add("C");
        listStrings.add("D", 1);

        assertArrayEquals(new String[]{"A", "D", "B", "C"}, listStrings.toArray());
    }

    @Test
    void testRemoveByIndex() {
        listStrings.add("a");
        listStrings.add("b");
        listStrings.add("c");
        listStrings.remove(1);

        assertArrayEquals(new String[]{"a", "c"}, listStrings.toArray());
    }

    @Test
    void testRemoveByElement() {
        listDoubles.add(1.1);
        listDoubles.add(2.2);
        listDoubles.add(3.3);
        listDoubles.remove(2.2);

        assertArrayEquals(new Double[]{1.1, 3.3}, listDoubles.toArray());
    }

    @Test
    void testGet() {
        listStrings.add("apple");
        listStrings.add("banana");
        listStrings.add("cherry");

        assertEquals("banana", listStrings.get(1));
    }

    @Test
    void testClear() {
        listIntegers.add(1);
        listIntegers.add(2);
        listIntegers.add(3);
        listIntegers.clear();

        assertEquals(0, listIntegers.size());
    }

    @Test
    void testSize() {
        listIntegers.add(1);
        listIntegers.add(2);
        listIntegers.add(3);

        assertEquals(3, listIntegers.size());
    }

    @Test
    void testSort() {
        listIntegers.add(5);
        listIntegers.add(2);
        listIntegers.add(8);
        listIntegers.add(1);
        listIntegers.sort();

        assertArrayEquals(new Integer[]{1, 2, 5, 8}, listIntegers.toArray());
    }
}