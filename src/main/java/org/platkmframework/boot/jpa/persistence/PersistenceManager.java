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
package org.platkmframework.boot.jpa.persistence;

import java.util.HashMap; 
import java.util.Map;

import javax.persistence.Persistence;

import org.platkmframework.jpa.entitymanager.PlatkmEntityManager;
import org.platkmframework.jpa.exception.PlatkmJpaException;
import org.platkmframework.jpa.factory.PlakmEntityManagerFactory;
import org.platkmframework.jpa.persistence.PersistenceUnit;
import org.platkmframework.jpa.persistence.PlatkmPersistenceFileParse;


/**
 *   Author: 
 *     Eduardo Iglesias
 *   Contributors: 
 *   	Eduardo Iglesias - initial API and implementation
 **/
public class PersistenceManager {
	
	private static PersistenceManager persistenceManager;
	
	ThreadLocal<Map<String, PlatkmEntityManager>> threadLocal;   
	 
	private PersistenceManager() {
		super();
		threadLocal = new ThreadLocal<>(); 
	}

	public static PersistenceManager instance()
	{
		if(persistenceManager == null)
			persistenceManager = new PersistenceManager();
		
		return persistenceManager;
	}

	public synchronized void begin() {
		close(); 
		
		for (PersistenceUnit persistenceUnit : PlatkmPersistenceFileParse.getPersistenceUnits()) {
			creantePlakmEnity(persistenceUnit); 
		}
	}
	
	public synchronized void begin(String persistenceUnitName) {
		close(); 
		
		PersistenceUnit persistenceUnit = PlatkmPersistenceFileParse.getPersistenceUnit(persistenceUnitName);
		creantePlakmEnity(persistenceUnit);  
	}
	
	private void creantePlakmEnity(PersistenceUnit persistenceUnit ) {
		PlakmEntityManagerFactory plakmEntityManagerFactory = (PlakmEntityManagerFactory) Persistence.createEntityManagerFactory(persistenceUnit.getName());
		Persistence.generateSchema(persistenceUnit.getName(), null);
			
		PlatkmEntityManager platkmEntityManager = plakmEntityManagerFactory.createEntityManager();
		platkmEntityManager.getTransaction().begin();
		put(persistenceUnit.getName(), platkmEntityManager);
	}

	public void commit() { 
		threadLocal.get().forEach((k, v) -> v.getTransaction().commit());  
	}

	public void rollback() {
		if(threadLocal.get()!=null)
			threadLocal.get().forEach((k, v) -> v.getTransaction().rollback());
	}
	
	
	public synchronized void  close() {
		if(threadLocal.get() != null)
			threadLocal.get().forEach((k, v) -> {
				
				try {
					
					if(v.getTransaction().isActive()){
						v.getTransaction().commit(); 
					}  
					
				} catch (Exception e) {
					v.getTransaction().rollback();
					new PlatkmJpaException(e);
				} finally {
					threadLocal.remove();
					v.close();
					try {
						((PlakmEntityManagerFactory) Persistence.createEntityManagerFactory(k)).returnObject(v);
					} catch (Exception e) {
						new PlatkmJpaException(e);
					}finally {
						threadLocal.remove();
					}
				} 
			}); 
			
					
	}

	private void put(String name, PlatkmEntityManager platkmEntityManager) {
		if(threadLocal.get() == null) {
			threadLocal.set(new HashMap<>());
		}
		threadLocal.get().put(name, platkmEntityManager); 
	} 
	
	public PlatkmEntityManager get(String name) {
		return threadLocal.get().get(name);
	}


}
