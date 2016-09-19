import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import xyz.fz.Application;

/**
 * Created by fz on 2016/9/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class EveryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void roleAddTest() {
        for (int i=0; i<100; i++) {
            jdbcTemplate.execute("insert into t_role(role_name, is_activity) values('客服" + i + "', 1)");
        }
    }
}
