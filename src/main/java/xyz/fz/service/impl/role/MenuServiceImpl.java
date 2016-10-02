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
import xyz.fz.dao.role.MenuDao;
import xyz.fz.domain.role.TMenu;
import xyz.fz.service.role.AuthService;
import xyz.fz.service.role.MenuService;

/**
 * Created by fz on 2016/9/19.
 */
@Service
@Transactional
@CacheConfig(cacheNames = "menus")
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private AuthService authService;

    @Override
    @Cacheable
    public Page<TMenu> menuPageList(String menuName, int curPage, int pageSize) {

        Sort sort = new Sort(Sort.Direction.ASC, "sort");
        Pageable pageable = new PageRequest(curPage, pageSize, sort);
        return menuDao.findByName(menuName, pageable);
    }

    @Override
    @CacheEvict(allEntries = true)
    public TMenu saveMenu(TMenu menu) {
        return menuDao.save(menu);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void toggle(Long id, int isActivity) {
        TMenu menu = menuDao.findOne(id);
        menu.setIsActivity(isActivity);
        menuDao.save(menu);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void del(Long id) {
        authService.deleteByMenuId(id);
        menuDao.delete(id);
    }

}
