package xyz.fz.service.impl.role;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fz.dao.role.RoleDao;
import xyz.fz.domain.role.TRole;
import xyz.fz.service.role.RoleAuthService;
import xyz.fz.service.role.RoleService;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

/**
 * Created by fz on 2016/9/19.
 */
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    private final RoleAuthService roleAuthService;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao, RoleAuthService roleAuthService) {
        this.roleDao = roleDao;
        this.roleAuthService = roleAuthService;
    }

    @Override
    public Page<TRole> rolePageList(String roleName, int curPage, int pageSize) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(curPage, pageSize, sort);
        ExampleMatcher matcher = ExampleMatcher.matching();
        TRole role = new TRole();
        if (!StringUtils.isEmpty(roleName)) {
            role.setRoleName(roleName);
            matcher = matcher.withMatcher("roleName", contains().startsWith());
        }
        Example<TRole> roleExample = Example.of(role, matcher);
        return roleDao.findAll(roleExample, pageable);
    }

    @Override
    @CacheEvict(value = {"roleAuth", "treeData", "allTreeData"}, allEntries = true)
    public TRole saveRole(TRole role) {
        return roleDao.save(role);
    }

    @Override
    @CacheEvict(value = {"roleAuth", "treeData", "allTreeData"}, allEntries = true)
    public void toggle(Long id, int isActivity) {
        TRole role = roleDao.findOne(id);
        role.setIsActivity(isActivity);
        roleDao.save(role);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"roleAuth", "treeData", "allTreeData"}, allEntries = true)
    public void del(Long id) {
        roleDao.delete(id);
        // done 删除该角色所有的角色权限
        roleAuthService.delRoleAuthByRoleId(id);
    }

}
