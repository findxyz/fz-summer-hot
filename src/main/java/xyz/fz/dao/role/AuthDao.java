package xyz.fz.dao.role;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.fz.domain.role.TAuth;

/**
 * Created by fz on 2016/9/19.
 */
@Repository
public interface AuthDao extends JpaRepository<TAuth, Long> {

    @Query("select a from TAuth a where a.menuId = :menuId ")
    Page<TAuth> findByMenuId(@Param("menuId") Long menuId, Pageable pageable);

    @Modifying
    @Query("delete from TAuth a where a.menuId = :menuId ")
    void deleteByMenuId(@Param("menuId") Long menuId);

}
