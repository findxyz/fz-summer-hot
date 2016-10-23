package xyz.fz.service.user;

import xyz.fz.dao.PagerData;
import xyz.fz.domain.user.TUser;

import java.util.Map;

/**
 * Created by fz on 2016/8/10.
 */
public interface UserService {

    PagerData<Map> userPageList(String userName, int curPage, int pageSize);

    TUser saveUser(TUser user);

    TUser findUser(String userName, String passWord);

    void resetPassWord(Long id);

    void start(Long id);

    void stop(Long id);

    void del(Long id);

    PagerData<Map> userRolePageList(Long userId, int curPage, int pageSize);

    void roleChange(Long roleId, Long userId);

    void modifyPassWord(Long id, String oldPassWord, String newPassWord);
}
