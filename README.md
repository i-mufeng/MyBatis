# MyBatis

## 6.1 MyBatis简介

ORM**对象关系映射**(Object Relation Mapping)框架，是一种应用程序操作关系型数据库的框架。

唯一的作用是将对对象的CRUD操作转换成对数据库的操作。

![image-20220312225059115](Spring.assets/image-20220312225059115.png)

**较为流行的ORM框架**：<font title="blue">SSH</font>

- <font title="red">JPA</font>：它本身只是一种ORM的规范，并不是ORM产品。最大的优势就是通用性和标准化。
- <font title="red">Hibernate</font>：目前世界范围内使用最多的ORM框架。早起就被JBoss选为持久层解决方案。RedHat也将其作为归于其组织的一部分。
- <font title="red">MyBatis</font>：早期叫IBatis。Apache基金会的子项目。是一种半`自动化`的ORM框架。开发者可以灵活的编写SQL语句。

## 6.2 MyBatis实例

### 6.2.1 MyBatis流程

![image-20220313003116609](Spring.assets/image-20220313003116609.png)

一、<font title="blue">SqlSessionFactory</font>

1. MyBatis中最重要的全局对象，负责初始化MyBatis环境以及创建SqlSession对象
2. 要保证全局唯一

二、<font title="blue">SqlSession</font>

1. 操作数据库的核心对象
2. 提供了JDBC方式与数据库进行交互
3. 封装了对数据库的CRUD方法和获取连接。 获取代理对象（`getMapper()`）

### 6.2.2 实例代码

一、引入依赖

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.28</version>
</dependency>
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.9</version>
</dependency>
```

二、配置文件

```xml
<?xml version="1.0" encoding="utf-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">

<!--MyBatis配置文件的根标签-->
<configuration>
    <properties resource="jdbc.properties"/>
    <!--用来设置MyBatis的多环境。default指定默认使用的环境，为必需！-->
    <environments default="development">
        <!--代表具体某一个环境的连接方式和相关配置（与事务相关）。id作为环境的唯一标识，为必需！-->
        <environment id="development">
            <!--
                type：配置事务管理的方式。
                    JDBC：代表直接使用JDBC的提交和回滚方式管理事务。
                    MANAGED：从来不提交或者回滚事务。默认情况下会关闭数据库。阻止关闭数据库代码如下：
                        <transactionManager type="MANAGED">
                            <property name="closeConnection" value="false"/>
                        </transactionManager>
            -->
            <transactionManager type="JDBC"/>
            <!--数据源的配置
                type：
                  - POOLED：代表会使用MyBatis自己的数据库连接池管理数据源，避免重复重复连接会造成资源消耗。
                  - UNPOOLED：直接连接，和JDBC一样，会频繁的开启关闭对数据源的连接。
                  - JNDI：代表该数据源是配置在应用服务器或EJB，实现数据源在应用外部进行集中化管理
            -->
            <dataSource type="POOLED">
                <property name="driver" value="${jdbc.driverClassName}"/>
                <property name="url" value="${jdbc.url}"/>
                <property name="username" value="${jdbc.username}"/>
                <property name="password" value="${jdbc.password}"/>
            </dataSource>
        </environment>
    </environments>
