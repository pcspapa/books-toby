package com.cspark.books.toby.learningtest.spring.ioc;

/**
 * Created by cspark on 2016. 1. 4..
 */
public class StringPrinter implements Printer {

    private StringBuffer buffer = new StringBuffer();

    @Override
    public void print(String message) {
        buffer.append(message);
    }

    @Override
    public String toString() {
        return buffer.toString();
    }

}
