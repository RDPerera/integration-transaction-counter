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

package org.wso2.micro.integrator.usage.data.collector.publisher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.micro.integrator.usage.data.collector.record.TransactionReport;

/**
 * Default implementation of TransactionPublisher that logs the report.
 * This is a placeholder implementation that can be replaced with actual Java interface
 * integration when available.
 * 
 * To integrate with your Java interface:
 * 1. Create a new implementation of TransactionPublisher
 * 2. Add your Java interface invocation in the publish() method
 * 3. Configure the new implementation class in deployment.toml
 * 
 * @author - Dilan Perera
 * @version - 1.0.0
 */
public class TransactionPublisherImpl implements TransactionPublisher {
    
    private static final Log LOG = LogFactory.getLog(TransactionPublisherImpl.class);
    
    @Override
    public void init() {
        // Publisher initialized
    }
    
    @Override
    public boolean publish(TransactionReport report) {
        try {
            // TODO: Replace this with actual Java interface call when available

            LOG.info("Transaction Report - ID: " + report.getId() + 
                     ", Server: " + report.getServerId() + 
                     " (" + report.getServerType() + ")" +
                     ", Host: " + report.getHost() +
                     ", Count: " + report.getTotalCount() + 
                     ", Window: " + report.getFormattedStartTime() + " to " + report.getFormattedEndTime());
            return true;
            
        } catch (Exception e) {
            LOG.error("Error while publishing transaction report", e);
            return false;
        }
    }
    
    @Override
    public void cleanup() {
        // Cleanup completed
    }
}
