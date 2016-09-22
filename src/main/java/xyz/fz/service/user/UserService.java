package xyz.fz.service.user;

import org.springframework.data.domain.Page;
import xyz.fz.domain.user.TUser;

import java.util.List;
import java.util.Map;

/**
 * Created by fz on 2016/8/10.
 */
public interface UserService {

    List<Map<String, Object>> userAllMapList();

    Page<TUser> userPageList(String userName, int curPage, int pageSize);

    TUser saveUser(TUser user);

    TUser findUser(String userName, String passWord);

    void resetPassWord(Long id);

    void start(Long id);

    void stop(Long id);

    void del(Long id);
}
