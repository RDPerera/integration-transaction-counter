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

import java.util.UUID;
import org.wso2.micro.integrator.usage.data.collector.TransactionCounterConstants;
import org.wso2.micro.integrator.usage.data.collector.exception.TransactionCounterConfigurationException;

public class TransactionCounterConfig {

    private static ConfigFetcher configFetcher;
    private static TransactionCounterConstants.ServerType serverType;

    public static void init() throws TransactionCounterConfigurationException {
        try {
            // Check whether the APIM Config class is available
            Class.forName(TransactionCounterConstants.APIM_CONFIG_CLASS);
            configFetcher = APIMConfigFetcher.getInstance();
            serverType = TransactionCounterConstants.ServerType.GATEWAY;
        } catch (ClassNotFoundException e) {
            try {
                // Check whether the MI Config class is available
                Class.forName(TransactionCounterConstants.MI_CONFIG_CLASS);
                configFetcher = MIConfigFetcher.getInstance();
                serverType = TransactionCounterConstants.ServerType.MI;
            } catch (ClassNotFoundException ex) {
                throw new TransactionCounterConfigurationException(ex);
            }
        }
    }

    public static TransactionCounterConstants.ServerType getServerType() {
        return serverType;
    }

    public static String getServerID() {
        if ( configFetcher.getConfigValue(TransactionCounterConstants.SERVER_ID) == null ) {
            return serverType.toString() + "_" + UUID.randomUUID().toString();
        }
        return configFetcher.getConfigValue(TransactionCounterConstants.SERVER_ID);
    }
}
