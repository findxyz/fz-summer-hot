package xyz.fz.service;

import org.springframework.data.domain.Page;
import xyz.fz.domain.TRole;

/**
 * Created by fz on 2016/9/19.
 */
public interface RoleService {

    Page<TRole> rolePageList(String roleName, int curPage, int pageSize);
}
