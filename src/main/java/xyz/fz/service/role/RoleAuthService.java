package xyz.fz.service.role;

import xyz.fz.dao.PagerData;
import xyz.fz.domain.role.TRoleAuth;

import java.util.List;
import java.util.Map;

/**
 * Created by fz on 2016/9/19.
 */
public interface RoleAuthService {

    PagerData<Map> roleAuthMenuPageList(Long roleId, int curPage, int pageSize);

    TRoleAuth saveRoleAuth(TRoleAuth roleAuth);

    PagerData<Map> roleMenuPageList(Long roleId, int curPage, int pageSize);

    void delRoleMenu(Long roleId, Long menuId);

    PagerData<Map> roleAuthPageList(Long roleId, Long menuId, int curPage, int pageSize);

    void delRoleAuth(Long roleAuthId);

    void delRoleAuthByMenuId(Long menuId);

    void delRoleAuthByAuthId(Long authId);

    void delRoleAuthByRoleId(Long roleId);

    List<Map> getRoleAuthData(Long roleId);

    Map<String, Object> getRoleAuthMap(Long roleId);

    List<Map> getTreeData(Long roleId);

    List<Map> getAllTreeData();
}
