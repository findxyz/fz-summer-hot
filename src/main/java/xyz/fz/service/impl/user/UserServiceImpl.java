package xyz.fz.service.impl.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import xyz.fz.dao.CommonDao;
import xyz.fz.dao.PagerData;
import xyz.fz.dao.user.UserDao;
import xyz.fz.domain.user.TUser;
import xyz.fz.service.user.UserService;
import xyz.fz.utils.BaseUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fz on 2016/8/10.
 */
@Service
@Transactional
@CacheConfig(cacheNames = "users", keyGenerator = "myCKG")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CommonDao commonDao;

    @Override
    public PagerData<Map> userPageList(String name, int curPage, int pageSize) {

        String countSql = "SELECT count(*) ";

        String dataSql = "";
        dataSql += "SELECT ";
        dataSql += "u.id, ";
        dataSql += "u.user_name AS userName, ";
        dataSql += "u.real_name AS realName, ";
        dataSql += "u.is_activity AS isActivity, ";
        dataSql += "DATE_FORMAT( ";
        dataSql += "u.create_time, ";
        dataSql += "'%Y-%m-%d %H:%i:%s' ";
        dataSql += ") AS createTime, ";
        dataSql += "( ";
        dataSql += "CASE ";
        dataSql += "WHEN r.role_name IS NULL THEN ";
        dataSql += "'无' ";
        dataSql += "ELSE ";
        dataSql += "r.role_name ";
        dataSql += "END ";
        dataSql += ") AS roleName ";

        String bodySql = "";
        bodySql += "FROM ";
        bodySql += "t_user u ";
        bodySql += "LEFT JOIN t_role r ON u.role_id = r.id ";
        bodySql += "WHERE ";
        bodySql += "1 = 1 ";

        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(name)) {
            bodySql += "AND (u.user_name like concat('%', :name, '%') OR u.real_name like concat('%', :name, '%')) ";
            params.put("name", name);
        }

        countSql += bodySql;
        dataSql += bodySql + "order by u.create_time desc limit " + (curPage * pageSize) + ", " + pageSize;

        return commonDao.queryPagerDataBySql(countSql, dataSql, params, Map.class);

    }

    @Override
    @CacheEvict(allEntries = true)
    public TUser saveUser(TUser user) {

        return userDao.save(user);
    }

    @Override
    @Cacheable
    public TUser findUser(String userName, String passWord) {
        List<TUser> userList = userDao.findUserList(userName, BaseUtil.sha1Hex(passWord));
        if (userList != null && userList.size() == 1) {
            return userList.get(0);
        } else {
            return null;
        }
    }

    @Override
    @CacheEvict(allEntries = true)
    public void resetPassWord(Long id) {
        TUser user = userDao.findOne(id);
        String defaultPassWord = "88888888";
        user.setPassWord(BaseUtil.sha1Hex(defaultPassWord));
        userDao.save(user);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void start(Long id) {
        TUser user = userDao.findOne(id);
        user.setIsActivity(1);
        userDao.save(user);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void stop(Long id) {
        TUser user = userDao.findOne(id);
        user.setIsActivity(0);
        userDao.save(user);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void del(Long id) {
        TUser user = userDao.findOne(id);
        userDao.delete(user);
    }

    @Override
    public PagerData<Map> userRolePageList(Long userId, int curPage, int pageSize) {
        String countSql = "SELECT count(*) ";

        String dataSql = "";
        dataSql += "SELECT ";
        dataSql += "r.id, ";
        dataSql += "r.role_name AS roleName, ";
        dataSql += "r.is_activity AS isActivity, ";
        dataSql += "u.id AS userId ";

        String bodySql = "";
        bodySql += "FROM ";
        bodySql += "t_role r ";
        bodySql += "LEFT JOIN ( ";
        bodySql += "SELECT ";
        bodySql += "u.id, ";
        bodySql += "u.role_id ";
        bodySql += "FROM ";
        bodySql += "t_user u ";
        bodySql += "WHERE ";
        bodySql += "u.id = :userId ";
        bodySql += ") u ON u.role_id = r.id ";
        bodySql += "WHERE ";
        bodySql += "1 = 1 ";

        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);

        countSql += bodySql;
        dataSql += bodySql + "order by r.id limit " + (curPage * pageSize) + ", " + pageSize;

        return commonDao.queryPagerDataBySql(countSql, dataSql, params, Map.class);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void roleChange(Long roleId, Long userId) {
        TUser user = userDao.findOne(userId);
        user.setRoleId(roleId);
        userDao.save(user);
    }

}
