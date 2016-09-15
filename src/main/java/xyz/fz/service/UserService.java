package xyz.fz.service;

import org.springframework.data.domain.Page;
import xyz.fz.domain.TUser;

import java.util.List;
import java.util.Map;

/**
 * Created by fz on 2016/8/10.
 */
public interface UserService {

    List<Map<String, Object>> userAllMapList();

    Page<TUser> userPageList(String userName, int curPage, int pageSize);

    TUser saveUser(TUser user);

    void resetPassWord(Long id);

    TUser findUser(String userName, String passWord);
}
