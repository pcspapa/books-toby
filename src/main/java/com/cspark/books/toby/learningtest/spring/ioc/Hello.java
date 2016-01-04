package com.cspark.books.toby.learningtest.spring.ioc;

/**
 * Created by cspark on 2016. 1. 4..
 */
public class Hello {

    String name;

    Printer printer;

    public String sayHello() {
        return "Hello " + name;
    }

    public void print() {
        printer.print(sayHello());
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }
}
