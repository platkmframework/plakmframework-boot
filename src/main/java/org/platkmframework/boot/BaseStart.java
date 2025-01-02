/**
 * ****************************************************************************
 *  Copyright(c) 2023 the original author Eduardo Iglesias Taylor.
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

import org.platkmframework.annotation.HttpRest;
import org.platkmframework.boot.ioc.BootInversionOfControl;
import org.platkmframework.content.ObjectContainer;
import org.platkmframework.content.init.BootInitializer;
import org.platkmframework.content.json.JsonUtil;
import org.platkmframework.content.project.ContentPropertiesConstant;
import org.platkmframework.content.project.ProjectContent;
import org.platkmframework.core.rmi.RMIException;
import org.platkmframework.core.rmi.RMIServerManager;
import org.platkmframework.core.scheduler.SchedulerManager;
import org.platkmframework.doi.data.ObjectReferece;
import org.platkmframework.doi.exception.IoDCException;
import org.platkmframework.httpclient.proxy.HttpRestProxyProcessor;
import org.platkmframework.proxy.ProxyProcessorFactory;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
public class BaseStart {

    /**
     * start
     * @throws Exception Exception
     */
    public void start() throws Exception {
        applyIoD(new BootInversionOfControl());
        initJson();
        initProxyProcessorFactory();
        initBootInitializer();
    }

    /**
     * applyIoD
     * @param bootInversionOfControl bootInversionOfControl
     * @throws IoDCException IoDCException
     */
    protected void applyIoD(BootInversionOfControl bootInversionOfControl) throws IoDCException {
        String javaClassPath = System.getProperty("java.class.path");
        String packagesPrefix = ProjectContent.instance().getAppProperties().getProperty(ContentPropertiesConstant.ORG_PLATKMFRAMEWORK_CONFIGURATION_PACKAGE_PREFIX);
        packagesPrefix += ",org.platkmframework";
        //SearchClasses searchClasses = new SearchClasses();
        ObjectReferece objectReferece = new ObjectReferece();
        objectReferece.setProp(ProjectContent.instance().getAppProperties());
        bootInversionOfControl.process(javaClassPath, packagesPrefix.split(","), objectReferece);
        ObjectContainer.instance().setReference(objectReferece);
    }

    /**
     * initJson
     */
    protected void initJson() {
        JsonUtil.init();
    }

    /**
     * initProxyProcessorFactory
     * @throws RMIException RMIException
     */
    protected void initProxyProcessorFactory() throws RMIException {
        ProxyProcessorFactory.instance().register(HttpRest.class.getName(), new HttpRestProxyProcessor());
        RMIServerManager.instance().runAllOnStart();
        SchedulerManager.instance().runAllOnStart();
    }

    /**
     * initBootInitializer
     */
    protected void initBootInitializer() {
        for (BootInitializer initializer : ProjectContent.instance().getInitializer()) {
            initializer.process();
        }
    }

    /**
     * BaseStart
     */
    public BaseStart() {
        super();
    }
}
