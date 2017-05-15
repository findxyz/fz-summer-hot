package xyz.fz.dao.wx;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.fz.domain.wx.TWxUser;

/**
 * Created by fz on 2016/8/11.
 */
@Repository
public interface WxUserDao extends JpaRepository<TWxUser, Long> {

    @Query("select wu from TWxUser wu where wu.openid = :openid ")
    TWxUser findByOpenid(@Param("openid") String openid);
}
