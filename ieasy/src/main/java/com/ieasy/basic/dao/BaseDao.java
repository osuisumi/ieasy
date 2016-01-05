/**
 * 
 */
package com.ieasy.basic.dao;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.ieasy.basic.model.Pager;
import com.ieasy.basic.model.SystemContext;
import com.ieasy.basic.util.dbutil.IDBUtilsHelper;

/**
 * @author 杨浩泉
 *
 */
@SuppressWarnings("unchecked")
@Repository
public class BaseDao<T> implements IBaseDao<T> {

	@Inject
	private SessionFactory sessionFactory ;
	
	@Inject
	private IDBUtilsHelper dbHelper ;
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


	public IDBUtilsHelper getDbHelper() {
		return dbHelper;
	}

	public void setDbHelper(IDBUtilsHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	@Override
	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession() ;
	}
	
	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#add(java.lang.Object)
	 */
	@Override
	public T add(T entity) {
		this.getCurrentSession().save(entity) ;
		return entity;
	}

	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#update(java.lang.Object)
	 */
	@Override
	public void update(T entity) {
		this.getCurrentSession().update(entity) ;
	}
	
	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#delete(java.lang.Object)
	 */
	@Override
	public void delete(Class<T> clazz, Serializable id) {
		this.getCurrentSession().delete(this.load(clazz, id)) ;
	}

	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#delete(java.lang.Object)
	 */
	@Override
	public void delete(T entity) {
		this.getCurrentSession().delete(entity) ;
	}

	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#load(java.lang.Object)
	 */
	@Override
	public T load(Class<T> clazz, Serializable id) {
		return (T)this.getCurrentSession().load(clazz, id) ;
	}

	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#list(java.lang.String, java.lang.Object[])
	 */
	@Override
	public List<T> list(String hql, Object[] args) {
		return this.list(hql, args, null);
	}

	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#list(java.lang.String, java.lang.Object)
	 */
	@Override
	public List<T> list(String hql, Object arg) {
		return this.list(hql, new Object[]{arg});
	}

	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#list(java.lang.String)
	 */
	@Override
	public List<T> list(String hql) {
		return this.list(hql, null, null) ;
	}
	
	private String initSort(String hql) {
		String sort = SystemContext.getSort() ;
		String order = SystemContext.getOrder() ;
		if(sort != null && !"".equals(sort.trim())) {
			hql += " order by " + sort ;
			if(!"desc".equalsIgnoreCase(order)) hql += " asc" ;
			else hql += " desc" ;
		}
		SystemContext.removeSort() ;
		SystemContext.removeOrder() ;
		return hql ;
	}
	
	@SuppressWarnings("rawtypes")
	private void setAliasParams(Query query, Map<String, Object> alias) {
		if(alias != null) {
			Set<String> keys = alias.keySet() ;
			for (String key : keys) {
				Object val = alias.get(key) ;
				
				if(val instanceof Integer[]) {
					//查询条件是列表
					query.setParameterList(key, (Integer[])val) ;
				} else if(val instanceof String[]) {
					//查询条件是列表
					query.setParameterList(key, (String[])val) ;
				} else if(val instanceof List) {
					//查询条件是列表
					query.setParameterList(key, (List<Object>)val) ;
				} else if(val instanceof Collection) {
					//查询条件是列表
					query.setParameterList(key, (Collection)val) ;
				} else {
					query.setParameter(key, val);
				}
			}
		}
	}
	
