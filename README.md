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
依赖查找和依赖注入来源并不相同
UserRepository
``` java
public class UserRepository {
    private Collection<User> users;
    private BeanFactory beanFactory;
    private ObjectFactory<ApplicationContext> objectFactory;
}
```
Demo
``` java
BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:/META-INF/dependency-inject-context.xml");
UserRepository userRepository = (UserRepository) beanFactory.getBean("userRepository");
System.out.println(userRepository.getUserObjectFactory().getObject());
```
结果
![](https://github.com/Yehehui/spring-learning/blob/master/image/自动注入.png)
spring为ObjectFactory自动注入了ClassPathXmlApplication
### 依赖来源
1. 自定义bean
2. 容器内建bean
3. 容器内建依赖
### ApplicationContext与BeanFactory的关系
ApplicationContext实现了BeanFactory，扩展了更多功能
#### BeanFactory 使用用例
``` java
DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
int count = reader.loadBeanDefinitions("classpath:/META-INF/dependency-lookup-context.xml");
if (beanFactory instanceof DefaultListableBeanFactory) {
    Map<String, User> users = beanFactory.getBeansOfType(User.class);
    System.out.println(users);
}
```
#### ApplicationContext 使用示例
``` java
@Configuration
public class ApplicationContextAsIocContainer {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(ApplicationContextAsIocContainer.class);
        context.refresh();
        getBeanCollectionByType(context);
        context.close();
    }

    static void getBeanCollectionByType(BeanFactory beanFactory) {
        if (beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory factory = (ListableBeanFactory) beanFactory;
            Map<String, User> users = factory.getBeansOfType(User.class);
            System.out.println(users);
        }
    }

    @Bean
    public User getUser(){
        User user=new User();
        user.setId(123);
        user.setName("test");
        return user;
    }
}
```
