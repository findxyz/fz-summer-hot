package xyz.fz.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.fz.domain.SummerHot;

/**
 * Created by fz on 2016/8/11.
 */
@Repository
public interface SummerHotDao extends JpaRepository<SummerHot, Long> {

}
