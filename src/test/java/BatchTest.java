import com.mufeng.dao.StudentDao;
import com.mufeng.entity.Student;
import com.mufeng.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

/**
 * @author MFGN
 * @data 2022/3/20 9:51
 * @description 批处理
 */

public class BatchTest {
    @Test
    public void testBatch() {
        SqlSession sqlSession = null;
        long start = System.currentTimeMillis();
        try {
            sqlSession = MyBatisUtil.creatSqlSession();
            StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
            Student student = null;

            for (int i = 0; i < 20000; i++) {
                student = new Student();
                student.setName("沐风" + i);
                student.setMobile("123123");
                student.setCourseId(1);
                studentDao.insert(student);
            }
            sqlSession.commit();
            long end = System.currentTimeMillis();
            System.err.println("本次批量插入耗时：" + (end - start) + "毫秒");
        } catch (Exception e) {
            e.printStackTrace();
            assert sqlSession != null;
            sqlSession.rollback();
        }finally {
            assert sqlSession != null;
            MyBatisUtil.closeSqlSession(sqlSession);
        }
    }
}
