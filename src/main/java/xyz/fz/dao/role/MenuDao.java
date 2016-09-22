package xyz.fz.dao.role;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.fz.domain.role.TMenu;

/**
 * Created by fz on 2016/9/19.
 */
@Repository
public interface MenuDao extends JpaRepository<TMenu, Long> {

    @Query("select m from TMenu m where m.menuName like concat('%', :name, '%') ")
    Page<TMenu> findByName(@Param("name") String name, Pageable pageable);

}
