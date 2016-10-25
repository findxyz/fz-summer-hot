package xyz.fz.dao.impl;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import xyz.fz.dao.CommonDao;
import xyz.fz.dao.PagerData;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by fz on 2015/11/7.
 */
@Repository
@SuppressWarnings("unchecked")
public class CommonDaoImpl implements CommonDao {

    private static Logger logger = LoggerFactory.getLogger(CommonDaoImpl.class);

    private static final Set<String> mapClazz = new HashSet<String>() {{
        add("java.util.Map");
        add("java.util.LinkedHashMap");
        add("java.util.HashMap");
    }};

    private static final Set<String> countClazz = new HashSet<String>() {{
        add("java.lang.Long");
        add("java.lang.Integer");
        add("java.math.BigInteger");
    }};

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public <T> List<T> queryList(String hql, Map<String, Object> params) {
        try {
            Query query = entityManager.createQuery(hql);
            if (params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }
            return query.getResultList();
        } catch (Exception e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> PagerData<T> queryPagerData(String countHql, String hql, Map<String, Object> params, int currentPage, int pageSize) {
        // currentPage begin from index 0
        try {
            PagerData pagerData = new PagerData();
            Long count = querySingle(countHql, params);
            Query query = entityManager.createQuery(hql);
            if (params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }
            query.setFirstResult(currentPage * pageSize);
            query.setMaxResults(pageSize);
            pagerData.setTotalCount(count);
            pagerData.setData(query.getResultList());
            return pagerData;
        } catch (Exception e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T querySingle(String hql, Map<String, Object> params) {
        try {
            Query query = entityManager.createQuery(hql);
            if (params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }
            return (T) query.getSingleResult();
        } catch (Exception e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T findById(Class<T> clazz, Object id) {
        try {
            Object object = entityManager.find(clazz, id);
            return (T) object;
        } catch (Exception e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        }
    }


    @Override
    public <T> List<T> queryListBySql(String sql, Map<String, Object> params, Class<T> clazz) {
        try {
            if (mapClazz.contains(clazz.getName())) {
                Query query = entityManager.createNativeQuery(sql);
                if (params != null) {
                    for (Map.Entry<String, Object> entry : params.entrySet()) {
                        query.setParameter(entry.getKey(), entry.getValue());
                    }
                }
                query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                return query.getResultList();
            } else {
                Query query = entityManager.createNativeQuery(sql, clazz);
                if (params != null) {
                    for (Map.Entry<String, Object> entry : params.entrySet()) {
                        query.setParameter(entry.getKey(), entry.getValue());
                    }
                }
                return query.getResultList();
            }
        } catch (Exception e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> PagerData<T> queryPagerDataBySql(String countSql, String sql, Map<String, Object> params, int currentPage, int pageSize, Class<T> clazz) {
        // currentPage begin from index 0
        try {
            sql += " limit " + (currentPage * pageSize) + ", " + pageSize;
            PagerData pagerData = new PagerData();
            BigInteger count = querySingleBySql(countSql, params, BigInteger.class);
            if (mapClazz.contains(clazz.getName())) {
                Query query = entityManager.createNativeQuery(sql);
                if (params != null) {
                    for (Map.Entry<String, Object> entry : params.entrySet()) {
                        query.setParameter(entry.getKey(), entry.getValue());
                    }
                }
                query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                pagerData.setData(query.getResultList());
                pagerData.setTotalCount(count.longValue());
                return pagerData;
            } else {
                Query query = entityManager.createNativeQuery(sql, clazz);
                if (params != null) {
                    for (Map.Entry<String, Object> entry : params.entrySet()) {
                        query.setParameter(entry.getKey(), entry.getValue());
                    }
                }
                pagerData.setData(query.getResultList());
                pagerData.setTotalCount(count.longValue());
                return pagerData;
            }
        } catch (Exception e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T querySingleBySql(String sql, Map<String, Object> params, Class<T> clazz) {
        try {
            if (mapClazz.contains(clazz.getName())) {
                Query query = entityManager.createNativeQuery(sql);
                if (params != null) {
                    for (Map.Entry<String, Object> entry : params.entrySet()) {
                        query.setParameter(entry.getKey(), entry.getValue());
                    }
                }
                query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                return (T) query.getSingleResult();
            } else if (countClazz.contains(clazz.getName())) {
                Query query = entityManager.createNativeQuery(sql);
                if (params != null) {
                    for (Map.Entry<String, Object> entry : params.entrySet()) {
                        query.setParameter(entry.getKey(), entry.getValue());
                    }
                }
                return (T) query.getSingleResult();
            } else {
                Query query = entityManager.createNativeQuery(sql, clazz);
                if (params != null) {
                    for (Map.Entry<String, Object> entry : params.entrySet()) {
                        query.setParameter(entry.getKey(), entry.getValue());
                    }
                }
                return (T) query.getSingleResult();
            }
        } catch (Exception e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Object entity) {
        try {
            entityManager.persist(entity);
        } catch (Exception e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Object entity) {
        try {
            entityManager.merge(entity);
        } catch (Exception e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Object entity) {
        try {
            entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
        } catch (Exception e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    @Override
    public int execute(String hql, Map<String, Object> params) {
        try {
            Query query = entityManager.createQuery(hql);
            if (params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }
            return query.executeUpdate();
        } catch (Exception e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    @Override
    public int executeBySql(String sql, Map<String, Object> params) {
        try {
            Query query = entityManager.createNativeQuery(sql);
            if (params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }
            return query.executeUpdate();
        } catch (Exception e) {
            logger.error(e.toString());
            throw new RuntimeException(e);
        }
    }

}
