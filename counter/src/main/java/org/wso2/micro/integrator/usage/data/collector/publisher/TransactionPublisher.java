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

import org.wso2.micro.integrator.usage.data.collector.record.TransactionReport;

/**
 * Interface for publishing hourly transaction reports.
 * Implementations can publish to various destinations (logs, Java interfaces, APIs, etc.).
 * 
 * @author - Dilan Perera
 * @version - 1.0.0
 */
public interface TransactionPublisher {
    
    /**
     * Initialize the publisher with necessary configuration.
     */
    void init();
    
    /**
     * Publish the hourly transaction report.
     * 
     * @param report The hourly transaction report to publish
     * @return true if publishing was successful, false otherwise
     */
    boolean publish(TransactionReport report);
    
    /**
     * Cleanup resources used by the publisher.
     */
    void cleanup();
}
