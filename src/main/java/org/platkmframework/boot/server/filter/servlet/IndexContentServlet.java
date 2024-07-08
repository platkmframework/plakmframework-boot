/*******************************************************************************
 * Copyright(c) 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors: 
 * 	Eduardo Iglesias - initial API and implementation
 *******************************************************************************/
package org.platkmframework.boot.server.filter.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.platkmframework.comon.service.exception.CustomServletException;
import org.platkmframework.content.project.CorePropertyConstant;
import org.platkmframework.content.project.ProjectContent;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse; 

/*
System.out.println(req.getContextPath());
System.out.println(req.getAuthType());
System.out.println(req.getContentType());
System.out.println(req.getMethod());
System.out.println(req.getPathInfo());
System.out.println(req.getRequestURI());
System.out.println(req.getRequestURL());
*/  
public class IndexContentServlet extends HttpServlet{

	private static Logger logger = LoggerFactory.getLogger(IndexContentServlet.class);
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected final void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req,resp);
	}
	
	@Override
	protected final void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req,resp);
	}
	
	
	@Override
	protected final void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { 
		process(req,resp);
	}

	protected void process(HttpServletRequest request, HttpServletResponse resp) throws CustomServletException 
	{ 
		logger.info("Default page called");
		try {
			resp.setCharacterEncoding("ISO-8859-1");
			resp.setContentType("html");  
			resp.setStatus(200);
			PrintWriter out = resp.getWriter();
			out.println("<html><body>"
					+ "Project :" + ProjectContent.instance().getProjectName()
					+ ", port: " + ProjectContent.instance().getProperty(CorePropertyConstant.ORG_PLATKMFRAMEWORK_SERVER_PORT));
			out.flush();
		} catch (Exception e) {
			throw new CustomServletException(200, "");
		}
	}
}
