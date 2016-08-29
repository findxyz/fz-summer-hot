package xyz.fz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.fz.dao.SummerHotDao;
import xyz.fz.domain.SummerHot;
import xyz.fz.service.SummerHotService;

import java.util.List;
import java.util.Map;

/**
 * Created by fz on 2016/8/10.
 */
@Service
@Transactional
public class SummerHotServiceImpl implements SummerHotService {

    private static final int pageSize = 20;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SummerHotDao summerHotDao;

    @Override
    public List<Map<String, Object>> summerHot() {

        return jdbcTemplate.queryForList("select * from summer_hot");
    }

    @Override
    public Page<SummerHot> pageSummerHot(int curPage) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(curPage, pageSize, sort);
        return summerHotDao.findAll(pageable);
    }

    @Override
    public SummerHot saveSummerHot(String name) {

        SummerHot summerHot = new SummerHot();
        summerHot.setName(name);
        return summerHotDao.save(summerHot);
    }


}
