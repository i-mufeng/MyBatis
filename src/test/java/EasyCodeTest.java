import com.mufeng.entity.Student;
import com.mufeng.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

/**
 * @author MFGN
 * @data 2022/3/18 16:53
 * @description
 */

public class EasyCodeTest {
    @Test
    public void test() {
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
