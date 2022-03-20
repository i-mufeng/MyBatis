import com.mufeng.entity.Student;
import com.mufeng.entity.Student2;
import com.mufeng.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.type.LocalDateTimeTypeHandler;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Date;
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
     * 多条件查询,使用Map接收结果
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
            sqlSession.insert("Student.insertStudent", student);
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
}
