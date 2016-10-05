package xyz.fz.dao.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.fz.domain.user.TUser;

import java.util.List;

/**
 * Created by fz on 2016/8/11.
 */
@Repository
public interface UserDao extends JpaRepository<TUser, Long> {

    @Query("select u from TUser u where u.userName = :userName and u.passWord = :passWord and u.isActivity = 1 ")
    List<TUser> findUserList(@Param("userName") String userName, @Param("passWord") String passWord);
}
