package xyz.fz.service.impl.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import xyz.fz.dao.CommonDao;
import xyz.fz.dao.PagerData;
import xyz.fz.dao.role.RoleAuthDao;
import xyz.fz.domain.role.TRoleAuth;
import xyz.fz.service.role.RoleAuthService;

import java.util.*;

/**
 * Created by fz on 2016/9/19.
 */
@Service
public class RoleAuthServiceImpl implements RoleAuthService {

    @Autowired
    private RoleAuthDao roleAuthDao;

    @Autowired
    private CommonDao commonDao;

    @Override
    public PagerData<Map> roleAuthMenuPageList(Long roleId, int curPage, int pageSize) {

        String countSql = "select count(0) from t_role_auth ra inner join t_menu m on ra.menu_id = m.id where 1=1 ";
        String sql = "select ra.id as id, m.id as menuId, m.menu_name as menuName, m.is_activity as isActivity from t_role_auth ra inner join t_menu m on ra.menu_id = m.id where 1=1 ";
        String conditionSql = "";
        Map<String, Object> params = new HashMap<>();
        conditionSql += "and ra.role_id = :roleId ";
        conditionSql += "and ra.auth_id = 0 ";
        params.put("roleId", roleId);
        countSql += conditionSql;
        sql += conditionSql;
        sql += "order by m.sort ";
        return commonDao.queryPagerDataBySql(countSql, sql, params, curPage, pageSize, Map.class);
    }

    @Override
    @CacheEvict(value = {"roleAuth", "treeData", "allTreeData"}, allEntries = true)
    public TRoleAuth saveRoleAuth(TRoleAuth roleAuth) {

        return roleAuthDao.save(roleAuth);
    }

    @Override
    public PagerData<Map> roleMenuPageList(Long roleId, int curPage, int pageSize) {

        String countSql = "SELECT COUNT(0) ";

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
        dataSql += bodySql + "order by m.sort ";

        return commonDao.queryPagerDataBySql(countSql, dataSql, params, curPage, pageSize, Map.class);
    }

    @Override
    @CacheEvict(value = {"roleAuth", "treeData", "allTreeData"}, allEntries = true)
    public void delRoleMenu(Long roleId, Long menuId) {
        roleAuthDao.delRoleMenu(roleId, menuId);
    }

    @Override
    public PagerData<Map> roleAuthPageList(Long roleId, Long menuId, int curPage, int pageSize) {

        String countSql = "SELECT COUNT(0) ";

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
        dataSql += bodySql + "order by a.sort ";

        return commonDao.queryPagerDataBySql(countSql, dataSql, params, curPage, pageSize, Map.class);
    }

    @Override
    @CacheEvict(value = {"roleAuth", "treeData", "allTreeData"}, allEntries = true)
    public void delRoleAuth(Long roleAuthId) {
        roleAuthDao.delete(roleAuthId);
    }

    @Override
    @CacheEvict(value = {"roleAuth", "treeData", "allTreeData"}, allEntries = true)
    public void delRoleAuthByMenuId(Long menuId) {
        roleAuthDao.delRoleAuthByMenuId(menuId);
    }

    @Override
    @CacheEvict(value = {"roleAuth", "treeData", "allTreeData"}, allEntries = true)
    public void delRoleAuthByAuthId(Long authId) {
        roleAuthDao.delRoleAuthByAuthId(authId);
    }

    @Override
    @CacheEvict(value = {"roleAuth", "treeData", "allTreeData"}, allEntries = true)
    public void delRoleAuthByRoleId(Long roleId) {
        roleAuthDao.delRoleAuthByRoleId(roleId);
    }

    @Override
    public List<Map> getRoleAuthData(Long roleId) {
        String sql = "";
        sql += "SELECT ";
        sql += "m.menu_name AS menuName, ";
        sql += "m.menu_path AS menuPath, ";
        sql += "( ";
        sql += "CASE ";
        sql += "WHEN ra.menu_id IS NULL THEN ";
        sql += "0 ";
        sql += "ELSE ";
        sql += "m.is_activity ";
        sql += "END ";
        sql += ") AS mIsActivity, ";
        sql += "a.text, ";
        sql += "a.url, ";
        sql += "( ";
        sql += "CASE ";
        sql += "WHEN ra.auth_id IS NULL THEN ";
        sql += "0 ";
        sql += "ELSE ";
        sql += "a.is_activity ";
        sql += "END ";
        sql += ") AS aIsActivity ";
        sql += "FROM ";
        sql += "t_menu m ";
        sql += "INNER JOIN t_auth a ON m.id = a.menu_id ";
        sql += "LEFT JOIN ( ";
        sql += "SELECT ";
        sql += "* ";
        sql += "FROM ";
        sql += "t_role_auth ra ";
        sql += "WHERE ";
        sql += "ra.role_id = :roleId ";
        sql += ") ra ON ra.auth_id = a.id ";
        sql += "AND ra.menu_id = m.id ";
        sql += "WHERE ";
        sql += "1 = 1 ";
        sql += "ORDER BY ";
        sql += "m.sort, ";
        sql += "a.sort ";
        Map<String, Object> params = new HashMap<>();
        params.put("roleId", roleId);
        return commonDao.queryListBySql(sql, params, Map.class);
    }

