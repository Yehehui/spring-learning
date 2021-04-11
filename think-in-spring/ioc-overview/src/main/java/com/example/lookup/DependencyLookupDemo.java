package com.example.lookup;

import com.example.anotation.Super;
import com.example.domain.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.ObjectFactoryCreatingFactoryBean;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.jws.soap.SOAPBinding;
import java.util.Map;

public class DependencyLookupDemo {
    public static void main(String[] args) throws Exception {
        BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/META-INF/dependency-lookup-context.xml");
        if (factory instanceof ListableBeanFactory){
            ListableBeanFactory beanFactory= (ListableBeanFactory) factory;
            Map<String, User> beansWithAnnotation = (Map)beanFactory.getBeansWithAnnotation(Super.class);
            System.out.println(beansWithAnnotation);
        }
    }
}
