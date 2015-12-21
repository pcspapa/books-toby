package com.cspark.books.toby.learningtest.jdk.proxy;

import com.cspark.books.toby.learningtest.jdk.Hello;
import com.cspark.books.toby.learningtest.jdk.HelloTarket;
import com.cspark.books.toby.learningtest.jdk.UppercaseHandler;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactoryBean;

import java.lang.reflect.Proxy;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by cspark on 2015. 12. 21..
 */
public class DynamicProxyTest {

    @Test
    public void simplProxy() throws Exception {
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Hello.class},
                new UppercaseHandler(new HelloTarket())
        );
    }

    @Test
    public void proxyFactoryBean() throws Exception {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarket());
        pfBean.addAdvice(new UppercaseAdvice());

        Hello proxiedHello = (Hello) pfBean.getObject();
        assertThat(proxiedHello.sayHello("Chan"), is("HELLO CHAN"));
        assertThat(proxiedHello.sayHi("Chan"), is("HI CHAN"));
        assertThat(proxiedHello.sayThankYou("Chan"), is("THANK YOU CHAN"));
    }

    static class UppercaseAdvice implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String) invocation.proceed();
            return ret.toUpperCase();
        }
    }
}
