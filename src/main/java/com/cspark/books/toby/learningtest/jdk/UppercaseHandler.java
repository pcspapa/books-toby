package com.cspark.books.toby.learningtest.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by cspark on 2015. 12. 18..
 */
public class UppercaseHandler implements InvocationHandler {

    Object target;

    public UppercaseHandler(Object tarket) {
        this.target = tarket;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object ret = method.invoke(target, args);
        if (ret instanceof String)
            return ((String) ret).toUpperCase();
        else
            return ret;
    }
}