package com.cspark.books.toby;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

/**
 * Created by cspark on 2015. 12. 14..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/junit.xml")
public class JUnitWithSpringTest {

    static Set<JUnitWithSpringTest> testObjects = new HashSet<>();

    static ApplicationContext contextObject = null;

    @Autowired
    ApplicationContext context;

    @Test
    public void test1() throws Exception {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);

        assertThat(contextObject == null || contextObject == context, is(true));
        contextObject = context;
    }

    @Test
    public void test2() throws Exception {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);

        assertThat(contextObject == null || contextObject == context, is(true));
        contextObject = context;
    }

    @Test
    public void test3() throws Exception {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);

        assertThat(contextObject == null || contextObject == context, is(true));
        contextObject = context;
    }
}
