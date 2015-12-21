package com.cspark.books.toby.learningtest.jdk.proxy;

import com.cspark.books.toby.learningtest.jdk.Hello;
import com.cspark.books.toby.learningtest.jdk.HelloTarket;
import com.cspark.books.toby.learningtest.jdk.UppercaseHandler;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

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

    @Test
    public void pointcutAdvisor() throws Exception {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarket());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxiedHello = (Hello) pfBean.getObject();
        assertThat(proxiedHello.sayHello("Chan"), is("HELLO CHAN"));
        assertThat(proxiedHello.sayHi("Chan"), is("HI CHAN"));
        assertThat(proxiedHello.sayThankYou("Chan"), is("Thank you Chan"));
    }

    @Test
    public void classNamePointcutAdvisor() throws Exception {
        NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut(){
            @Override
            public ClassFilter getClassFilter() {
                return new ClassFilter() {
                    @Override
                    public boolean matches(Class<?> clazz) {
                        return clazz.getSimpleName().startsWith("HelloT");
                    }
                };
            }
        };
        classMethodPointcut.setMappedName("sayH*");

        checkAdviced(new HelloTarket(), classMethodPointcut, true);

        class HelloWord extends HelloTarket{}
        checkAdviced(new HelloWord(), classMethodPointcut, false);

        class HelloToby extends HelloTarket{}
        checkAdviced(new HelloToby(), classMethodPointcut, true);
    }

    private void checkAdviced(Object tarket, NameMatchMethodPointcut pointcut, boolean adviced) {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(tarket);
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
        Hello proxiedHello = (Hello) pfBean.getObject();

        if (adviced) {
            assertThat(proxiedHello.sayHello("Chan"), is("HELLO CHAN"));
            assertThat(proxiedHello.sayHi("Chan"), is("HI CHAN"));
            assertThat(proxiedHello.sayThankYou("Chan"), is("Thank you Chan"));
        }
        else {
            assertThat(proxiedHello.sayHello("Chan"), is("Hello Chan"));
            assertThat(proxiedHello.sayHi("Chan"), is("Hi Chan"));
            assertThat(proxiedHello.sayThankYou("Chan"), is("Thank you Chan"));
        }




    }

    static class UppercaseAdvice implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String) invocation.proceed();
            return ret.toUpperCase();
        }
    }
}
