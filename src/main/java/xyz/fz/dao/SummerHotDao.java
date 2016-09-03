package xyz.fz.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import xyz.fz.domain.SummerHot;

/**
 * Created by fz on 2016/8/11.
 */
@Repository
public interface SummerHotDao extends JpaRepository<SummerHot, Long> {

    @Query("select s from SummerHot s where s.name like concat('%', :name, '%')")
    Page<SummerHot> findByName(@Param("name") String name, Pageable pageable);
}
