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

        String countSql = "";
        String sql = "";
        String countHeaderSql = "SELECT count(*) ";
        String rowHeaderSql = "";
        rowHeaderSql += "SELECT ";
        rowHeaderSql += "ra.id, ";
        rowHeaderSql += "m.id as menuId, ";
        rowHeaderSql += "m.menu_name as menuName, ";
        rowHeaderSql += "m.is_activity as isActivity ";
        String sqlBody = "";
        sqlBody += "FROM ";
        sqlBody += "t_menu m ";
        sqlBody += "LEFT JOIN ( ";
        sqlBody += "SELECT ";
        sqlBody += "ra.* ";
        sqlBody += "FROM ";
        sqlBody += "t_role_auth ra ";
        sqlBody += "WHERE ";
        sqlBody += "1 = 1 ";
        sqlBody += "AND ra.role_id = :roleId ";
        sqlBody += "AND ra.auth_id = 0 ";
        sqlBody += ") ra ON m.id = ra.menu_id ";
        sqlBody += "WHERE ";
        sqlBody += "1 = 1 ";
        Map<String, Object> params = new HashMap<>();
        params.put("roleId", roleId);
        countSql += countHeaderSql + sqlBody;
        sql += rowHeaderSql + sqlBody + "order by m.sort limit " + (curPage * pageSize) + ", " + pageSize;
        return commonDao.queryPagerDataBySql(countSql, sql, params, Map.class);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void delRoleMenu(Long roleId, Long menuId) {
        roleAuthDao.delRoleMenu(roleId, menuId);
    }

    @Override
    @Cacheable
    public PagerData<Map> roleAuthPageList(Long roleId, Long menuId, int curPage, int pageSize) {

        String countSql = "";
        String sql = "";
        String countHeaderSql = "SELECT count(*) ";
        String rowHeaderSql = "";
        rowHeaderSql += "SELECT ";
        rowHeaderSql += "ra.id, ";
        rowHeaderSql += "a.id AS authId, ";
        rowHeaderSql += "a.text AS authName, ";
        rowHeaderSql += "a.is_activity AS isActivity ";
        String sqlBody = "";
        sqlBody += "FROM ";
        sqlBody += "t_auth a ";
        sqlBody += "LEFT JOIN ( ";
        sqlBody += "SELECT ";
        sqlBody += "* ";
        sqlBody += "FROM ";
        sqlBody += "t_role_auth ra ";
        sqlBody += "WHERE ";
        sqlBody += "ra.role_id = :roleId ";
        sqlBody += "AND ra.menu_id = :menuId ";
        sqlBody += ") ra ON a.id = ra.auth_id ";
        sqlBody += "WHERE ";
        sqlBody += "a.menu_id = :menuId ";
        Map<String, Object> params = new HashMap<>();
        params.put("roleId", roleId);
        params.put("menuId", menuId);
        countSql += countHeaderSql + sqlBody;
        sql += rowHeaderSql + sqlBody + "order by a.sort limit " + (curPage * pageSize) + ", " + pageSize;
        return commonDao.queryPagerDataBySql(countSql, sql, params, Map.class);
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
