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

package org.wso2.micro.integrator.usage.data.collector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.AbstractExtendedSynapseHandler;
import org.apache.synapse.MessageContext;
import org.wso2.micro.integrator.usage.data.collector.aggregator.TransactionAggregator;
import org.wso2.micro.integrator.usage.data.collector.exception.TransactionCounterConfigurationException;
import org.wso2.micro.integrator.usage.data.collector.publisher.TransactionPublisher;
import org.wso2.micro.integrator.usage.data.collector.publisher.TransactionPublisherImpl;
import org.wso2.micro.integrator.usage.data.collector.record.TransactionReport;
import org.wso2.micro.integrator.usage.data.collector.config.TransactionCounterConfig;

/**
 * Transaction counter handler that tracks and aggregates transaction counts hourly.
 * Simplified version that uses only TransactionAggregator with no queue/producer/consumer overhead.
 * 
 * @author - Isuru Wijesiri, Dilan Perera
 * @version - 2.0.0
 */
public class TransactionCountHandler extends AbstractExtendedSynapseHandler {
    private static final Log LOG = LogFactory.getLog(TransactionCountHandler.class);
    private TransactionAggregator transactionAggregator;
    private static boolean enabled = false;

    public TransactionCountHandler() {
        // Initialize the config mechanism
        try {
            TransactionCounterConfig.init();
        } catch (TransactionCounterConfigurationException e) {
            LOG.error("Error while initializing Transaction Counter. Transaction counter will be disabled", e);
            return;
        }

        // Initialize hourly transaction report
        TransactionReport.init(
                TransactionCounterConfig.getServerID(),
                TransactionCounterConfig.getServerType().toString()
        );

        // Initialize hourly aggregator - always enabled by default
        try {
            TransactionPublisher publisher = new TransactionPublisherImpl();
            publisher.init();
            
            this.transactionAggregator = TransactionAggregator.getInstance();
            this.transactionAggregator.init(publisher);
        } catch (Exception e) {
            LOG.error("Error while initializing Transaction Aggregator. " +
                     "Hourly aggregation will be disabled", e);
            return;
        }

        enabled = true;
    }

    @Override
    public boolean handleRequestInFlow(MessageContext messageContext) {
        if(!enabled) {
            return true;
        }
        int tCount = TransactionCountingLogic.handleRequestInFlow(messageContext);
        if(tCount > 0) {
            LOG.info("New transaction detected in RequestInFlow - Count: " + tCount);
            if(this.transactionAggregator != null && this.transactionAggregator.isEnabled()) {
                this.transactionAggregator.addTransactions(tCount);
            }
        }
        return true;
    }

    @Override
    public boolean handleRequestOutFlow(MessageContext messageContext) {
        if(!enabled) {
            return true;
        }
        int tCount = TransactionCountingLogic.handleRequestOutFlow(messageContext);
        if(tCount > 0) {
            LOG.info("New transaction detected in RequestOutFlow - Count: " + tCount);
            if(this.transactionAggregator != null && this.transactionAggregator.isEnabled()) {
                this.transactionAggregator.addTransactions(tCount);
            }
        }
        return true;
    }

    @Override
    public boolean handleResponseInFlow(MessageContext messageContext) {
        if(!enabled) {
            return true;
        }
        int tCount = TransactionCountingLogic.handleResponseInFlow(messageContext);
        if(tCount > 0) {
            LOG.info("New transaction detected in ResponseInFlow - Count: " + tCount);
            if(this.transactionAggregator != null && this.transactionAggregator.isEnabled()) {
                this.transactionAggregator.addTransactions(tCount);
            }
        }
        return true;
    }

    @Override
    public boolean handleResponseOutFlow(MessageContext messageContext) {
        if(!enabled) {
            return true;
        }
        int tCount = TransactionCountingLogic.handleResponseOutFlow(messageContext);
        if(tCount > 0) {
            LOG.info("New transaction detected in ResponseOutFlow - Count: " + tCount);
            if(this.transactionAggregator != null && this.transactionAggregator.isEnabled()) {
                this.transactionAggregator.addTransactions(tCount);
            }
        }
        return true;
    }

    @Override
    public boolean handleServerInit() {
        // Nothing to implement
        return true;
    }

    @Override
    public boolean handleServerShutDown() {
        // Clean up resources
        if (transactionAggregator != null && transactionAggregator.isEnabled()) {
            transactionAggregator.shutdown();
        }
        return true;
    }

    @Override
    public boolean handleArtifactDeployment(String s, String s1, String s2) {
        // Nothing to implement
        return true;
    }

    @Override
    public boolean handleArtifactUnDeployment(String s, String s1, String s2) {
        // Nothing to implement
        return true;
    }

    @Override
    public boolean handleError(MessageContext messageContext) {
        // Nothing to implement
        return true;
    }
}
