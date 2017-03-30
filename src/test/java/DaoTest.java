import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import xyz.fz.Application;
import xyz.fz.domain.role.TRoleAuth;
import xyz.fz.service.role.RoleAuthService;

/**
 * Created by fz on 2016/9/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class DaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RoleAuthService roleAuthService;

    @Test
    public void roleAddTest() {
        for (int i=0; i<100; i++) {
            jdbcTemplate.execute("insert into t_role(role_name, is_activity) values('客服" + i + "', 1)");
        }
    }

    @Test
    public void menuAddTest() {
        for (int i=0; i<100; i++) {
            jdbcTemplate.execute("insert into t_menu(menu_name, menu_path, sort, is_activity) values('菜单" + i + "', '/menu" + i + "', " + i + ", 1)");
        }
    }

    @Test
    public void roleAuthMenuAddTest() {
        TRoleAuth roleAuth = new TRoleAuth();
        roleAuth.setRoleId(105L);
        roleAuth.setMenuId(1L);
        roleAuth.setAuthId(0L);
        roleAuthService.saveRoleAuth(roleAuth);
    }
}
