package xyz.fz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fz.dao.RoleDao;
import xyz.fz.domain.TRole;
import xyz.fz.service.RoleService;

/**
 * Created by fz on 2016/9/19.
 */
@Service
@Transactional
@CacheConfig(cacheNames = "roles")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public Page<TRole> rolePageList(String roleName, int curPage, int pageSize) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(curPage, pageSize, sort);
        return roleDao.findByName(roleName, pageable);
    }
}
