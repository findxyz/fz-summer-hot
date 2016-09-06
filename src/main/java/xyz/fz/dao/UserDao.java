package xyz.fz.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.fz.domain.TUser;

/**
 * Created by fz on 2016/8/11.
 */
@Repository
public interface UserDao extends JpaRepository<TUser, Long> {

    @Query("select u from TUser u where u.userName like concat('%', :userName, '%')")
    Page<TUser> findByUserName(@Param("userName") String userName, Pageable pageable);
}
