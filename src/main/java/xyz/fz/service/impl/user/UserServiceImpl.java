package xyz.fz.service.impl.user;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.fz.dao.CommonDao;
import xyz.fz.dao.PagerData;
import xyz.fz.dao.user.UserDao;
import xyz.fz.domain.user.TUser;
import xyz.fz.service.user.UserService;
import xyz.fz.util.BaseUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fz on 2016/8/10.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final CommonDao commonDao;

    @Autowired
    public UserServiceImpl(UserDao userDao, CommonDao commonDao) {
        this.userDao = userDao;
        this.commonDao = commonDao;
    }

    @Override
    public PagerData<Map> userPageList(String name, int curPage, int pageSize) {

        String countSql = "SELECT COUNT(0) ";

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
        dataSql += "WHEN u.role_id IS NULL THEN ";
        dataSql += "'无' ";
        dataSql += "WHEN u.role_id = 0 THEN ";
        dataSql += "'超级管理员' ";
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
        bodySql += "AND (u.role_id <> 0 OR u.role_id IS NULL) ";

        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isEmpty(name)) {
            bodySql += "AND (u.user_name like concat('%', :name, '%') OR u.real_name like concat('%', :name, '%')) ";
            params.put("name", name);
        }

        countSql += bodySql;
        dataSql += bodySql + "order by u.create_time desc";

        return commonDao.queryPagerDataBySql(countSql, dataSql, params, curPage, pageSize, Map.class);

    }

    @Override
    public TUser saveUser(TUser user) {

        return userDao.save(user);
    }

    @Override
    public TUser findUser(String userName, String passWord) {
        List<TUser> userList = userDao.findList(userName, BaseUtil.sha256Hex(passWord));
        if (userList != null && userList.size() == 1) {
            return userList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void resetPassWord(Long id) {
        TUser user = userDao.findOne(id);
        String defaultPassWord = "88888888";
        user.setPassWord(BaseUtil.sha256Hex(defaultPassWord));
        userDao.save(user);
    }

    @Override
    public void start(Long id) {
        TUser user = userDao.findOne(id);
        user.setIsActivity(1);
        userDao.save(user);
    }

    @Override
    public void stop(Long id) {
        TUser user = userDao.findOne(id);
        user.setIsActivity(0);
        userDao.save(user);
    }

    @Override
    public void del(Long id) {
        TUser user = userDao.findOne(id);
        userDao.delete(user);
    }

    @Override
    public PagerData<Map> userRolePageList(Long userId, int curPage, int pageSize) {
        String countSql = "SELECT COUNT(0) ";

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
        dataSql += bodySql + "order by r.id ";

        return commonDao.queryPagerDataBySql(countSql, dataSql, params, curPage, pageSize, Map.class);
    }

    @Override
    public void roleChange(Long roleId, Long userId) {
        TUser user = userDao.findOne(userId);
        user.setRoleId(roleId);
        userDao.save(user);
    }

    @Override
    public void modifyPassWord(Long id, String oldPassWord, String newPassWord) {
        TUser user = userDao.findOne(id);
        if (StringUtils.equals(BaseUtil.sha256Hex(oldPassWord), user.getPassWord())) {
            user.setPassWord(BaseUtil.sha256Hex(newPassWord));
            userDao.save(user);
        } else {
            throw new RuntimeException("原密码错误");
        }
    }

}
