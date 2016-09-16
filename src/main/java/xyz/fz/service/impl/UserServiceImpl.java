package xyz.fz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
@CacheConfig(cacheNames = "users")
public class UserServiceImpl implements UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserDao userDao;

    @Override
    @Cacheable
    public List<Map<String, Object>> userAllMapList() {

        return jdbcTemplate.queryForList("select * from summer_hot");
    }

    @Override
    @Cacheable
    public Page<TUser> userPageList(String name, int curPage, int pageSize) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(curPage, pageSize, sort);
        return userDao.findByName(name, pageable);
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

}
