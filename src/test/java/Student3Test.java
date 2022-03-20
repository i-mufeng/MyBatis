import com.mufeng.entity.Student3;
import com.mufeng.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

/**
 * @author MFGN
 * @data 2022/3/13 23:50
 * @description
 */

public class Student3Test {
    @Test
    public void testInsertStudentWithPrimaryKey() {
        SqlSession sqlSession = null;
        try {
            sqlSession = MyBatisUtil.creatSqlSession();
            Student3 student3 = new Student3();
            student3.setMobile("151");
            student3.setName("学生3");
            sqlSession.insert("Student3.insertStudentWithPrimaryKey", student3);
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
}
