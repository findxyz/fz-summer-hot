package xyz.fz.service.impl.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fz.dao.role.AuthDao;
import xyz.fz.domain.role.TAuth;
import xyz.fz.service.role.AuthService;
import xyz.fz.service.role.RoleAuthService;

/**
 * Created by fz on 2016/9/19.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthDao authDao;

    private final RoleAuthService roleAuthService;

    @Autowired
    public AuthServiceImpl(AuthDao authDao, RoleAuthService roleAuthService) {
        this.authDao = authDao;
        this.roleAuthService = roleAuthService;
    }

    @Override
    public Page<TAuth> authPageList(Long menuId, int curPage, int pageSize) {

        Sort sort = new Sort(Sort.Direction.ASC, "sort");
        Pageable pageable = new PageRequest(curPage, pageSize, sort);
        return authDao.findByMenuId(menuId, pageable);
    }

    @Override
    @CacheEvict(value = {"roleAuth", "treeData", "allTreeData"}, allEntries = true)
    public TAuth saveAuth(TAuth auth) {
        return authDao.save(auth);
    }

    @Override
    @CacheEvict(value = {"roleAuth", "treeData", "allTreeData"}, allEntries = true)
    public void deleteByMenuId(Long menuId) {
        authDao.deleteByMenuId(menuId);
    }

    @Override
    @CacheEvict(value = {"roleAuth", "treeData", "allTreeData"}, allEntries = true)
    public void toggle(Long id, int isActivity) {
        TAuth auth = authDao.findOne(id);
        auth.setIsActivity(isActivity);
        authDao.save(auth);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"roleAuth", "treeData", "allTreeData"}, allEntries = true)
    public void del(Long id) {
        authDao.delete(id);
        // done 删除角色权限中属于该权限的数据
        roleAuthService.delRoleAuthByAuthId(id);
    }

}
