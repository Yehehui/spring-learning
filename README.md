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
