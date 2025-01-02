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
package org.platkmframework.boot.ioc;

import java.util.ArrayList;
import org.platkmframework.doi.IoDProcess;
import org.platkmframework.doi.SearchClasses;
import org.platkmframework.doi.data.ObjectReferece;
import jakarta.websocket.server.ServerEndpoint;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
public class BootInversionOfControl extends SearchClasses implements IoDProcess {

    /**
     * BootInversionOfControl
     */
    public static final String C_WEBSCOKET_ENDPOINT_CLASS = "WEBSCOKET_ENDPOINT_CLASS";

    /**
     * processCustomClass
     * @param objectReferece objectReferece
     * @param class1 class1
     */
    @Override
    protected void processCustomClass(ObjectReferece objectReferece, Class<?> class1) {
        if (class1.isAnnotationPresent(ServerEndpoint.class)) {
            if (!objectReferece.getCustomInfo().containsKey(C_WEBSCOKET_ENDPOINT_CLASS)) {
                objectReferece.getCustomInfo().put(C_WEBSCOKET_ENDPOINT_CLASS, new ArrayList<String>());
            }
            ((ArrayList<String>) objectReferece.getCustomInfo().get(C_WEBSCOKET_ENDPOINT_CLASS)).add(class1.getName());
        }
    }

    /**
     * BootInversionOfControl
     */
    public BootInversionOfControl() {
        super();
    }
}
