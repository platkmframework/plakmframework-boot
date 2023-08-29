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
package org.platkmframework.boot.jpa.server.listener;
   

import org.platkmframework.jpa.persistence.PlatkmPersistenceFileParse;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
 



/**
 *   Author: 
 *     Eduardo Iglesias
 *   Contributors: 
 *   	Eduardo Iglesias - initial API and implementation
 **/
public class PersistenceListener implements ServletContextListener
{  

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent)  
	{
		try 
		{    
			PlatkmPersistenceFileParse.parse();
			
		} catch (Exception e) 
		{ 
			e.printStackTrace();
			System.exit(-1);
		}  
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {}

}
