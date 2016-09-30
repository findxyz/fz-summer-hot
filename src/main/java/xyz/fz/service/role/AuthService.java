package xyz.fz.service.role;

import org.springframework.data.domain.Page;
import xyz.fz.domain.role.TAuth;

/**
 * Created by fz on 2016/9/19.
 */
public interface AuthService {

    Page<TAuth> authPageList(Long menuId, int curPage, int pageSize);

    TAuth saveAuth(TAuth auth);
}
