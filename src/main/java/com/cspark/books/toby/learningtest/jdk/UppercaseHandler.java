package com.cspark.books.toby.learningtest.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by cspark on 2015. 12. 18..
 */
public class UppercaseHandler implements InvocationHandler {

    Hello target;

    public UppercaseHandler(Hello tarket) {
        this.target = tarket;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String ret = (String) method.invoke(target, args);
        return ret.toUpperCase();
    }
}