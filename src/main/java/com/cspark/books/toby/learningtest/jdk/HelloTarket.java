package com.cspark.books.toby.learningtest.jdk;

/**
 * Created by cspark on 2015. 12. 18..
 */
public class HelloTarket implements Hello {
    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }

    @Override
    public String sayHi(String name) {
        return "Hi " + name;
    }

    @Override
    public String sayThankYou(String name) {
        return "Thank you " + name;
    }
}
