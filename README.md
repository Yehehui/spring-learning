# spring-learning
[spring相关文档](https://docs.spring.io/spring-framework/docs/current/reference/html/index.html)
## ioc-overview
### 依赖查找
#### 实时获取
demo:
``` java
BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/META-INF/dependency-lookup-context.xml");
User user = factory.getBean(User.class);
System.out.println(user);
```
xml:
``` xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="user" class="com.example.domain.User">
        <property name="id" value="1"/>
        <property name="name" value="hehuiye"/>
    </bean>
</beans>
```
#### 延时获取
使用objectFactoryCreatingFactoryBean<br/>
demo:
``` java
BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/META-INF/dependency-lookup-context.xml");
ObjectFactory<User> objectFactory = (ObjectFactory<User>) factory.getBean("objectFactory");
System.out.println(objectFactory.getObject());
```
xml:
``` xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">
    <bean id="user" class="com.example.domain.User">
        <property name="id" value="1"/>
        <property name="name" value="hehuiye"/>
    </bean>
    <bean id="objectFactory" class="org.springframework.beans.factory.config.ObjectFactoryCreatingFactoryBean">
        <property name="targetBeanName" value="user"/>
    </bean>
</beans>
```
#### 获取集合
``` java
BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/META-INF/dependency-lookup-context.xml");
if (factory instanceof ListableBeanFactory){
    ListableBeanFactory beanFactory= (ListableBeanFactory) factory;
    Map<String, User> map=beanFactory.getBeansOfType(User.class);
    System.out.println(map);
}
```
#### 根据标签获取
定义标签：
``` java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Super {
}
```
demo:
``` java
BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/META-INF/dependency-lookup-context.xml");
if (factory instanceof ListableBeanFactory){
    ListableBeanFactory beanFactory= (ListableBeanFactory) factory;
    Map<String, User> beansWithAnnotation = (Map)beanFactory.getBeansWithAnnotation(Super.class);
    System.out.println(beansWithAnnotation);
}
```
xml:
``` xml
<bean id="superUser" class="com.example.domain.SuperUser" parent="user" primary="true">
    <property name="address" value="福州"/>
</bean>
```
### 依赖注入
util命名空间：
``` xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:util="http://www.springframework.org/schema/util"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/util https://www.springframework.org/schema/util/spring-util.xsd">
    <import resource="dependency-lookup-context.xml"/>
    <bean id="userRepository" class="com.example.domain.UserRepository">
        <property name="users">
            <util:list>
                <ref bean="superUser"/>
                <ref bean="user"/>
            </util:list>
        </property>
    </bean>
</beans>
```
autowire
``` xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:util="http://www.springframework.org/schema/util"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/util https://www.springframework.org/schema/util/spring-util.xsd">
    <import resource="dependency-lookup-context.xml"/>
    <bean id="userRepository" class="com.example.domain.UserRepository" autowire="byType">
    </bean>
</beans>
```
### 区别
UserRepository
``` java
public class UserRepository {
    Collection<User> users;
    BeanFactory beanFactory;
}
```
Demo
``` java
BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:/META-INF/dependency-inject-context.xml");
UserRepository userRepository = (UserRepository) beanFactory.getBean("userRepository");
System.out.println(userRepository.getBeanFactory());
System.out.println(userRepository.getBeanFactory() == beanFactory);
System.out.println(beanFactory.getBean(BeanFactory.class));
```
结果
![](https://github.com/Yehehui/spring-learning/blob/master/image/%E4%BE%9D%E8%B5%96%E6%B3%A8%E5%85%A5%E6%9F%A5%E6%89%BE%E5%8C%BA%E5%88%ABdemo.png)
