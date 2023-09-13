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
 
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL; 
import java.util.Properties;
 
import org.apache.commons.lang3.StringUtils;


/**
 *   Author: 
 *     Eduardo Iglesias
 *   Contributors: 
 *   	Eduardo Iglesias - initial API and implementation
 **/
public class ServerStop {
	 
	public static org.eclipse.jetty.util.log.Logger jlogger = org.eclipse.jetty.util.log.Log.getRootLogger();
	   
	public static void main(String[] args) throws Exception
	{  
		shutdownJetty(args);
	}
	 
	public static  void shutdownJetty(String[] args) throws Exception
    {
		
		File currentFolder;
		if(args != null && args.length > 0) {
			currentFolder = new File(args[0]);
		}else {
			currentFolder = new File("");
		}
		
    	Properties prop = ServerUtil.readConfig(currentFolder);
    	String stopKey      = prop.getProperty(ServerUtil.C_STOP_KEY);
    	String hostname	= prop.getProperty(ServerUtil.C_PARAM_HOST_NAME);//hostname
		if(StringUtils.isEmpty(hostname))
			throw new Exception ("hostname information not found");
		String port  = prop.getProperty(ServerUtil.C_PARAM_PORT);//puerto
		
		if(StringUtils.isEmpty(port))
			port = ServerUtil.C_DEFAULT_PORT;
    	   
       try {
            URL url = new URL("http://localhost:" + port + "/shutdown?token=" + stopKey);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.getResponseCode();
            jlogger.info("Shutting down " + url + ": " + connection.getResponseMessage());
        } catch (SocketException e) {
        	jlogger.debug("Not running");
            // Okay - the server is not running
        } catch (IOException e) {
            throw new RuntimeException(e);
        } 
    }
  
}
