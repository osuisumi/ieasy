package com.ieasy.basic.dao;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.ieasy.basic.model.Pager;
import com.ieasy.basic.util.dbutil.IDBUtilsHelper;

/**
 * 公共的DAO处理对象，这个对象中包含了Hibernate的所有基本操作和对SQL的操作
 * @author 杨浩泉
 *
 * @param <T>
 */
public interface IBaseDao<T> {

	public Session getCurrentSession() ;
	
	public IDBUtilsHelper getDbHelper() ;
	
	/**
	 * 添加对象
	 * @param entity
	 * @return
	 */
	public T add(T entity) ;
	
	/**
	 * 更新对象
	 * @param entity
	 */
	public void update(T entity) ;
	
	/**
	 * 根据ID删除对象
	 * @param id
	 */
	public void delete(Class<T> clazz, Serializable id) ;
	
	/**
	 * 根据ID删除对象
	 * @param id
	 */
	public void delete(T entity) ;
	
	/**
	 * 根据ID加载对象
	 * @param id
	 */
	public T load(Class<T> clazz, Serializable id) ;
	
	/**
	 * 不分页列表对象
	 * @param hql 查询对象的HQL
	 * @param args 查询参数
	 * @return	返回一组列表对象
	 */
	public List<T> list(String hql, Object[] args) ;
	public List<T> list(String hql, Object arg) ;
	public List<T> list(String hql) ;
	/**
	 * 基于别名和查询参数的混合列表对象
	 * @param hql
	 * @param args
	 * @param alias 别名对象 
	 * @return
	 */
	public List<T> list(String hql, Object[] args, Map<String, Object> alias) ;
	public List<T> list(String hql, Map<String, Object> alias) ;
	
	
	/**
	 * 分页列表对象
	 * @param hql 查询对象的HQL
	 * @param args 查询参数
	 * @return	返回一组列表对象
	 */
	public Pager<T> find(String hql, Object[] args) ;
	public Pager<T> find(String hql, Object arg) ;
	public Pager<T> find(String hql) ;
	/**
	 * 基于别名和查询参数的混合列表对象
	 * @param hql
	 * @param args
	 * @param alias 别名对象 
	 * @return
	 */
	public Pager<T> find(String hql, Object[] args, Map<String, Object> alias) ;
	public Pager<T> find(String hql, Map<String, Object> alias) ;
	
	/**
	 * 根据HQL查询一组对象
	 * @param hql
	 * @param args
	 * @return
	 */
	public Object queryObject(String hql, Object[] args) ;
	public Object queryObject(String hql, Object arg) ;
	public Object queryObject(String hql) ;
	public Object queryObject(String hql, Object[] args, Map<String, Object> alias) ;
	public Object queryObject(String hql, Map<String, Object> alias) ;
	
	/**
	 * 根据HQL根据对象
	 * @param hql
	 * @param args
	 */
	public void updateByHql(String hql, Object[] args) ;
	public void updateByHql(String hql, Object arg) ;
	public void updateByHql(String hql) ;
	public void updateByHql(String hql, Object[] args, Map<String, Object> alias) ;
	public void updateByHql(String hql, Map<String, Object> alias) ;
	
	
	/**
	 * 根据SQL查询对象
	 * @param hql
	 * @param args
	 * @return 返回单组对象
	 */
	public Object queryObjectSQL(String sql, Object[] args, Class<?> clz, boolean hasEntity) ;
	public Object queryObjectSQL(String sql, Object arg, Class<?> clz, boolean hasEntity) ;
	public Object queryObjectSQL(String sql, Class<?> clz, boolean hasEntity) ;
	public Object queryObjectSQL(String sql, Object[] args, Map<String, Object> alias, Class<?> clz, boolean hasEntity) ;
	public Object queryObjectSQL(String sql, Map<String, Object> alias, Class<?> clz, boolean hasEntity) ;
	
	/**
	 * 根据SQL查询
	 * @param hql
	 * @param args
	 * @return 返回单组数据
	 */
	public Object[] queryObjectSQL(String sql, Object[] args) ;
	public Object[] queryObjectSQL(String sql, Object arg) ;
	public Object[] queryObjectSQL(String sql) ;
	public Object[] queryObjectSQL(String sql, Object[] args, Map<String, Object> alias) ;
	public Object[] queryObjectSQL(String sql, Map<String, Object> alias) ;
	
