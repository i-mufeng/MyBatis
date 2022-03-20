import com.mufeng.entity.Student;
import com.mufeng.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

/**
 * @author MFGN
 * @data 2022/3/17 8:53
 * @description 测试缓存
 */

public class CacheTest {
    /**
     * 测试一级缓存
     */
    @Test
    public void testLevel1() {
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
    }
}
