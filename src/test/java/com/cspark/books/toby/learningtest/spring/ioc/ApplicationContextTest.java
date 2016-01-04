package com.cspark.books.toby.learningtest.spring.ioc;

import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by cspark on 2016. 1. 4..
 */
public class ApplicationContextTest {

    @Test
    public void registerBean() throws Exception {
        StaticApplicationContext ac = new StaticApplicationContext();

        // 1st
        ac.registerSingleton("hello1", Hello.class);

        Hello hello1 = ac.getBean("hello1", Hello.class);
        assertThat(hello1, is(notNullValue()));

        // 2nd
        BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        helloDef.getPropertyValues().add("name", "Spring");
        ac.registerBeanDefinition("hello2", helloDef);

        Hello hello2 = ac.getBean("hello2", Hello.class);
        assertThat(hello2.sayHello(), is("Hello Spring"));
        assertThat(hello1, is(not(hello2)));
        assertThat(ac.getBeanFactory().getBeanDefinitionCount(), is(2));
    }

    @Test
    public void registerBeanWithDependency() throws Exception {
        StaticApplicationContext ac = new StaticApplicationContext();

        // SpringPrinter
        ac.registerBeanDefinition("printer", new RootBeanDefinition(StringPrinter.class));

        // Hello
        BeanDefinition helloDef = new RootBeanDefinition(Hello.class);
        helloDef.getPropertyValues().add("name", "Spring");
        helloDef.getPropertyValues().add("printer", new RuntimeBeanReference("printer"));
        ac.registerBeanDefinition("hello", helloDef);

        Hello hello = ac.getBean("hello", Hello.class);
        hello.print();

        assertThat(ac.getBean("printer").toString(), is("Hello Spring"));
    }

    @Test
    public void genericApplicationContext() throws Exception {
        GenericApplicationContext ac = new GenericApplicationContext();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(ac);
        reader.loadBeanDefinitions("com/cspark/books/toby/learningtest/spring/ioc/genericApplicationContext.xml");
        ac.refresh();

        Hello hello = ac.getBean("hello", Hello.class);
        hello.print();

        assertThat(ac.getBean("printer").toString(), is("Hello Spring"));
    }
}