	/**
	 * 根据SQL查询对象，不包含关联对象（不分页）
	 * @param sql 查询的SQL语言
	 * @param args 查询条件
	 * @param clz 查询的实体对象
	 * @param hasEntity 该对象是否是一个Hibernate所管理的实体，如果不是需要使用setResultTransform查询
	 * @return 一组对象
	 */
	public <N extends Object>List<N> listSQL(String sql, Object[] args, Class<?> clz, boolean hasEntity) ;
	public <N extends Object>List<N> listSQL(String sql, Object arg, Class<?> clz, boolean hasEntity) ;
	public <N extends Object>List<N> listSQL(String sql, Class<?> clz, boolean hasEntity) ;
	public <N extends Object>List<N> listSQL(String sql, Object[] args, Map<String, Object> alias, Class<?> clz, boolean hasEntity) ;
	public <N extends Object>List<N> listSQL(String sql, Map<String, Object> alias, Class<?> clz, boolean hasEntity) ;
	
	/**
	 * 根据SQL查询，不分页，不关联对象（支持单列和多列）
	 * @param sql 查询的SQL语言
	 * @param args 查询条件
	 * @param alias 查询条件（别名查询）
	 * @return 一组数据
	 */
	public List<Object[]> listSQL(String sql, Object[] args, Map<String, Object> alias) ;
	public List<Object[]> listSQL(String sql, Map<String, Object> alias) ;
	public List<Object[]> listSQL(String sql, Object[] args) ;
	public List<Object[]> listSQL(String sql, Object arg) ;
	
	/**
	 * 根据SQL查询对象，不包含关联对象
	 * @param sql 查询的SQL语言
	 * @param args 查询条件
	 * @param clz 查询的实体对象
	 * @param hasEntity 该对象是否是一个Hibernate所管理的实体，如果不是需要使用setResultTransform查询
	 * @return 一组对象
	 */
	public <N extends Object>Pager<N> findSQL(String sql, Object[] args, Class<?> clz, boolean hasEntity) ;
	public <N extends Object>Pager<N> findSQL(String sql, Object arg, Class<?> clz, boolean hasEntity) ;
	public <N extends Object>Pager<N> findSQL(String sql, Class<?> clz, boolean hasEntity) ;
	public <N extends Object>Pager<N> findSQL(String sql, Object[] args, Map<String, Object> alias, Class<?> clz, boolean hasEntity) ;
	public <N extends Object>Pager<N> findSQL(String sql, Map<String, Object> alias, Class<?> clz, boolean hasEntity) ;
	
	
	/**
	 * 原生SQL执行
	 * @param hql
	 * @param args
	 * @return 返回执行的有效行
	 */
	public int executeSQL(String sql, Object[] args) ;
	public int executeSQL(String sql, Object arg) ;
	public int executeSQL(String sql) ;
	public int executeSQL(String sql, Object[] args, Map<String, Object> alias) ;
	public int executeSQL(String sql, Map<String, Object> alias) ;
	
	
	/**
	 * 统计记录数查询
	 * @param hql 查询的HQL
	 * @param args 查询参数
	 * @param alias 别名查询
	 * @return 返回Long类型
	 */
	public Long count(String hql, Object[] args, Map<String, Object> alias, boolean isHQL) ;
	public Long count(String hql, Object[] args, boolean isHQL) ;
	public Long count(String hql, Map<String, Object> alias, boolean isHQL) ;
	public Long count(String hql, boolean isHQL) ;
	
	
	/**
	 * 统计记录数查询
	 * @param hql 查询的SQL
	 * @param args 查询参数
	 * @param alias 别名查询
	 * @return 返回Long类型
	 */
	public BigInteger countSQL(String sql, Object[] args, Map<String, Object> alias, boolean isHQL) ;
	public BigInteger countSQL(String sql, Object[] args, boolean isHQL) ;
	public BigInteger countSQL(String sql, Map<String, Object> alias, boolean isHQL) ;
	public BigInteger countSQL(String sql, boolean isHQL) ;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
