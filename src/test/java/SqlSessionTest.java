import com.mufeng.entity.Student;
import com.mufeng.util.MyBatisUtil;
import lombok.Setter;
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
        } finally {
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
        } finally {
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
        } finally {
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
        } finally {
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
            List<Student> studentList = sqlSession.selectList("Student.queryByIdRange", param);
            for (Student student : studentList) {
                System.out.println(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
            List<Student> studentList = sqlSession.selectList("Student.queryByMultiParam", param);
            for (Student student : studentList) {
                System.out.println(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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
            List<Map> studentList = sqlSession.selectList("Student.queryByMultiParam3", param);
            for (Map student : studentList) {
                System.out.println(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqlSession != null) {
                MyBatisUtil.closeSqlSession(sqlSession);
            }
        }
    }
    //
    //@Test
    //public void testInsertStudent() {
    //    SqlSession sqlSession = null;
    //    try {
    //        sqlSession = MyBatisUtil.creatSqlSession();
    //        Student student = new Student(10, "沐风", "15115152564", 1);
    //        sqlSession.insert("Student.insertStudent", student);
    //        sqlSession.commit();
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //        assert sqlSession != null;
    //        sqlSession.rollback();
    //    } finally {
    //        if (sqlSession != null) {
    //            MyBatisUtil.closeSqlSession(sqlSession);
    //        }
    //    }
    //}

    /*先查询后修改*/

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
        } finally {
            assert sqlSession != null;
            sqlSession.close();
        }
    }



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
            param.setMobile("15115152564");
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


    /**
     * 模糊查询
     */
    @Test
    public void testDynamicSql2() {
        SqlSession sqlSession = null;
        try {
            sqlSession = MyBatisUtil.creatSqlSession();
            Student param = new Student();
            param.setId(3);
            param.setName("沐");
            List<Student> list = sqlSession.selectList("Student.dynamicSql2", param);
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

}