</configuration>
```

三、测试

```java
@Test
    public void testSqlSessionFactory() {
        SqlSession sqlSession = null;
        try {
            /*加载配置文件*/
            Reader reader = Resources.getResourceAsReader("Mybatis-config.xml");
            /*利用创建者模式根据解析配置文件，并初始化SqlSessionFactory对象*/
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            /*通过SqlSessionFactory对象创建SqlSession对象*/
            sqlSession = sqlSessionFactory.openSession();
            /*通过SqlSession对象获取JDBC连接*/
            Configuration configuration = sqlSession.getConfiguration();
            System.out.println(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }
```

### 6.2.3 关于SqlSession全局唯一的工具类

```java
package com.mufeng.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

/**
 * @author MFGN
 * @data 2022/3/13 0:36
 * @description 关于SqlSession全局唯一的工具类
 */
public class MyBatisUtil {
    private static SqlSessionFactory sqlSessionFactory = null;

    /*使用静态代码块，使得SqlSession全局唯一*/
    static {
        try {
            /*加载配置文件*/
            Reader reader = Resources.getResourceAsReader("Mybatis-config.xml");
            /*利用创建者模式根据解析配置文件，并初始化SqlSessionFactory对象*/
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException e) {
            e.printStackTrace();
            /*抛出异常*/
            throw new ExceptionInInitializerError(e);
        }

    }
    /**
     * 创建SqlSession对象
     * @return SqlSession
     */
    public static SqlSession creatSqlSession(){
        return sqlSessionFactory.openSession();
    }

    /**
     * 关闭连接
     * @param sqlSession 需要关闭的SqlSession
     */
    public static void closeSqlSession(SqlSession sqlSession) {
        sqlSession.close();
    }
}
```

### 6.2.4 简单的GRUD查询

引入logback日志组件

```xml
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.3.0-alpha13</version>
</dependency>
```

开启下划线转驼峰

```xml
<configuration>
    <settings>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
    </settings>
</configuration>
```

本地时间格式

```java
private LocalDateTime createTime;
```

**一、映射文件** (mapper.xml)

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--
    mapper是MyBatis映射文件的根标签
        namespace：必须指定且必须唯一。代表命名空间。
-->
<mapper namespace="Student">
    <!--
        id：该命名空间下的唯一标识
        resultType：在没有设置别名的情况下，写全限定名
        #{} 代表取值   在针对只有一个参数的情况下，该取值没有限定，但建议与其他地方一致
    -->
    <select id="queryById" resultType="com.mufeng.entity.Student">
        SELECT * FROM student WHERE id = #{id};
    </select>

</mapper>
```

**二、将映射器注册给MyBatis**(mybatis-config.xml)

```xml
<!--注册映射器-->
<mappers>
    <mapper resource="mapper/studentDao.xml"/>
</mappers>
```

**三、调用SqlSession的查询方法**

Student1：

```java
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Student {
    private Integer id;
    private String name;
    private String mobile;
}
```

Student2：

```java
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Student2 {
    private Integer id;
    private String name;
    private String mobile;
    private LocalDateTime createTime;
}
```

```java
import com.mufeng.entity.Student;
import com.mufeng.util.MyBatisUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MFGN
 * @data 2022/3/13 0:10
 * @description
 */
public class SqlSessionTest {
    @Test
    public void testSqlSessionFactory() {
        SqlSession sqlSession = null;
        try {
            /*加载配置文件*/
            Reader reader = Resources.getResourceAsReader("Mybatis-config.xml");
            /*利用创建者模式根据解析配置文件，并初始化SqlSessionFactory对象*/
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            /*通过SqlSessionFactory对象创建SqlSession对象*/
            sqlSession = sqlSessionFactory.openSession();
            /*通过SqlSession对象获取JDBC连接*/
            Configuration configuration = sqlSession.getConfiguration();
            System.out.println(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    @Test
    public void testSqlSessionFactory2() {
        SqlSession sqlSession = null;
        try {
            sqlSession = MyBatisUtil.creatSqlSession();
            System.out.println(sqlSession.getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (sqlSession != null) {
                MyBatisUtil.closeSqlSession(sqlSession);
            }
        }
    }

    /**
     * 根据 id 查询
     */
    @Test
    public void testQueryStudentById() {
        SqlSession sqlSession = null;
        try {
            sqlSession = MyBatisUtil.creatSqlSession();
            Student student = sqlSession.selectOne("Student.queryById", 1);
            System.out.println(student);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (sqlSession != null) {
                MyBatisUtil.closeSqlSession(sqlSession);
            }
        }
    }

    /**
     * 查询所有
     */
    @Test
    public void testQueryAll() {
        SqlSession sqlSession = null;
        try {
            sqlSession = MyBatisUtil.creatSqlSession();
            List<Student> studentList = sqlSession.selectList("Student.queryAll");
            for (Student student : studentList) {
                System.out.println(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (sqlSession != null) {
                MyBatisUtil.closeSqlSession(sqlSession);
            }
        }
    }

    /**
     * 范围查询
     */
    @Test
    public void testQueryByIdRange() {
        SqlSession sqlSession = null;
        try {
            sqlSession = MyBatisUtil.creatSqlSession();
            HashMap<String, Integer> param = new HashMap<>();
            param.put("fi", 1);
            param.put("la", 3);
            List<Student> studentList = sqlSession.selectList("Student.queryByIdRange",param);
            for (Student student : studentList) {
                System.out.println(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (sqlSession != null) {
                MyBatisUtil.closeSqlSession(sqlSession);
            }
        }
    }

    /**
     * 多条件查询
     */
    @Test
    public void testQueryByMultiParam() {
        SqlSession sqlSession = null;
        try {
            sqlSession = MyBatisUtil.creatSqlSession();
            HashMap<String, Object> param = new HashMap<>();
            param.put("id", 1);
            param.put("name", "沐风");
            List<Student> studentList = sqlSession.selectList("Student.queryByMultiParam",param);
            for (Student student : studentList) {
                System.out.println(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (sqlSession != null) {
                MyBatisUtil.closeSqlSession(sqlSession);
            }
        }
    }

    /**
     * 多条件查询,使用Map接收结果
     */
    @Test
    public void testQueryByMultiParam2() {
        SqlSession sqlSession = null;
        try {
            sqlSession = MyBatisUtil.creatSqlSession();
            HashMap<String, Object> param = new HashMap<>();
            param.put("id", 1);
            param.put("name", "沐风");
            List<Map> studentList = sqlSession.selectList("Student.queryByMultiParam3",param);
            for (Map student : studentList) {
                System.out.println(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (sqlSession != null) {
                MyBatisUtil.closeSqlSession(sqlSession);
            }
        }
    }


}
```

```java
import com.mufeng.entity.Student2;
import com.mufeng.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MFGN
 * @data 2022/3/13 2:45
 * @description
 */

public class Student2App {
    @Test
    public void testUnderscoreToCamelCase(){
        SqlSession sqlSession = null;
        try {
            sqlSession = MyBatisUtil.creatSqlSession();
            List<Student2> studentList = sqlSession.selectList("Student2.queryById",1);
            for (Student2 student : studentList) {
                System.out.println(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (sqlSession != null) {
                MyBatisUtil.closeSqlSession(sqlSession);
            }
        }
    }


    /**
     * 多条件查询,使用Map接收结果（不开启驼峰命名也不影响查询结果）
     */
    @Test
    public void testQueryByMultiParam2() {
        SqlSession sqlSession = null;
        try {
            sqlSession = MyBatisUtil.creatSqlSession();
            HashMap<String, Object> param = new HashMap<>();
            param.put("id", 2);
            param.put("name", "芒果");
            List<Map> studentList = sqlSession.selectList("Student2.queryByMultiParam3",param);
            for (Map student : studentList) {
                System.out.println(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (sqlSession != null) {
                MyBatisUtil.closeSqlSession(sqlSession);
            }
        }
    }
}
```

### 6.2.5 MyBatis数据写入

#### 1、事务

数据写入涉及到数据库的`事务`，事务的存在是为了保证 <font title="blue">数据操作的完整性</font>

#### 2、 增加数据：

```xml
<select id="insertStudent" parameterType="com.mufeng.entity.Student">
    INSERT INTO student(id, name, mobile)
    VALUES (#{id}, #{name}, #{mobile});
</select>
```

```java
@Test
public void testInsertStudent() {
    SqlSession sqlSession = null;
    try {
        sqlSession = MyBatisUtil.creatSqlSession();
        Student2 student = new Student2();
        student.setId(12);
        student.setName("mufeng");
        student.setMobile("151");
        student.setCreateTime(LocalDateTime.of(2001,4,3,12,20,33));
        sqlSession.insert("Student2.insertStudent", student);
        sqlSession.commit();
    } catch (Exception e) {
        e.printStackTrace();
        assert sqlSession != null;
        sqlSession.rollback();
    }finally {
        if (sqlSession != null) {
            MyBatisUtil.closeSqlSession(sqlSession);
        }
    }
}
```

#### 3、修改和删除数据

```xml
<select id="updateById">
    UPDATE student
    SET mobile=#{mobile}
    WHERE id=#{id};
</select>

<select id="delById">
    DELETE
    FROM student
    WHERE id = #{id};
</select>
```

```java
@Test
public void testUpdate() {
    SqlSession sqlSession = null;
    try {
        sqlSession = MyBatisUtil.creatSqlSession();
        Student student = sqlSession.selectOne("Student.queryById", 1);
        System.out.println(student);
        student.setMobile("XXXXXXX");
        sqlSession.update("Student.updateById", student);
        //System.out.println("生效个数" + update);
        sqlSession.commit();
    } catch (Exception e) {
        e.printStackTrace();
        assert sqlSession != null;
        sqlSession.rollback();
    } finally {
        if (sqlSession != null) {
            sqlSession.close();
        }
    }
}

@Test
public void testDelStudent() {
    SqlSession sqlSession = null;
    try {
        sqlSession = MyBatisUtil.creatSqlSession();
        int del = sqlSession.delete("Student.delById", 10);
        System.out.println(del);
        sqlSession.commit();
    } catch (Exception e) {
        e.printStackTrace();
        assert sqlSession != null;
        sqlSession.rollback();
    }finally {
        assert sqlSession != null;
        sqlSession.close();
    }
}
```

## 6.3 其他

### 一、自增主键的获取

```xml
<insert id="insertStudentWithPrimaryKey" parameterType="com.mufeng.entity.Student">
    <selectKey resultType="int" keyColumn="id" keyProperty="id" order="AFTER">
        SELECT LAST_INSERT_ID()
    </selectKey>
    INSERT INTO student(name, mobile)
    VALUES (#{name}, #{mobile})
</insert>
推荐的方法：
<insert id="insertStudentWithPrimaryKey2" parameterType="com.mufeng.entity.Student"
        useGeneratedKeys="true" keyColumn="id" keyProperty="id">
    INSERT INTO student(name, mobile)
    VALUES (#{name}, #{mobile})
</insert>
```

UUID（不常用，一般使用基于雪花算法的方法类似于滴滴TinyId、美团Leaf “雪花+号段模式”、redis）

```xml
<insert id="insertStudentWithPrimaryKey2" parameterType="com.mufeng.entity.Student"
        useGeneratedKeys="true" keyColumn="id" keyProperty="id">
    INSERT INTO student(name, mobile)
    VALUES (#{name}, #{mobile})
</insert>
```

```java
/*自增主键的获取*/
@Test
public void testInsertStudentWithPrimaryKey() {
    SqlSession sqlSession = null;
    try {
        sqlSession = MyBatisUtil.creatSqlSession();
        Student student = new Student();
        student.setName("muzi");
        student.setMobile("123");
        sqlSession.insert("Student.insertStudentWithPrimaryKey2", student);
        sqlSession.commit();
        System.out.println(student);
    } catch (Exception e) {
        e.printStackTrace();
        assert sqlSession != null;
        sqlSession.rollback();
    } finally {
        if (sqlSession != null) {
            MyBatisUtil.closeSqlSession(sqlSession);
        }
    }
}
```

### 二、SQL防注入

SQL注入：将恶意的sql插入到执行语句

MyBatis的两个传参方式`#`  `$`的区别

```sql
INSERT INTO student(name, mobile)
VALUES (#{name}, #{mobile})
```

### 三、动态SQL

**MySQL的动态SQL**用到了`OGNL`，是一种开源的表达式语言

| 关系   | JAVA              | OGNL表示                 |
| ------ | ----------------- | ------------------------ |
| 与     | `e1&e2`           | e1 `and` e2              |
| 或     | `e1|e2`           | e1 `or` e2               |
| 相等   | `==`              | e1 `eq` e2   e1 `==`e2   |
| 不相等 | `!=`              | e1 `neq` e2   e1 `!=` e2 |
| 比较   | `>`/`<`/`>=`/`<=` | `gt`/`lt`/`gte`/`lte`    |
| 非     | ！                | `not`                    |

**涉及到的标签：**

if、where、set、foreach(批处理)、choose(when，otherwise)

- if标签

  ```xml
  <select id="dynamicSql" parameterType="com.mufeng.entity.Student" resultType="com.mufeng.entity.Student">
      SELECT * FROM student WHERE true
      <if test="name!=null and name!=''">
          AND name LIKE CONCAT('%',#{name},'%')
      </if>
  </select>
  ```

  ```java
  /**
   * 模糊查询
   */
  @Test
  public void testDynamicSql1() {
      SqlSession sqlSession = null;
      try {
          sqlSession = MyBatisUtil.creatSqlSession();
          Student param = new Student();
          param.setName("沐");
          List<Student> list = sqlSession.selectList("Student.dynamicSql", param);
          for (Student student1 : list) {
              System.out.println(student1);
          }
      } catch (Exception e) {
          e.printStackTrace();
      } finally {
          if (sqlSession != null) {
              MyBatisUtil.closeSqlSession(sqlSession);
          }
      }
  }
  ```

- where标签：一般配合`if`标签来用，可以省略`WHRER`、过滤掉`AND`关键字

  ```xml
  <select id="dynamicSql" parameterType="com.mufeng.entity.Student" resultType="com.mufeng.entity.Student">
      SELECT * FROM student
      <where>
          <if test="name!=null and name!=''">
              AND name LIKE CONCAT('%',#{name},'%')
          </if>
          <if test="mobile!=null and mobile!=''">
              AND mobile=#{mobile}
          </if>
      </where>
  </select>
  ```

- choose标签

  ```xml
  <select id="dynamicSql2" parameterType="com.mufeng.entity.Student" resultType="com.mufeng.entity.Student">
      SELECT * FROM student
      <where>
          <choose>
              <when test="id!=null">
                  AND id=#{id}
              </when>
              <when test="name!=null and name!=''">
                  AND name LIKE contat('%',#{name},'%')
              </when>
              <otherwise>
                  AND id=1
              </otherwise>
          </choose>
      </where>
  </select>
  ```

> 可以通过`<sql id="">id,name,mobile</sql>`；来提取公共sql片段，在需要的地方通过`<include refid="base_colum"/>`来使用

### 四、MyBatis缓存机制

如果执行两次相同的SQL，第一次会去硬盘查库查表，并将结果缓存在内存

MyBatis的缓存机分为两级。缓存的区别在于其缓存的范围。

**1、一级缓存**

默认情况下在Setting中已经开启一级缓存，缓存范围是当前的`SqlSession`，又称为本地缓存。 

```java
sqlSession = MyBatisUtil.creatSqlSession();
Student student1 = sqlSession.selectOne("Student.queryById", 1);
Student student2 = sqlSession.selectOne("Student.queryById", 1);
Student student3 = sqlSession.selectOne("Student.queryById", 1);	//只执行一次SQL
System.out.println(student1 == student2 && student2  == student3);	//true，内存地址一致
```

```java
SqlSession sqlSession = null;
try {
    sqlSession = MyBatisUtil.creatSqlSession();
    Student student1 = sqlSession.selectOne("Student.queryById", 1);
    System.out.println(sqlSession+""+student1);
} catch (Exception e) {
    e.printStackTrace();
} finally {
    if (sqlSession != null) {
        MyBatisUtil.closeSqlSession(sqlSession);
    }
}
SqlSession sqlSession1 = null;
try {
    sqlSession1 = MyBatisUtil.creatSqlSession();
    Student student1 = sqlSession1.selectOne("Student.queryById", 1);
    System.out.println(sqlSession1+""+student1);
} catch (Exception e) {
    e.printStackTrace();
} finally {
    if (sqlSession1 != null) {
        MyBatisUtil.closeSqlSession(sqlSession1);
    }
}
```

一级缓存的范围是同一个SqlSession，由于此处的两次查询由于SqlSession不同，所以查询到的对象也不同

**2、二级缓存**

```xml
<!--开启二级缓存
    eviction(内存淘汰/回收策略)：当缓存的对象数量达到上限，自动触发指定的回收策略算法清除缓存
        - LRU(Least Recently Used)：默认选项。淘汰最少使用的缓存对象
        - FIFO(First In First Out)：先进先出。类似于队列，先被缓存的对象向先被淘汰。
        - SOFT(软引用)：
        - WEAK(弱引用)：依靠 JVM 的垃圾回收机制
    flushInterval(刷新间隔)：清空所有缓存，单位为毫秒。默认不设置。
    size(缓存数量的上限值)：默认为 1024。数量指一次查询结果，无论记录条数。
		- 如果查询数量较大，可以设置<select>标签的useCache属性为false，关闭查询的二级缓存
		  增删改的操作flashCache属性默认为true，查的默认为false
    readOnly()：如果为 true，则代表调用者命中的缓存对象是原本缓存的对象。
        - 如果为 false（默认），则代表调用者命中的缓存对象是原本缓存的克隆（序列化）。
        - 设置为 false 比 true 效率低，但是更安全。
-->
<cache eviction="LRU" flushInterval="60000" readOnly="true"/>
```

缓存命中率：
$$
\frac{命中次数}{查询次数}
$$
<font title="blue">由于设置了`ReadOnly=true`，所有都会是同一个对象。</font>

<font title="blue">日志中Cache Hit Ratio</font>：二级缓存命中率

<font title="blue">缓存命中规则</font>：能够命中一级缓存就读取一级缓存的数据，否则查询二级缓存。

### 五、MyBatis代理机制

```java
public interface StudentDao {
    Student queryById(Integer id);
}
```

```xml
<mapper namespace="com.mufeng.dao.StudentDao">
    <cache eviction="LRU" flushInterval="60000" readOnly="true"/>

    <sql id="base_colum">id,name,mobile</sql>
    <select id="queryById" resultType="com.mufeng.entity.Student">
        SELECT <include refid="base_colum"/>
        FROM student
        WHERE id = #{id};
    </select>
</mapper>
```

```xml
    <!--注册映射器-->
    <mappers>
        <mapper resource="mapper/studentDao.xml"/>
    </mappers>
```

```java
 1@Test2public void proxyTest() {3    try (SqlSession sqlSession = MyBatisUtil.creatSqlSession()) {4        StudentDao studentDao = sqlSession.getMapper(StudentDao.class);5        Student student = studentDao.queryById(1);6        System.out.println(student);7    } catch (Exception e) {8        e.printStackTrace();9    }10}	java
```

### 六、MyBatis设置别名

**1、通过类路径配置别名**

```xml
<typeAliases>
    <!--默认为类名，不区分大小写-->
    <typeAlias type="com.mufeng.entity.Student"/>
    <!--自定义别名-->
    <typeAlias type="com.mufeng.entity.Student" alias="xxx"/>   

    <!--通过类似包扫描来对包下所有的文件进行扫描，会出现乱码的问题-->
    <package name="com.mufeng.entity"/>
</typeAliases>
```

**2、通过包扫描配置别名**

```xml
<typeAliases>
    <!--通过包扫描来对包下所有的文件进行扫描，会出现乱码的问题-->
    <package name="com.mufeng.entity"/>
</typeAliases>
```

```java
@Alias("stu")		//包扫描自定义别名
public class Student implements Serializable {
```

解决乱码问题：

使用自己的VFS。



### 七、MyBatis级联/联合查询

#### 一对一/多对一

```java
/**
 * 关联的课程id
 */
private Integer courseId;

/**
 * 关联的课程对象
 */
private Course course;
```

```xml
<!--联合查询-->
<resultMap type="com.mufeng.entity.Student" id="StudentMap2">
    <result property="id" column="id" jdbcType="INTEGER"/>
    <result property="name" column="name" jdbcType="VARCHAR"/>
    <result property="mobile" column="mobile" jdbcType="VARCHAR"/>
    <result property="courseId" column="course_id" jdbcType="INTEGER"/>
    <association property="course" javaType="com.mufeng.entity.Course">
        <id property="courseId" column="course_id"/>
        <result property="courseName" column="course_name" jdbcType="VARCHAR"/>
        <result property="courseContent" column="course_content" jdbcType="VARCHAR"/>
    </association>
</resultMap>

<select id="selectManyToOne1" resultMap="StudentMap2">
    SELECT s.*,c.course_id courseId,course_name,course_content
    FROM
        student s
            LEFT JOIN course c on s.course_id = c.course_id
    where s.id=#{studentId}
</select>
```

```xml
<!--级联查询-->
<resultMap type="com.mufeng.entity.Student" id="StudentMap3">
    <result property="id" column="id" jdbcType="INTEGER"/>
    <result property="courseId" column="course_id" jdbcType="INTEGER"/>

    <association property="course" javaType="com.mufeng.entity.Course"
                 select="com.mufeng.dao.CourseDao.queryById" column="course_id"/>
</resultMap>

<!--第一步操作-->
<select id="queryById3" resultMap="StudentMap3">
    select
        id, name, mobile, course_id
    from student
    where id = #{id}
</select>
```

```java
/**
 * 查询id为2的学生信息以及其报名的课程信息
 */
@Test
public void queryById2() {
    SqlSession sqlSession = null;
    try {
        sqlSession = MyBatisUtil.creatSqlSession();
        StudentDao mapper = sqlSession.getMapper(StudentDao.class);
        Student student = mapper.queryById3(1);
        sqlSession.commit();
        System.out.println(student);

    } catch (Exception e) {
        e.printStackTrace();
        assert sqlSession != null;
        sqlSession.rollback();
    } finally {
        assert sqlSession != null;
        sqlSession.close();

    }
}
```

#### 多表联合查询/一对多

一门课程包含多个学生，一对多的关系

#### 分页插件

引入pageHelper依赖

```xml
<!--分页插件-->
<dependency>
    <groupId>com.github.pagehelper</groupId>
    <artifactId>pagehelper</artifactId>
    <version>5.2.1</version>
</dependency>
```

配置

### 八、整合第三方连接池

#### C3P0

引入依赖

```xml
<dependency>
    <groupId>com.mchange</groupId>
    <artifactId>c3p0</artifactId>
    <version>0.9.5.2</version>
</dependency>
```

```java
package com.mufeng.mydatasource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

/**
 * @author MFGN
 * @data 2022/3/20 9:08
 * @description c3p0数据源工厂
 */

public class C3P0DataSoureFactory extends UnpooledDataSourceFactory {
    public C3P0DataSoureFactory() {
        this.dataSource = new ComboPooledDataSource();
    }

}
```

配置数据源

```xml
<environments default="development">
    <environment id="development">
        <transactionManager type="JDBC"/>
        <dataSource type="com.mufeng.mydatasource.C3P0DataSoureFactory">
            <property name="driverClass" value="${jdbc.driverClassName}"/>
            <property name="jdbcUrl" value="${jdbc.url}"/>
            <property name="user" value="${jdbc.username}"/>
            <property name="password" value="${jdbc.password}"/>
        </dataSource>
    </environment>
```

#### Druid

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.1.24</version>
</dependency>
```

```xml
<environment id="development">
    <transactionManager type="JDBC"/>
    <dataSource type="com.mufeng.mydatasource.DruidDataSourceFactory">
        <!--不需要配置-->
        <!--<property name="driverClassName" value="${jdbc.driverClassName}"/>-->
        <property name="jdbcUrl" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
    </dataSource>
</environment>
```
