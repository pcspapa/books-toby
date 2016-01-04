package com.cspark.books.toby.learningtest.ioc;

/**
 * Created by cspark on 2016. 1. 4..
 */
public class ConsolePrinter implements Printer {

    @Override
    public void print(String message) {
        System.out.println(message);
    }

}
