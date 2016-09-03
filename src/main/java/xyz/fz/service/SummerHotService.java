package xyz.fz.service;

import org.springframework.data.domain.Page;
import xyz.fz.domain.SummerHot;

import java.util.List;
import java.util.Map;

/**
 * Created by fz on 2016/8/10.
 */
public interface SummerHotService {

    List<Map<String, Object>> summerHot();

    Page<SummerHot> pageSummerHot(String name, int curPage, int pageSize);

    SummerHot saveSummerHot(String name);
}
