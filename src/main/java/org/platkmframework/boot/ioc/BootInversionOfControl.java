package org.platkmframework.boot.ioc;

import java.util.ArrayList;

import org.platkmframework.doi.IoDProcess;
import org.platkmframework.doi.SearchClasses;
import org.platkmframework.doi.data.ObjectReferece;

import jakarta.websocket.server.ServerEndpoint;

public class BootInversionOfControl extends SearchClasses implements IoDProcess {

	public static final String C_WEBSCOKET_ENDPOINT_CLASS = "WEBSCOKET_ENDPOINT_CLASS";
	
	@Override
	protected void processCustomClass(ObjectReferece objectReferece, Class<?> class1) {
		if(class1.isAnnotationPresent(ServerEndpoint.class)){
			if(!objectReferece.getCustomInfo().containsKey(C_WEBSCOKET_ENDPOINT_CLASS)){
				objectReferece.getCustomInfo().put(C_WEBSCOKET_ENDPOINT_CLASS, new ArrayList<String>());
			}
			((ArrayList<String>)objectReferece.getCustomInfo().get(C_WEBSCOKET_ENDPOINT_CLASS) ).add(class1.getName());
		}
	}
	
}
