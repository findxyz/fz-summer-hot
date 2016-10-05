package xyz.fz.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by fz on 2015/11/7.
 */
public interface CommonDao {

    /* hql相关查询 */

    <T> List<T> queryList(String hql);

    <T> List<T> queryList(String hql, Map<String, Object> params);

    <T> List<T> queryListLimitCount(String hql, int maxCount);

    <T> List queryListLimitCount(String hql, Map<String, Object> params, int maxCount);

    <T> List<T> queryPageList(String hql, int currentPage, int pageSize);

    <T> List<T> queryPageList(String hql, Map<String, Object> params, int currentPage, int pageSize);

    <T> PagerData<T> queryPagerData(String countHql, String hql, int currentPage, int pageSize);

    <T> PagerData<T> queryPagerData(String countHql, String hql, Map<String, Object> params, int currentPage, int pageSize);

    <T> T getSingle(String hql);

    <T> T getSingle(String hql, Map<String, Object> params);

    <T> T findById(Class<T> clazz, Object id);

    /* sql相关查询 */

    <T> List<T> queryListBySql(String sql, Class<T> clazz);

    <T> List<T> queryListBySql(String sql, Map<String, Object> params, Class<T> clazz);

    <T> PagerData<T> queryPagerDataBySql(String countSql, String sql, Class<T> clazz);

    <T> PagerData<T> queryPagerDataBySql(String countSql, String sql, Map<String, Object> params, Class<T> clazz);

    <T> T getSingleBySql(String sql, Class<T> clazz);

    <T> T getSingleBySql(String sql, Map<String, Object> params, Class<T> clazz);

    /* 非查询dao方法 */

    void save(Object entity);

    void update(Object entity);

    void delete(Object entity);

    int execute(String hql);

    int execute(String hql, Map<String, Object> params);

    int executeBySql(String sql);

    int executeBySql(String sql, Map<String, Object> params);
}
