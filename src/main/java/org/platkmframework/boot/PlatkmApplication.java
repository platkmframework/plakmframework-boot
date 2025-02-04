/**
 * ****************************************************************************
 *  Copyright(c) 2025 the original author Eduardo Iglesias Taylor.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  	 https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  Contributors:
 *  	Eduardo Iglesias Taylor - initial API and implementation
 * *****************************************************************************
 */
package org.platkmframework.boot;

import org.platkmframework.boot.base.ioc.start.BaseStart;
import org.platkmframework.boot.base.server.runner.StartServerProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
public class PlatkmApplication {

	
	private static Logger logger = LoggerFactory.getLogger(PlatkmApplication.class);
	
    /**
     * start
     * @param args args
     */
    public static void start(Class appClass, String[] args) {
        try { 
        	BaseStart baseStart = new BaseStart();
        	baseStart.start(appClass, args);
            StartServerProcessor startServerProcessor = new StartServerProcessor();
            startServerProcessor.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * PlatkmFrameworkApplication
     */
    private PlatkmApplication() {
    	logger.error("This class should not be an instance");
        System.exit(-1);
    }
}
