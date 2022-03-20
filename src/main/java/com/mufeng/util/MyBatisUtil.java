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
