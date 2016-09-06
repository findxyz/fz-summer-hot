package xyz.fz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fz.dao.UserDao;
import xyz.fz.domain.TUser;
import xyz.fz.service.UserService;
import xyz.fz.utils.BaseUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by fz on 2016/8/10.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final String defaultPassWord = "88888888";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserDao userDao;

    @Override
    public List<Map<String, Object>> userAllMapList() {

        return jdbcTemplate.queryForList("select * from summer_hot");
    }

    @Override
    public Page<TUser> userPageList(String userName, int curPage, int pageSize) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(curPage, pageSize, sort);
        return userDao.findByUserName(userName, pageable);
    }

    @Override
    public TUser saveUser(TUser user) {

        return userDao.save(user);
    }

    @Override
    public boolean resetPassWord(Long id) {

        TUser user = userDao.findOne(id);
        user.setPassWord(BaseUtil.sha1Hex(defaultPassWord));
        boolean success;
        try {
            userDao.save(user);
            success = true;
        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        }
        return success;
    }


}
