package com.example.factorial;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FactorialAppTest {

    @Test
    void testFactorialOfZero() {
        assertEquals(1, FactorialApp.factorial(0));
    }

    @Test
    void testFactorialOfFive() {
        assertEquals(120, FactorialApp.factorial(5));
    }

    @Test
    void testNegativeNumber() {
        assertThrows(IllegalArgumentException.class,
                () -> FactorialApp.factorial(-1));
    }
}

