package com.example;

import org.aopalliance.intercept.Interceptor;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

public class BeanInfodemo {
    public static void main(String[] args) throws IntrospectionException {
        BeanInfo info = Introspector.getBeanInfo(Person.class,Object.class);
        PropertyDescriptor[] propertyDescriptors = info.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor:propertyDescriptors){
            System.out.println(propertyDescriptor);
        }
    }
}
