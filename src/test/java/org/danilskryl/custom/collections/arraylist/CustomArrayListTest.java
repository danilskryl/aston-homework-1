package org.danilskryl.custom.collections.arraylist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomArrayListTest {

    private CustomArrayList<Integer> customList;

    @BeforeEach
    void setUp() {
        customList = new CustomArrayList<>();
    }

    @Test
    void testAddAndSize() {
        assertEquals(0, customList.size());
        customList.add(42);
        assertEquals(1, customList.size());
        customList.add(100);
        assertEquals(2, customList.size());
    }

    @Test
    void testAddAtIndex() {
        customList.add(1);
        customList.add(3);
        customList.add(4);

        customList.add(2, 1);
        assertEquals(4, customList.size());
        assertEquals(2, customList.get(1));
        assertEquals(3, customList.get(2));
    }

    @Test
    void testRemoveByIndex() {
        customList.add(1);
        customList.add(2);
        customList.add(3);

        customList.remove(1);
        assertEquals(2, customList.size());
        assertEquals(3, customList.get(1));
    }

    @Test
    void testRemoveByValue() {
        customList.add(1);
        customList.add(2);
        customList.add(3);

        customList.remove(Integer.valueOf(2));
        assertEquals(2, customList.size());
        assertEquals(1, customList.get(0));
        assertEquals(3, customList.get(1));
    }

    @Test
    void testGet() {
        customList.add(1);
        customList.add(2);
        customList.add(3);

        assertEquals(2, customList.get(1));
    }

    @Test
    void testClear() {
        customList.add(1);
        customList.add(2);
        customList.add(3);

        customList.clear();
        assertEquals(0, customList.size());
    }

    @Test
    void testSort() {
        customList.add(3);
        customList.add(1);
        customList.add(2);

        customList.sort();

        System.out.println(customList);
        assertEquals(1, customList.get(0));
        assertEquals(2, customList.get(1));
        assertEquals(3, customList.get(2));
    }

    @Test
    void testIterator() {
        customList.add(1);
        customList.add(2);
        customList.add(3);

        StringBuilder result = new StringBuilder();
        for (Integer value : customList) {
            result.append(value);
        }

        assertEquals("123", result.toString());
    }
}
