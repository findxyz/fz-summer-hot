package xyz.fz.service.impl.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fz.dao.role.AuthDao;
import xyz.fz.domain.role.TAuth;
import xyz.fz.service.role.AuthService;

/**
 * Created by fz on 2016/9/19.
 */
@Service
@Transactional
@CacheConfig(cacheNames = "auths")
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthDao authDao;

    @Override
    @Cacheable
    public Page<TAuth> authPageList(Long menuId, int curPage, int pageSize) {

        Sort sort = new Sort(Sort.Direction.ASC, "sort");
        Pageable pageable = new PageRequest(curPage, pageSize, sort);
        return authDao.findByMenuId(menuId, pageable);
    }

    @Override
    @CacheEvict(allEntries = true)
    public TAuth saveAuth(TAuth auth) {
        return authDao.save(auth);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void deleteByMenuId(Long menuId) {
        authDao.deleteByMenuId(menuId);
        // todo 删除角色权限中包含该菜单的权限
    }

    @Override
    @CacheEvict(allEntries = true)
    public void toggle(Long id, int isActivity) {
        TAuth auth = authDao.findOne(id);
        auth.setIsActivity(isActivity);
        authDao.save(auth);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void del(Long id) {
        authDao.delete(id);
        // todo 删除角色权限中有该权限的权限
    }

}
