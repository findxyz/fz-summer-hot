package xyz.fz.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.fz.domain.TUser;

import java.util.List;

/**
 * Created by fz on 2016/8/11.
 */
@Repository
public interface UserDao extends JpaRepository<TUser, Long> {

    @Query("select u from TUser u where u.userName like concat('%', :name, '%') or u.realName like concat('%', :name, '%') ")
    Page<TUser> findByName(@Param("name") String name, Pageable pageable);

    @Query("select u from TUser u where u.userName = :userName and u.passWord = :passWord and u.isActivity = 1 ")
    List<TUser> findUserList(@Param("userName") String userName, @Param("passWord") String passWord);
}
