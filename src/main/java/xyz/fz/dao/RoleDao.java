package xyz.fz.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.fz.domain.TRole;

/**
 * Created by fz on 2016/9/19.
 */
@Repository
public interface RoleDao extends JpaRepository<TRole, Long> {

    @Query("select r from TRole r where r.roleName like concat('%', :name, '%') ")
    Page<TRole> findByName(@Param("name") String name, Pageable pageable);

}
