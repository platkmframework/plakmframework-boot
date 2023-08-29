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
package org.platkmframework.boot.ioc;

import java.lang.reflect.Field;

import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.platkmframework.boot.jpa.entitymanager.PlatkmEntityManagerProxy;
import org.platkmframework.content.ioc.IoDProcess;
import org.platkmframework.content.ioc.SearchClasses;
import org.platkmframework.content.ioc.exception.IoDCException; 


/**
 *   Author: 
 *     Eduardo Iglesias
 *   Contributors: 
 *   	Eduardo Iglesias - initial API and implementation
 **/
public class CustomIoDprocess extends SearchClasses implements IoDProcess {
	 
	
	@Override
	protected void checkCustomIoD(Object ob, Field f) throws IoDCException {

		try {
			if(f.isAnnotationPresent(PersistenceContext.class)) { 
				PersistenceContext persistenceContext = f.getAnnotation(PersistenceContext.class);
				if(StringUtils.isBlank(persistenceContext.unitName())) {
					System.out.print("debe poner el nombre de la unidad en el fichero de configuraci�n");
					System.exit(-1);
				}  
				 
				boolean accessValue = f.canAccess(ob);
				f.setAccessible(true);
				f.set(ob, new PlatkmEntityManagerProxy(persistenceContext.unitName()));
				f.setAccessible(accessValue);
			}
			
		} catch (IllegalArgumentException | IllegalAccessException e) { 
			e.printStackTrace();
			throw new IoDCException(e.getMessage());
		} 
	}
}
