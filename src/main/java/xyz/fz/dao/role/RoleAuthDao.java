package xyz.fz.dao.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.fz.domain.role.TRoleAuth;

/**
 * Created by fz on 2016/9/19.
 */
@Repository
public interface RoleAuthDao extends JpaRepository<TRoleAuth, Long> {

    @Modifying
    @Query("delete from TRoleAuth ra where ra.roleId = :roleId and ra.menuId = :menuId ")
    void delRoleMenu(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

    @Modifying
    @Query("delete from TRoleAuth ra where ra.menuId = :menuId ")
    void delRoleAuthByMenuId(@Param("menuId") Long menuId);

    @Modifying
    @Query("delete from TRoleAuth ra where ra.authId = :authId ")
    void delRoleAuthByAuthId(@Param("authId") Long authId);

    @Modifying
    @Query("delete from TRoleAuth ra where ra.roleId = :roleId ")
    void delRoleAuthByRoleId(@Param("roleId") Long roleId);
}
