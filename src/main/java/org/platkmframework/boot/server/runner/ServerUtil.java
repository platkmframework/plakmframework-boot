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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;


/**
 *   Author: 
 *     Eduardo Iglesias
 *   Contributors: 
 *   	Eduardo Iglesias - initial API and implementation
 **/
public class ServerUtil {

	public static  final String C_CONFIG_FILE    	  = "config.file";
	
	public static final String C_PARAM_HOST_NAME  	  = "hostname"; 
	public static  final String C_STOP_KEY            =  "stopKey"; 
	public static final String C_PARAM_PORT  		  = "port";  
	public static final String C_DEFAULT_PORT 	 	  = "80";
	
	public static final String C_ENCODE 			  = "UTF-8";
	 
	public static Properties readConfig(File currentFolder){
		Properties prop = new Properties(); 
		//read  file from default path 
		File configFile = new File(currentFolder.getAbsolutePath() + File.separator + C_CONFIG_FILE);
		if(configFile.exists() && configFile.isFile())
		{ 
			try { 
				List<?> list = FileUtils.readLines(configFile, "UTF-8");
				if(list!=null)
					for (int i = 0; i < list.size(); i++) 
					{
						String line       = (String)list.get(i);
						String keyValue[] = line.split("=");
						prop.put(keyValue[0], keyValue.length>1?keyValue[1]:"");
					} 
			} catch (IOException e) 
			{ 
				e.printStackTrace();
			}	
		}
		 
		return prop;
	}
	
}
