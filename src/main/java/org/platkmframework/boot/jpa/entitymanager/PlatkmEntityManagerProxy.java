/*******************************************************************************
 *   Copyright(c) 2023 the original author or authors.
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *  
 *        https://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *******************************************************************************/
package org.platkmframework.boot.jpa.entitymanager;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityGraph; 
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;

import org.platkmframework.boot.jpa.persistence.PersistenceManager;
import org.platkmframework.common.domain.filter.criteria.DeleteCriteria;
import org.platkmframework.common.domain.filter.criteria.FilterCriteria;
import org.platkmframework.database.manager.exception.DatabaseValidationException;
import org.platkmframework.database.manager.session.base.ColumnInfoValue;
import org.platkmframework.databasereader.core.IDatabaseReader;
import org.platkmframework.databasereader.model.Table;
import org.platkmframework.jpa.database.mapping.DatabaseMapper;
import org.platkmframework.jpa.entitymanager.PlatkmDb;
import org.platkmframework.jpa.entitymanager.PlatkmEntityManager;
import org.platkmframework.jpa.entitymanager.PlatkmQuery;
import org.platkmframework.jpa.entitymanager.QueryDao;
import org.platkmframework.jpa.entitymanager.QueryManagerDao;
import org.platkmframework.jpa.querymanager.QueryManager;
import org.platkmframework.util.error.InvocationException;
 


/**
 *   Author: 
 *     Eduardo Iglesias
 *   Contributors: 
 *   	Eduardo Iglesias - initial API and implementation
 **/
public class PlatkmEntityManagerProxy implements PlatkmEntityManager {
    
	private String persistenceUnitName;
	
	public PlatkmEntityManagerProxy(String persistenceUnitName) {
		this.persistenceUnitName = persistenceUnitName;
	}
	
 
	@Override
	public void persist(Object entity) {
		
		PersistenceManager.instance().get(persistenceUnitName).persist(entity);
		
	}

	@Override
	public <T> T merge(T entity) { 
		return PersistenceManager.instance().get(persistenceUnitName).merge(entity);
	}

	@Override
	public void remove(Object entity) {
		PersistenceManager.instance().get(persistenceUnitName).remove(entity);
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey) { 
		return PersistenceManager.instance().get(persistenceUnitName).find(entityClass,primaryKey);
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) { 
		return PersistenceManager.instance().get(persistenceUnitName).find(entityClass, primaryKey);
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
		return PersistenceManager.instance().get(persistenceUnitName).find(entityClass, primaryKey);
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
		return PersistenceManager.instance().get(persistenceUnitName).find(entityClass, primaryKey);
	}

	@Override
	public <T> T getReference(Class<T> entityClass, Object primaryKey) {
		return PersistenceManager.instance().get(persistenceUnitName).getReference(entityClass, primaryKey);
	}

	@Override
	public void flush() {
		PersistenceManager.instance().get(persistenceUnitName).flush(); 
	}

	@Override
	public void setFlushMode(FlushModeType flushMode) {
		PersistenceManager.instance().get(persistenceUnitName).setFlushMode(flushMode);
	}

	@Override
	public FlushModeType getFlushMode() { 
		return PersistenceManager.instance().get(persistenceUnitName).getFlushMode();
	}

	@Override
	public void lock(Object entity, LockModeType lockMode) {
		PersistenceManager.instance().get(persistenceUnitName).lock(entity, lockMode);
	}

