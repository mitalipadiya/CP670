package com.example.androidassignments.utils;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

public class HelperTest extends TestCase {
    @Test
    public void testIsValidPassword() {
        Helper helper = new Helper();
        boolean isValid = helper.isValidPassword("abcd");
        assertEquals(isValid, true);
    }
}