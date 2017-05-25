package xyz.fz.service.impl.role;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fz.dao.role.MenuDao;
import xyz.fz.domain.role.TMenu;
import xyz.fz.service.role.AuthService;
import xyz.fz.service.role.MenuService;
import xyz.fz.service.role.RoleAuthService;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

/**
 * Created by fz on 2016/9/19.
 */
@Service
public class MenuServiceImpl implements MenuService {

    private final MenuDao menuDao;

    private final AuthService authService;

    private final RoleAuthService roleAuthService;

    @Autowired
    public MenuServiceImpl(MenuDao menuDao, AuthService authService, RoleAuthService roleAuthService) {
        this.menuDao = menuDao;
        this.authService = authService;
        this.roleAuthService = roleAuthService;
    }

    @Override
    public Page<TMenu> menuPageList(String menuName, int curPage, int pageSize) {

        Sort sort = new Sort(Sort.Direction.ASC, "sort");
        Pageable pageable = new PageRequest(curPage, pageSize, sort);
        ExampleMatcher matcher = ExampleMatcher.matching();
        TMenu menu = new TMenu();
        if (!StringUtils.isEmpty(menuName)) {
            menu.setMenuName(menuName);
            matcher = matcher.withMatcher("menuName", contains().startsWith());
        }
        Example<TMenu> menuExample = Example.of(menu, matcher);
        return menuDao.findAll(menuExample, pageable);
    }

    @Override
    @CacheEvict(value = {"roleAuth", "treeData", "allTreeData"}, allEntries = true)
    public TMenu saveMenu(TMenu menu) {
        return menuDao.save(menu);
    }

    @Override
    @CacheEvict(value = {"roleAuth", "treeData", "allTreeData"}, allEntries = true)
    public void toggle(Long id, int isActivity) {
        TMenu menu = menuDao.findOne(id);
        menu.setIsActivity(isActivity);
        menuDao.save(menu);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"roleAuth", "treeData", "allTreeData"}, allEntries = true)
    public void del(Long id) {
        authService.deleteByMenuId(id);
        // done 删除角色权限中属于该菜单的权限
        roleAuthService.delRoleAuthByMenuId(id);
        menuDao.delete(id);
    }

}
