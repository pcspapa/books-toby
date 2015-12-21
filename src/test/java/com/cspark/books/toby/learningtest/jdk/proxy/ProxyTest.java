package com.cspark.books.toby.learningtest.jdk.proxy;

import com.cspark.books.toby.learningtest.jdk.Hello;
import com.cspark.books.toby.learningtest.jdk.HelloTarket;
import com.cspark.books.toby.learningtest.jdk.UppercaseHandler;
import org.junit.Test;

import java.lang.reflect.Proxy;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by cspark on 2015. 12. 18..
 */
public class ProxyTest {

    @Test
    public void simpleProxy() throws Exception {
        Hello hello = new HelloTarket();
        assertThat(hello.sayHello("Chan"), is("Hello Chan"));
        assertThat(hello.sayHi("Chan"), is("Hi Chan"));
        assertThat(hello.sayThankYou("Chan"), is("Thank you Chan"));
    }

    @Test
    public void dynamicProxy() throws Exception {
        Hello hello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] {Hello.class},
                new UppercaseHandler(new HelloTarket()));
        assertThat(hello.sayHello("Chan"), is("HELLO CHAN"));
        assertThat(hello.sayHi("Chan"), is("HI CHAN"));
        assertThat(hello.sayThankYou("Chan"), is("THANK YOU CHAN"));
    }
}
