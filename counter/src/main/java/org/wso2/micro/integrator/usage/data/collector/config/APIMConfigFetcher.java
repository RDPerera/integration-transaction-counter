/*
 *  Copyright (c) 2025, WSO2 LLC. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 LLC. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.micro.integrator.usage.data.collector.config;

import org.wso2.micro.integrator.usage.data.collector.TransactionCounterConstants;
import org.wso2.micro.integrator.usage.data.collector.exception.TransactionCounterConfigurationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class APIMConfigFetcher implements ConfigFetcher {

    private static APIMConfigFetcher instance = null;
    private final static HashMap<String, Object> configMap = new HashMap<>();

    private APIMConfigFetcher() throws TransactionCounterConfigurationException {
        try {
            Class<?> configClass = Class.forName(TransactionCounterConstants.APIM_CONFIG_CLASS);

            Object serviceReferenceHolder = configClass.getMethod("getInstance").invoke(null);
            Object apiManagerConfigurationService = configClass.getMethod("getAPIManagerConfigurationService")
                    .invoke(serviceReferenceHolder);
            Object apiManagerConfiguration = apiManagerConfigurationService.getClass()
                    .getMethod("getAPIManagerConfiguration").invoke(apiManagerConfigurationService);
            Method getFirstProperty = apiManagerConfiguration.getClass().getMethod("getFirstProperty",
                    String.class);

            // Reading the config values
            String  temp;

            temp = (String) getFirstProperty.invoke(apiManagerConfiguration,
                    TransactionCounterConstants.GATEWAY_SERVER_ID);
            String SERVER_ID = temp;

            configMap.put(TransactionCounterConstants.SERVER_ID, SERVER_ID);

        } catch (ClassNotFoundException e) {
            // This error won't be thrown here because it is already checked in TransactionCountConfig
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new TransactionCounterConfigurationException();
        } catch (NumberFormatException | NullPointerException e) {
            throw new TransactionCounterConfigurationException("Error while reading the config values", e);
        }
    }

    public static ConfigFetcher getInstance() throws TransactionCounterConfigurationException {
        if (instance == null) {
            instance = new APIMConfigFetcher();
        }
        return instance;
    }

    @Override
    public String getConfigValue(String key) {
        if (configMap.get(key) == null) {
            return null;
        }
        return configMap.get(key).toString();
    }

}