    @Override
    @Cacheable(value = "roleAuth", keyGenerator = "myRedisKeyGenerator")
    public Map<String, Object> getRoleAuthMap(Long roleId) {

        Map<String, Object> result = new HashMap<>();
        Set<String> menuSet = new HashSet<>();
        Set<String> authNoSet = new HashSet<>();
        List<Map> list = getRoleAuthData(roleId);
        for (Map m : list) {
            if (m.get("mIsActivity").toString().equals("1")) {
                menuSet.add(m.get("menuPath").toString());
            }
            if (m.get("aIsActivity").toString().equals("0")) {
                authNoSet.add(m.get("url").toString());
            }
        }
        result.put("menuSet", menuSet);
        result.put("authNoSet", authNoSet);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    @Cacheable(value = "treeData", keyGenerator = "myRedisKeyGenerator")
    public List<Map> getTreeData(Long roleId) {

        List<Map> list = getRoleAuthData(roleId);
        List<Map> treeList = new ArrayList<>();
        Map menuMap = null;
        List<Map> children = null;
        for (Map m : list) {
            if (menuMap == null || !menuMap.get("menuPath").equals(m.get("menuPath").toString())) {
                if (m.get("mIsActivity").toString().equals("1")) {
                    menuMap = new HashMap();
                    children = new ArrayList<>();
                    menuMap.put("children", children);
                    menuMap.put("menuPath", m.get("menuPath").toString());
                    menuMap.put("text", m.get("menuName").toString());
                    menuMap.put("isexpand", true);
                    if (m.get("aIsActivity").toString().equals("1")) {
                        Map child = new HashMap();
                        child.put("text", m.get("text").toString());
                        child.put("url", m.get("url").toString());
                        children.add(child);
                    }
                    treeList.add(menuMap);
                }
            } else {
                if (m.get("aIsActivity").toString().equals("1")) {
                    Map child = new HashMap();
                    child.put("text", m.get("text").toString());
                    child.put("url", m.get("url").toString());
                    children.add(child);
                }
            }
        }
        return treeList;
    }

    @Override
    @SuppressWarnings("unchecked")
    @Cacheable(value = "allTreeData", keyGenerator = "myRedisKeyGenerator")
    public List<Map> getAllTreeData() {
        String sql = "";
        sql += "SELECT ";
        sql += "m.menu_name AS menuName, ";
        sql += "m.menu_path AS menuPath, ";
        sql += "a.text, ";
        sql += "a.url ";
        sql += "FROM ";
        sql += "t_menu m ";
        sql += "INNER JOIN t_auth a ON m.id = a.menu_id ";
        sql += "WHERE ";
        sql += "1 = 1 ";
        sql += "ORDER BY ";
        sql += "m.sort, ";
        sql += "a.sort ";
        Map<String, Object> params = new HashMap<>();
        List<Map> list = commonDao.queryListBySql(sql, params, Map.class);
        List<Map> treeList = new ArrayList<>();
        Map menuMap = null;
        List<Map> children = null;
        for (Map m : list) {
            if (menuMap == null || !menuMap.get("menuPath").equals(m.get("menuPath").toString())) {
                menuMap = new HashMap();
                children = new ArrayList<>();
                menuMap.put("children", children);
                menuMap.put("menuPath", m.get("menuPath").toString());
                menuMap.put("text", m.get("menuName").toString());
                menuMap.put("isexpand", true);
                Map child = new HashMap();
                child.put("text", m.get("text").toString());
                child.put("url", m.get("url").toString());
                children.add(child);
                treeList.add(menuMap);
            } else {
                Map child = new HashMap();
                child.put("text", m.get("text").toString());
                child.put("url", m.get("url").toString());
                children.add(child);
            }
        }
        return treeList;
    }

}