	private void setArgsParams(Query query, Object[] args) {
		if(args != null && args.length > 0) {
			int index = 0;
			for(Object arg : args) {
				query.setParameter(index++, arg) ;
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#list(java.lang.String, java.lang.Object[], java.util.Map)
	 */
	@Override
	public List<T> list(String hql, Object[] args, Map<String, Object> alias) {
		hql = initSort(hql) ;
		
		Query query = this.getCurrentSession().createQuery(hql) ;
		setAliasParams(query, alias) ;
		setArgsParams(query, args) ;
		
		return query.list() ;
	}

	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#list(java.lang.String, java.util.Map)
	 */
	@Override
	public List<T> list(String hql, Map<String, Object> alias) {
		return this.list(hql, null, alias);
	}

	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#find(java.lang.String, java.lang.Object[])
	 */
	@Override
	public Pager<T> find(String hql, Object[] args) {
		return this.find(hql, args, null) ;
	}

	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#find(java.lang.String, java.lang.Object)
	 */
	@Override
	public Pager<T> find(String hql, Object arg) {
		return this.find(hql, new Object[]{arg}) ;
	}

	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#find(java.lang.String)
	 */
	@Override
	public Pager<T> find(String hql) {
		return this.find(hql, null, null) ;
	}
	
	@SuppressWarnings("rawtypes")
	private void setPagers(Query query, Pager pager) {
		Integer page = SystemContext.getPage() ;
		Integer rows = SystemContext.getRows() ;
		if(page == null || page < 0) page = 1 ;
		if(rows == null || rows < 0) rows = 30 ;
		pager.setPage(page) ;
		pager.setRows(rows) ;
		query.setFirstResult((page-1)*rows).setMaxResults(rows) ;
	}

	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#find(java.lang.String, java.lang.Object[], java.util.Map)
	 */
	@Override
	public Pager<T> find(String hql, Object[] args, Map<String, Object> alias) {
		Pager<T> pager = new Pager<T>() ;
		
		hql = initSort(hql) ;
		Query query = this.getCurrentSession().createQuery(hql) ;
		setPagers(query, pager) ;
		setAliasParams(query, alias) ;
		setArgsParams(query, args) ;
		
		pager.setDataRows(query.list()) ;
		pager.setTotal(this.count(hql, args, alias, true)) ;
		
		return pager;
	}

	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#find(java.lang.String, java.util.Map)
	 */
	@Override
	public Pager<T> find(String hql, Map<String, Object> alias) {
		return this.find(hql, null, alias);
	}

	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#queryObject(java.lang.String, java.lang.Object[])
	 */
	@Override
	public Object queryObject(String hql, Object[] args) {
		return this.queryObject(hql, args, null) ;
	}
	
	@Override
	public Object queryObject(String hql, Object[] args, Map<String, Object> alias) {
		Query query = this.getCurrentSession().createQuery(hql) ;
		setAliasParams(query, alias) ;
		setArgsParams(query, args) ;
		return query.uniqueResult() ;
	}

	@Override
	public Object queryObject(String hql, Map<String, Object> alias) {
		return this.queryObject(hql, null, alias) ;
	}

	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#queryObject(java.lang.String, java.lang.Object)
	 */
	@Override
	public Object queryObject(String hql, Object arg) {
		return this.queryObject(hql, new Object[]{arg}, null) ;
	}

	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#queryObject(java.lang.String)
	 */
	@Override
	public Object queryObject(String hql) {
		return this.queryObject(hql, null, null) ;
	}

	
	@Override
	public void updateByHql(String hql, Object[] args, Map<String, Object> alias) {
		Query query = this.getCurrentSession().createQuery(hql) ;
		setAliasParams(query, alias) ;
		setArgsParams(query, args) ;
		query.executeUpdate() ;
	}

	@Override
	public void updateByHql(String hql, Map<String, Object> alias) {
		this.updateByHql(hql, null, alias) ;
	}
	
	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#updateByHql(java.lang.String, java.lang.Object[])
	 */
	@Override
	public void updateByHql(String hql, Object[] args) {
		this.updateByHql(hql, args, null) ;
	}

	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#updateByHql(java.lang.String, java.lang.Object)
	 */
	@Override
	public void updateByHql(String hql, Object arg) {
		this.updateByHql(hql, new Object[]{arg}, null) ;
	}

	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#updateByHql(java.lang.String)
	 */
	@Override
	public void updateByHql(String hql) {
		this.updateByHql(hql, null, null) ;
	}

	
	@Override
	public Object queryObjectSQL(String sql, Object[] args, Class<?> clz,boolean hasEntity) {
		return this.queryObjectSQL(sql, args, null, clz, hasEntity) ;
	}
	
	@Override
	public Object queryObjectSQL(String sql, Object arg, Class<?> clz, boolean hasEntity) {
		return this.queryObjectSQL(sql, new Object[]{arg}, null, clz, hasEntity) ;
	}
	
	@Override
	public Object queryObjectSQL(String sql, Class<?> clz, boolean hasEntity) {
		return this.queryObjectSQL(sql, null, null, clz, hasEntity) ;
	}
	
	@Override
	public Object queryObjectSQL(String sql, Object[] args, Map<String, Object> alias, Class<?> clz, boolean hasEntity) {
		SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql) ;
		setAliasParams(sqlQuery, alias) ;
		setArgsParams(sqlQuery, args) ;
		if(hasEntity) {
			sqlQuery.addEntity(clz) ;
		} else {
			sqlQuery.setResultTransformer(Transformers.aliasToBean(clz)) ;
		}
		return sqlQuery.uniqueResult() ;
	}
	
	@Override
	public Object queryObjectSQL(String sql, Map<String, Object> alias, Class<?> clz, boolean hasEntity) {
		return this.queryObjectSQL(sql, null, alias, clz, hasEntity) ;
	}

	@Override
	public Object[] queryObjectSQL(String sql, Object[] args) {
		return this.queryObjectSQL(sql, args, null) ;
	}

	@Override
	public Object[] queryObjectSQL(String sql, Object arg) {
		return this.queryObjectSQL(sql, new Object[]{arg}, null) ;
	}

	@Override
	public Object[] queryObjectSQL(String sql) {
		return this.queryObjectSQL(sql, null, null) ;
	}

	@Override
	public Object[] queryObjectSQL(String sql, Object[] args, Map<String, Object> alias) {
		SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql) ;
		setAliasParams(sqlQuery, alias) ;
		setArgsParams(sqlQuery, args) ;
		return (Object[]) sqlQuery.uniqueResult() ;
	}

	@Override
	public Object[] queryObjectSQL(String sql, Map<String, Object> alias) {
		return this.queryObjectSQL(sql, null, alias) ;
	}
	
	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#listSQL(java.lang.String, java.lang.Object[], java.lang.Class, boolean)
	 */
	@Override
	public <N extends Object>List<N> listSQL(String sql, Object[] args, Class<?> clz, boolean hasEntity) {
		return this.listSQL(sql, args, null, clz, hasEntity) ;
	}

	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#listSQL(java.lang.String, java.lang.Object, java.lang.Class, boolean)
	 */
	@Override
	public <N extends Object>List<N> listSQL(String sql, Object arg, Class<?> clz, boolean hasEntity) {
		return this.listSQL(sql, new Object[]{arg}, null, clz, hasEntity) ;
	}

	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#listSQL(java.lang.String, java.lang.Class, boolean)
	 */
	@Override
	public <N extends Object>List<N> listSQL(String sql, Class<?> clz, boolean hasEntity) {
		return this.listSQL(sql, null, null, clz, hasEntity) ;
	}
	
	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#listSQL(java.lang.String, java.lang.Object[], java.util.Map, java.lang.Class, boolean)
	 */
	@Override
	public <N extends Object>List<N> listSQL(String sql, Object[] args, Map<String, Object> alias, Class<?> clz, boolean hasEntity) {
		sql = initSort(sql) ;
		SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql) ;
		setAliasParams(sqlQuery, alias) ;
		setArgsParams(sqlQuery, args) ;
		if(hasEntity) {
			sqlQuery.addEntity(clz) ;
		} else {
			sqlQuery.setResultTransformer(Transformers.aliasToBean(clz)) ;
		}
		return sqlQuery.list() ;
	}

	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#listSQL(java.lang.String, java.util.Map, java.lang.Class, boolean)
	 */
	@Override
	public <N extends Object>List<N> listSQL(String sql, Map<String, Object> alias, Class<?> clz,
			boolean hasEntity) {
		return this.listSQL(sql, null, alias, clz, hasEntity) ;
	}

