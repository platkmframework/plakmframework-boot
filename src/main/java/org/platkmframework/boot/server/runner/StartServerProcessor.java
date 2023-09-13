/*******************************************************************************
 * Copyright(c) 2023 the original author Eduardo Iglesias Taylor.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	 https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * 	Eduardo Iglesias Taylor - initial API and implementation
 *******************************************************************************/
package org.platkmframework.boot.server.runner;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Properties;
 

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ShutdownHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.platkmframework.annotation.CustomFilter;
import org.platkmframework.annotation.CustomServlet;
import org.platkmframework.boot.jpa.server.filter.DataBaseFilter;
import org.platkmframework.boot.server.filter.CORSFilter;
import org.platkmframework.content.ioc.ObjectContainer;
import org.platkmframework.core.request.CorePropertyConstant;
import org.platkmframework.util.DataTypeUtil;

import jakarta.servlet.Filter;
import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.http.HttpServlet;


/**
 *   Author: 
 *     Eduardo Iglesias
 *   Contributors: 
 *   	Eduardo Iglesias - initial API and implementation
 **/
public class StartServerProcessor {
	 
	/** 
	 * @param args
	 * @throws Exception 
	 */
	public static Server start(Properties  properties) throws Exception {

			  
		InetSocketAddress inetSocketAddress = new InetSocketAddress(properties.getProperty(CorePropertyConstant.ORG_PLATKMFRAMEWORK_SERVER_NAME), DataTypeUtil.getIntegerValue(properties.getProperty(CorePropertyConstant.ORG_PLATKMFRAMEWORK_SERVER_PORT),0)); 
		Server server = new Server(inetSocketAddress);
		server.setStopAtShutdown(true);

		if(DataTypeUtil.getBooleanValue(properties.getProperty(CorePropertyConstant.ORG_PLATKMFRAMEWORK_SERVER_ACTIVE_QUEUE_POOL), false)) {
			QueuedThreadPool threadPool = new QueuedThreadPool(DataTypeUtil.getIntegerValue(properties.getProperty(CorePropertyConstant.ORG_PLATKMFRAMEWORK_SERVER_MAXTHREADS),0), DataTypeUtil.getIntegerValue(properties.getProperty(CorePropertyConstant.ORG_PLATKMFRAMEWORK_SERVER_MINTHREADS),0), DataTypeUtil.getIntegerValue(properties.getProperty(CorePropertyConstant.ORG_PLATKMFRAMEWORK_SERVER_IDLETIMEOUT),0)); 
			server.addBean(threadPool);
		}
		 
        WebAppContext webapp = new WebAppContext();
        webapp.	setContextPath("/");  
        webapp.setResourceBase("");
        //webapp.setInitParameter(C_APPLICATION_ENVIRONMENT, StartConfig.getEnvironment());
        webapp.setDisplayName(org.platkmframework.core.request.servlet.RequestManagerServlet.class.getName());
        jakarta.servlet.ServletRegistration.Dynamic dynamic = webapp.getServletContext().addServlet(org.platkmframework.core.request.servlet.RequestManagerServlet.class.getName(),new org.platkmframework.core.request.servlet.RequestManagerServlet());
        String[] patterns = properties.getProperty(CorePropertyConstant.ORG_PLATKMFRAMEWORK_SERVER_PATTERNS).split(",");
        dynamic.addMapping(patterns);
        dynamic.setLoadOnStartup(1); 
        dynamic.setAsyncSupported(true);
    
        dynamic.setMultipartConfig(getMultipartConfig(properties));
          
        //filter
        Filter filter = new org.eclipse.jetty.servlets.DoSFilter();
        webapp.getServletHandler().addFilter(newFilterHolder(filter, true), newFilterMapping(filter, patterns));
        
        filter = new org.eclipse.jetty.servlets.QoSFilter();
        webapp.getServletHandler().addFilter(newFilterHolder(filter, true), newFilterMapping(filter, patterns));

        filter = new  CORSFilter();
        webapp.getServletHandler().addFilter(newFilterHolder(filter, true), newFilterMapping(filter, patterns));
        
        filter = new org.platkmframework.core.request.filter.ExceptionFilter();
        webapp.getServletHandler().addFilter(newFilterHolder(filter, true), newFilterMapping(filter, patterns));

        filter = new DataBaseFilter();
        webapp.getServletHandler().addFilter(newFilterHolder(filter, true), newFilterMapping(filter, patterns));

        filter = new org.platkmframework.core.security.filter.SecurityFilter();
        webapp.getServletHandler().addFilter(newFilterHolder(filter, true), newFilterMapping(filter, patterns));  
                
        List<Object> custonFilters = ObjectContainer.instance().getListObjectByAnnontation(CustomFilter.class);
        CustomFilter customFilter;
        for (Object object : custonFilters) {
        	 filter = (Filter) object;
        	 customFilter = filter.getClass().getAnnotation(CustomFilter.class); 
             webapp.getServletHandler().addFilter(newFilterHolder(filter, true), newFilterMapping(filter, customFilter.pattern()));
       }        
        
        
        addContentServlet(webapp, properties);
        
        webapp.setErrorHandler(new CustomErrorHandler());
          
        //webapp.setInitParameter(ServerUtil.C_PARAM_SECRET_KEY, secretKey);
        //(POST)/shutdown?token=
        if(StringUtils.isNotBlank(properties.getProperty(CorePropertyConstant.ORG_PLATKMFRAMEWORK_SERVER_STOPKEY))) {
        	Handler[] handlers = {webapp, new ShutdownHandler(properties.getProperty(CorePropertyConstant.ORG_PLATKMFRAMEWORK_SERVER_STOPKEY))};
        	HandlerCollection handlerCollection = new HandlerCollection();
        	handlerCollection.setHandlers(handlers); 
        	server.setHandler(handlerCollection);  
        }
          	
	        
        try{
        	
        	server.start();
			//server.join(); 
			 
		} catch (Exception e) 
        {
			server = null;
			e.printStackTrace();
			System.out.print("Process error -> " + e.getMessage());
			System.exit(-1);
		}	  
        
        return server;
	}

	private static void addContentServlet(WebAppContext webapp, Properties properties) { 
		
		List<Object> list = ObjectContainer.instance().getListObjectByAnnontation(CustomServlet.class);
		CustomServlet customServlet;
		HttpServlet httpServlet;
		for (Object object : list) {
			customServlet = object.getClass().getAnnotation(CustomServlet.class);
			httpServlet = (HttpServlet)object;
			jakarta.servlet.ServletRegistration.Dynamic dynamic = webapp.getServletContext().addServlet(httpServlet.getClass().getName(), (HttpServlet)object);
			dynamic.addMapping(customServlet.mappings());
			dynamic.setLoadOnStartup(1);   
		}
	}

	private static MultipartConfigElement getMultipartConfig(Properties properties) {
		return new MultipartConfigElement(
	            "/tmp",
	            5242880,
	            20971520,
	            0
	        );
	}

	private static FilterHolder newFilterHolder(Filter filter, boolean asynSupport) {  
        FilterHolder filterHolder = new FilterHolder(filter);
        filterHolder.setName(filter.getClass().getName());
        filterHolder.setAsyncSupported(asynSupport);
		return filterHolder;
	}

	private static FilterMapping newFilterMapping(Filter filter, String[] patterns) {
	     FilterMapping filterMapping = new FilterMapping();
		filterMapping.setFilterName(filter.getClass().getName());
		filterMapping.setPathSpec(String.join(",", patterns));
		return filterMapping;
	}
 
 


}
