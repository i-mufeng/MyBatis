import com.mufeng.dao.StudentDao;
import com.mufeng.entity.Student;
import com.mufeng.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

/**
 * @author MFGN
 * @data 2022/3/18 15:07
 * @description 多对一查询查询
 */

public class ManyToOneTest {
    /**
     * 查询id为2的学生信息以及其报名的课程信息
     */
    @Test
    public void testManyToOne() {
        SqlSession sqlSession = null;
        try {
            sqlSession = MyBatisUtil.creatSqlSession();
            StudentDao mapper = sqlSession.getMapper(StudentDao.class);
            Student student = mapper.selectManyToOne1(1);
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
}
