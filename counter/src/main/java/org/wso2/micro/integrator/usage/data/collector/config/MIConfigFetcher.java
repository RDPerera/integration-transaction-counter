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
import java.util.HashMap;

public class MIConfigFetcher implements ConfigFetcher {

    private static MIConfigFetcher instance = null;
    private final static HashMap<String, Object> configMap = new HashMap<>();

    private MIConfigFetcher() throws TransactionCounterConfigurationException {
        try {
            Class<?> configClass = Class.forName(TransactionCounterConstants.MI_CONFIG_CLASS);

            @SuppressWarnings("unchecked")
            HashMap<String, Object> configs = (HashMap<String, Object>) configClass
                    .getMethod("getParsedConfigs").invoke(null);

            // Reading the config values
            String temp;

            temp = (String) configs.get(TransactionCounterConstants.MI_SERVER_ID);
            String SERVER_ID = temp;

            configMap.put(TransactionCounterConstants.SERVER_ID, SERVER_ID);

        } catch (ClassNotFoundException e) {
            // This error won't be thrown here because it is already checked in TransactionCountConfig
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new TransactionCounterConfigurationException(e);
        } catch (ClassCastException e) {
            throw new TransactionCounterConfigurationException("Error while parsing the config", e);
        }
    }

    public static MIConfigFetcher getInstance() throws TransactionCounterConfigurationException {
        if(instance == null) {
            instance = new MIConfigFetcher();
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
