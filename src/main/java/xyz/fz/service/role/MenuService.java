package xyz.fz.service.role;

import org.springframework.data.domain.Page;
import xyz.fz.domain.role.TMenu;

/**
 * Created by fz on 2016/9/19.
 */
public interface MenuService {

    Page<TMenu> menuPageList(String menuName, int curPage, int pageSize);

    TMenu saveMenu(TMenu menu);

    void toggle(Long id, int isActivity);

    void del(Long id);
}