	@Override
	public List<Object[]> listSQL(String sql, Object[] args, Map<String, Object> alias) {
		SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql) ;
		setAliasParams(sqlQuery, alias) ;
		setArgsParams(sqlQuery, args) ;
		return sqlQuery.list() ;
	}

	@Override
	public List<Object[]> listSQL(String sql, Map<String, Object> alias) {
		return this.listSQL(sql, null, alias) ;
	}

	@Override
	public List<Object[]> listSQL(String sql, Object[] args) {
		return this.listSQL(sql, args, null) ;
	}

	@Override
	public List<Object[]> listSQL(String sql, Object arg) {
		return this.listSQL(sql, new Object[]{arg}) ;
	}
	
	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#findSQL(java.lang.String, java.lang.Object[], java.lang.Class, boolean)
	 */
	@Override
	public <N extends Object>Pager<N> findSQL(String sql, Object[] args, Class<?> clz, boolean hasEntity) {
		return this.findSQL(sql, args, null, clz, hasEntity);
	}

	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#findSQL(java.lang.String, java.lang.Object, java.lang.Class, boolean)
	 */
	@Override
	public <N extends Object>Pager<N> findSQL(String sql, Object arg, Class<?> clz,
			boolean hasEntity) {
		return this.findSQL(sql, new Object[]{arg}, null, clz, hasEntity);
	}

	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#findSQL(java.lang.String, java.lang.Class, boolean)
	 */
	@Override
	public <N extends Object>Pager<N> findSQL(String sql, Class<?> clz, boolean hasEntity) {
		return this.findSQL(sql, null, null, clz, hasEntity);
	}
	
	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#findSQL(java.lang.String, java.lang.Object[], java.util.Map, java.lang.Class, boolean)
	 */
	@Override
	public <N extends Object>Pager<N> findSQL(String sql, Object[] args, Map<String, Object> alias, Class<?> clz, boolean hasEntity) {
		Pager<N> pager = new Pager<N>() ;
		
		sql = initSort(sql) ; 
		SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql) ;
		setAliasParams(sqlQuery, alias) ;
		setArgsParams(sqlQuery, args) ;
		setPagers(sqlQuery, pager) ;
		if(hasEntity) {
			sqlQuery.addEntity(clz) ;
		} else {
			sqlQuery.setResultTransformer(Transformers.aliasToBean(clz)) ;
		}
		pager.setDataRows(sqlQuery.list()) ;
		pager.setTotal(this.countSQL(sql, args, alias, false).longValue()) ;
		
		return pager;
	}

	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#findSQL(java.lang.String, java.util.Map, java.lang.Class, boolean)
	 */
	@Override
	public <N extends Object>Pager<N> findSQL(String sql, Map<String, Object> alias, Class<?> clz, boolean hasEntity) {
		return this.findSQL(sql, null, alias, clz, hasEntity);
	}
	
	@Override
	public int executeSQL(String sql, Object[] args) {
		return this.executeSQL(sql, args, null);
	}

	@Override
	public int executeSQL(String sql, Object arg) {
		return this.executeSQL(sql, new Object[]{arg}, null);
	}

	@Override
	public int executeSQL(String sql) {
		return this.executeSQL(sql, null, null);
	}

	@Override
	public int executeSQL(String sql, Object[] args, Map<String, Object> alias) {
		SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql) ;
		setAliasParams(sqlQuery, alias) ;
		setArgsParams(sqlQuery, args) ;
		return sqlQuery.executeUpdate() ;
	}

	@Override
	public int executeSQL(String sql, Map<String, Object> alias) {
		return this.executeSQL(sql, null, alias);
	}

	private String getCountHql(String hql, boolean isHQL) {
		String e = hql.substring(hql.lastIndexOf("from")) ;
		String c = "select count(*) " + e ;
		if(isHQL) c.replaceAll("fetch", "") ;
		return c ;
	}
	
	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#count(java.lang.String, java.lang.Object[], java.util.Map)
	 */
	@Override
	public Long count(String hql, Object[] args, Map<String, Object> alias, boolean isHQL) {
		hql = getCountHql(hql, isHQL) ;
		Query query = this.getCurrentSession().createQuery(hql) ;
		setAliasParams(query, alias) ;
		setArgsParams(query, args) ;
		return (Long) query.uniqueResult() ;
	}

	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#count(java.lang.String, java.lang.Object[])
	 */
	@Override
	public Long count(String hql, Object[] args, boolean isHQL) {
		return this.count(hql, args, null, isHQL) ;
	}

	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#count(java.lang.String, java.util.Map)
	 */
	@Override
	public Long count(String hql, Map<String, Object> alias, boolean isHQL) {
		return this.count(hql, null, alias, isHQL) ;
	}

	/* (non-Javadoc)
	 * @see com.ieasy.basic.dao.IBaseDao#count(java.lang.String)
	 */
	@Override
	public Long count(String hql, boolean isHQL) {
		return this.count(hql, null, null, isHQL);
	}

	
	private String getCountSql(String hql, boolean isHQL) {
		String e = hql.substring(hql.lastIndexOf("from")) ;
		String c = "select count(*) " + e ;
		if(isHQL) c.replaceAll("fetch", "") ;
		return c ;
	}
	
	
	@Override
	public BigInteger countSQL(String sql, Object[] args, Map<String, Object> alias, boolean isHQL) {
		sql = getCountSql(sql, isHQL) ;
		SQLQuery sqlQuery = this.getCurrentSession().createSQLQuery(sql) ;
		setAliasParams(sqlQuery, alias) ;
		setArgsParams(sqlQuery, args) ;
		return (BigInteger)sqlQuery.uniqueResult();
	}

	@Override
	public BigInteger countSQL(String sql, Object[] args, boolean isHQL) {
		return this.countSQL(sql, args, null, isHQL) ;
	}

	@Override
	public BigInteger countSQL(String sql, Map<String, Object> alias, boolean isHQL) {
		return this.countSQL(sql, null, alias, isHQL) ;
	}

	@Override
	public BigInteger countSQL(String sql, boolean isHQL) {
		return this.countSQL(sql, null, null, isHQL) ;
	}

	
}
