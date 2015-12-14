package com.cspark.books.toby;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

/**
 * Created by cspark on 2015. 12. 14..
 */
public class JUnitTest {

    static Set<JUnitTest> testObjects = new HashSet<>();

    @Test
    public void test1() throws Exception {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);
    }

    @Test
    public void test2() throws Exception {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);
    }

    @Test
    public void test3() throws Exception {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);
    }
}
