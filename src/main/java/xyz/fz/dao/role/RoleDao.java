package xyz.fz.dao.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.fz.domain.role.TRole;

/**
 * Created by fz on 2016/9/19.
 */
@Repository
public interface RoleDao extends JpaRepository<TRole, Long> {

}
