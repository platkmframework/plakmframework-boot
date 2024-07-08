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

public class BaseStart{
	
	public void start() throws Exception {
		applyIoD(new BootInversionOfControl());
		initJson();
		initProxyProcessorFactory();
		initBootInitializer();
	}

	protected void applyIoD(BootInversionOfControl bootInversionOfControl) throws IoDCException {
		
		String javaClassPath  = System.getProperty("java.class.path"); 
		String packagesPrefix = ProjectContent.instance().getAppProperties().getProperty(ContentPropertiesConstant.ORG_PLATKMFRAMEWORK_CONFIGURATION_PACKAGE_PREFIX);
		packagesPrefix+=",org.platkmframework";
		
		//SearchClasses searchClasses = new SearchClasses();
		ObjectReferece objectReferece = new ObjectReferece();
		objectReferece.setProp(ProjectContent.instance().getAppProperties());
		bootInversionOfControl.process(javaClassPath, packagesPrefix.split(","), objectReferece);
		ObjectContainer.instance().setReference(objectReferece);
	}
	
	protected void initJson() {
		JsonUtil.init();
	}

	protected void initProxyProcessorFactory() throws RMIException {
		
		ProxyProcessorFactory.instance().register(HttpRest.class.getName(), new HttpRestProxyProcessor());
		RMIServerManager.instance().runAllOnStart();
		SchedulerManager.instance().runAllOnStart();
				
	}
	
	protected void initBootInitializer() {
		for (BootInitializer initializer : ProjectContent.instance().getInitializer()) {
			initializer.process();
		}
	}
	 
}