	@Override
	public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
		PersistenceManager.instance().get(persistenceUnitName).lock(entity, lockMode); 
	}

	@Override
	public void refresh(Object entity) {
		PersistenceManager.instance().get(persistenceUnitName).refresh(entity);
	}

	@Override
	public void refresh(Object entity, Map<String, Object> properties) {
		PersistenceManager.instance().get(persistenceUnitName).refresh(entity, properties);
	}

	@Override
	public void refresh(Object entity, LockModeType lockMode) {
		PersistenceManager.instance().get(persistenceUnitName).refresh(entity, lockMode);
	}

	@Override
	public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
		PersistenceManager.instance().get(persistenceUnitName).refresh(entity, lockMode, properties); 
	}

	@Override
	public void clear() {
		PersistenceManager.instance().get(persistenceUnitName).clear();
	}

	@Override
	public void detach(Object entity) {
		PersistenceManager.instance().get(persistenceUnitName).detach(entity);
	}

	@Override
	public boolean contains(Object entity) {
		return PersistenceManager.instance().get(persistenceUnitName).contains(entity);
	}

	@Override
	public LockModeType getLockMode(Object entity) {
		return PersistenceManager.instance().get(persistenceUnitName).getLockMode(entity);
	}

	@Override
	public void setProperty(String propertyName, Object value) {
		PersistenceManager.instance().get(persistenceUnitName).setProperty(propertyName, value);
	}

	@Override
	public Map<String, Object> getProperties() { 
		return PersistenceManager.instance().get(persistenceUnitName).getProperties();
	}

	@Override
	public Query createQuery(String sql) {
		return PersistenceManager.instance().get(persistenceUnitName).createQuery(sql);
	}

	@Override
	public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
		return PersistenceManager.instance().get(persistenceUnitName).createQuery(criteriaQuery);
	}

	@Override
	public Query createQuery(CriteriaUpdate updateQuery) {
		return PersistenceManager.instance().get(persistenceUnitName).createQuery(updateQuery);
	}

	@Override
	public Query createQuery(CriteriaDelete deleteQuery) {
		return PersistenceManager.instance().get(persistenceUnitName).createQuery(deleteQuery);
	}
	
	
	@Override
	public PlatkmQuery createQuery(FilterCriteria filterCriteria, List<Object> params, Class returnClass) {
		return PersistenceManager.instance().get(persistenceUnitName).createQuery(filterCriteria, params,returnClass);
	}

	@Override
	public Query createQuery(DeleteCriteria deleteCriteria, List<Object> params) {
		 
		return PersistenceManager.instance().get(persistenceUnitName).createQuery(deleteCriteria, params);	
	}

	@Override
	public <T> TypedQuery<T> createQuery(String sql, Class<T> resultClass) {
		return PersistenceManager.instance().get(persistenceUnitName).createQuery(sql, resultClass); 
	}

	@Override
	public Query createNamedQuery(String name) {
		return PersistenceManager.instance().get(persistenceUnitName).createNamedQuery(name);	 
	}

	@Override
	public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
		return PersistenceManager.instance().get(persistenceUnitName).createNamedQuery(name, resultClass); 
	}

	@Override
	public Query createNativeQuery(String sql) { 
		return PersistenceManager.instance().get(persistenceUnitName).createNativeQuery(sql);	 
	}

	@Override
	public Query createNativeQuery(String sql, Class resultClass) {
		return PersistenceManager.instance().get(persistenceUnitName).createNativeQuery(sql, resultClass);
	}

	@Override
	public Query createNativeQuery(String sql, String resultSetMapping) {
		return PersistenceManager.instance().get(persistenceUnitName).createNativeQuery(sql);
	}

	@Override
	public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
		return PersistenceManager.instance().get(persistenceUnitName).createNamedStoredProcedureQuery(name);
	}

	@Override
	public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
		return PersistenceManager.instance().get(persistenceUnitName).createStoredProcedureQuery(procedureName);
	}

	@Override
	public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
		return PersistenceManager.instance().get(persistenceUnitName).createStoredProcedureQuery(procedureName, resultClasses);
	}

	@Override
	public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
		return PersistenceManager.instance().get(persistenceUnitName).createStoredProcedureQuery(procedureName, resultSetMappings);
	}

	@Override
	public void joinTransaction() {
		PersistenceManager.instance().get(persistenceUnitName).joinTransaction();
	}

	@Override
	public boolean isJoinedToTransaction() {
		return PersistenceManager.instance().get(persistenceUnitName).isJoinedToTransaction();
	}

	@Override
	public <T> T unwrap(Class<T> cls) {
		return PersistenceManager.instance().get(persistenceUnitName).unwrap(cls);
	}

	@Override
	public Object getDelegate() {
		return PersistenceManager.instance().get(persistenceUnitName).getDelegate();
	}

	@Override
	public void close() {
		PersistenceManager.instance().get(persistenceUnitName).close();
		
	}

	@Override
	public boolean isOpen() { 
		return PersistenceManager.instance().get(persistenceUnitName).isOpen();
	}

	@Override
	public EntityTransaction getTransaction() { 
		return PersistenceManager.instance().get(persistenceUnitName).getTransaction();
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		return PersistenceManager.instance().get(persistenceUnitName).getEntityManagerFactory();
	}

	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		return PersistenceManager.instance().get(persistenceUnitName).getCriteriaBuilder();
	}

	@Override
	public Metamodel getMetamodel() {
		return PersistenceManager.instance().get(persistenceUnitName).getMetamodel();
	}

	@Override
	public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
		return PersistenceManager.instance().get(persistenceUnitName).createEntityGraph(rootType);
	}

	@Override
	public EntityGraph<?> createEntityGraph(String graphName) {
		return PersistenceManager.instance().get(persistenceUnitName).createEntityGraph(graphName);
	}

	@Override
	public EntityGraph<?> getEntityGraph(String graphName) {
		return PersistenceManager.instance().get(persistenceUnitName).getEntityGraph(graphName);
	}

	@Override
	public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
		return PersistenceManager.instance().get(persistenceUnitName).getEntityGraphs(entityClass);
	}
 
	public QueryDao getQueryDao() {
		return PersistenceManager.instance().get(persistenceUnitName).getQueryDao();
	}

	public void restart() {
		 
		PersistenceManager.instance().get(persistenceUnitName).setFlushMode(null);
	}

	@Override
	public QueryManagerDao getQueryManagerDao() { 
		return PersistenceManager.instance().get(persistenceUnitName).getQueryManagerDao();
	}


	@Override
	public QueryManager getQueryManager() { 
		return PersistenceManager.instance().get(persistenceUnitName).getQueryManager();
	}


	@Override
	public DatabaseMapper getDatabaseMapper() {
		return PersistenceManager.instance().get(persistenceUnitName).getDatabaseMapper();
	}


	@Override
	public Long insertSQL(String tableName, List<ColumnInfoValue> columns)
			throws SQLException, DatabaseValidationException { 
		return PersistenceManager.instance().get(persistenceUnitName).insertSQL(tableName, columns);
	}


	@Override
	public void updateSQL(String tablename, List<ColumnInfoValue> columns)
			throws SQLException, InvocationException, ParseException, DatabaseValidationException {
		PersistenceManager.instance().get(persistenceUnitName).updateSQL(tablename, columns);
		
	}

	@Override
	public List<Table> getMetadata() { 
		return PersistenceManager.instance().get(persistenceUnitName).getMetadata();
	} 
}
