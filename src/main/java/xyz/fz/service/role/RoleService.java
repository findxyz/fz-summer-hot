package xyz.fz.service.role;

import org.springframework.data.domain.Page;
import xyz.fz.domain.role.TRole;

/**
 * Created by fz on 2016/9/19.
 */
public interface RoleService {

    Page<TRole> rolePageList(String roleName, int curPage, int pageSize);

    TRole saveRole(TRole role);

    void toggle(Long id, int isActivity);

    void del(Long id);
}
