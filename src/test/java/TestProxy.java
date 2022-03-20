import com.mufeng.dao.StudentDao;
import com.mufeng.entity.Student;
import com.mufeng.util.MyBatisUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;

/**
 * @author MFGN
 * @data 2022/3/17 20:47
 * @description
 */

public class TestProxy {
    @Test
    public void proxyTest() {
        try (SqlSession sqlSession = MyBatisUtil.creatSqlSession()) {
            StudentDao studentDao = sqlSession.getMapper(StudentDao.class);
            Student student = studentDao.queryById(1);
            System.out.println(student);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
