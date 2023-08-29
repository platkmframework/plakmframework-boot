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
package org.platkmframework.boot.server.runner;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; 
import org.apache.commons.lang3.StringUtils;
import org.platkmframework.boot.ioc.CustomIoDprocess;
import org.platkmframework.content.ioc.ObjectContainer;
import org.platkmframework.content.project.ProjectContent;
import org.platkmframework.jpa.persistence.PlatkmPersistenceFileParse;
import org.platkmframework.util.JsonUtil;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule; 


/**
 *   Author: 
 *     Eduardo Iglesias
 *   Contributors: 
 *   	Eduardo Iglesias - initial API and implementation
 **/
public class PlatkmFrameworkApplication {

	public static void main(String[] args) {
		
		try 
		{
			ProjectContent.instance().loadApplicationProperties();
			String packagesPrefix = ProjectContent.instance().getAppProperties().getProperty("org.platkmframework.configuration.custom.properties.file");
			if(StringUtils.isNotBlank(packagesPrefix)){
				ProjectContent.instance().loadApplicationProperties(packagesPrefix.split(","));
			}
			PlatkmPersistenceFileParse.parse();
			String javaClassPath  = System.getProperty("java.class.path");
			
			packagesPrefix = ProjectContent.instance().getAppProperties().getProperty("org.platkmframework.configuration.package.prefix");
			packagesPrefix+=",org.platkmframework";
			ObjectContainer.instance().process(javaClassPath, packagesPrefix.split(","), 
													ProjectContent.instance().getAppProperties(),
													new CustomIoDprocess());
			
			JavaTimeModule javaTimeModule = new JavaTimeModule();
			String format = ProjectContent.instance().getAppProperties().getProperty("platform.format.datetime");
			if(StringUtils.isNotBlank(format)) {
				javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(format)));
				javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(format)));
			}
			
			format	= ProjectContent.instance().getAppProperties().getProperty("platform.format.date");
			if(StringUtils.isNotBlank(format)) {
				javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(format)));
				javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(format)));
			}
			
			JsonUtil.setObjectMapper(JsonMapper.builder()  
					   .addModule(new ParameterNamesModule())
					   .addModule(new Jdk8Module())
					   .addModule(javaTimeModule)
					   .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) 
					   .build());
			
			
			
			
			StartServerProcessor.start(ProjectContent.instance().getAppProperties());
			
		} catch ( Exception e) { 
			e.printStackTrace();
			System.exit(-1);
		}

	}

}
