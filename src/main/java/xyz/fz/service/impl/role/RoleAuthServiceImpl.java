package xyz.fz.service.impl.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fz.dao.CommonDao;
import xyz.fz.dao.PagerData;
import xyz.fz.dao.role.RoleAuthDao;
import xyz.fz.domain.role.TRoleAuth;
import xyz.fz.service.role.RoleAuthService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fz on 2016/9/19.
 */
@Service
@Transactional
@CacheConfig(cacheNames = "roleMenuAuths", keyGenerator = "myCKG")
public class RoleAuthServiceImpl implements RoleAuthService {

    @Autowired
    private RoleAuthDao roleAuthDao;

    @Autowired
    private CommonDao commonDao;

    @Override
    @Cacheable
    public PagerData<Map> roleAuthMenuPageList(Long roleId, int curPage, int pageSize) {

        String countSql = "select count(*) from t_role_auth ra inner join t_menu m on ra.menu_id = m.id where 1=1 ";
        String sql = "select ra.id as id, m.id as menuId, m.menu_name as menuName, m.is_activity as isActivity from t_role_auth ra inner join t_menu m on ra.menu_id = m.id where 1=1 ";
        String conditionSql = "";
        Map<String, Object> params = new HashMap<>();
        conditionSql += "and ra.role_id = :roleId ";
        conditionSql += "and ra.auth_id = 0 ";
        params.put("roleId", roleId);
        countSql += conditionSql;
        sql += conditionSql;
        sql += "order by m.sort limit " + (curPage * pageSize) + ", " + pageSize;
        return commonDao.queryPagerDataBySql(countSql, sql, params, Map.class);
    }

    @Override
    @CacheEvict(allEntries = true)
    public TRoleAuth saveRoleAuth(TRoleAuth roleAuth) {

        return roleAuthDao.save(roleAuth);
    }

    @Override
    @Cacheable
    public PagerData<Map> roleMenuPageList(Long roleId, int curPage, int pageSize) {

        String countSql = "SELECT count(*) ";

        String dataSql = "";
        dataSql += "SELECT ";
        dataSql += "ra.id, ";
        dataSql += "m.id as menuId, ";
        dataSql += "m.menu_name as menuName, ";
        dataSql += "m.is_activity as isActivity ";

        String bodySql = "";
        bodySql += "FROM ";
        bodySql += "t_menu m ";
        bodySql += "LEFT JOIN ( ";
        bodySql += "SELECT ";
        bodySql += "ra.* ";
        bodySql += "FROM ";
        bodySql += "t_role_auth ra ";
        bodySql += "WHERE ";
        bodySql += "1 = 1 ";
        bodySql += "AND ra.role_id = :roleId ";
        bodySql += "AND ra.auth_id = 0 ";
        bodySql += ") ra ON m.id = ra.menu_id ";
        bodySql += "WHERE ";
        bodySql += "1 = 1 ";

        Map<String, Object> params = new HashMap<>();
        params.put("roleId", roleId);
        countSql += bodySql;
        dataSql += bodySql + "order by m.sort limit " + (curPage * pageSize) + ", " + pageSize;

        return commonDao.queryPagerDataBySql(countSql, dataSql, params, Map.class);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void delRoleMenu(Long roleId, Long menuId) {
        roleAuthDao.delRoleMenu(roleId, menuId);
    }

    @Override
    @Cacheable
    public PagerData<Map> roleAuthPageList(Long roleId, Long menuId, int curPage, int pageSize) {

        String countSql = "SELECT count(*) ";

        String dataSql = "";
        dataSql += "SELECT ";
        dataSql += "ra.id, ";
        dataSql += "a.id AS authId, ";
        dataSql += "a.text AS authName, ";
        dataSql += "a.is_activity AS isActivity ";

        String bodySql = "";
        bodySql += "FROM ";
        bodySql += "t_auth a ";
        bodySql += "LEFT JOIN ( ";
        bodySql += "SELECT ";
        bodySql += "* ";
        bodySql += "FROM ";
        bodySql += "t_role_auth ra ";
        bodySql += "WHERE ";
        bodySql += "ra.role_id = :roleId ";
        bodySql += "AND ra.menu_id = :menuId ";
        bodySql += ") ra ON a.id = ra.auth_id ";
        bodySql += "WHERE ";
        bodySql += "a.menu_id = :menuId ";

        Map<String, Object> params = new HashMap<>();
        params.put("roleId", roleId);
        params.put("menuId", menuId);

        countSql += bodySql;
        dataSql += bodySql + "order by a.sort limit " + (curPage * pageSize) + ", " + pageSize;

        return commonDao.queryPagerDataBySql(countSql, dataSql, params, Map.class);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void delRoleAuth(Long roleAuthId) {
        roleAuthDao.delete(roleAuthId);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void delRoleAuthByMenuId(Long menuId) {
        roleAuthDao.delRoleAuthByMenuId(menuId);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void delRoleAuthByAuthId(Long authId) {
        roleAuthDao.delRoleAuthByAuthId(authId);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void delRoleAuthByRoleId(Long roleId) {
        roleAuthDao.delRoleAuthByRoleId(roleId);
    }

}
