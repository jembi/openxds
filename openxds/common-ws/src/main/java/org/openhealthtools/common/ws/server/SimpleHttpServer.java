/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 * Contributors:
 *   Apache Software Foundation  - inital API and implementation
 *   Misys Open Source Solutions - modified
 */
package org.openhealthtools.common.ws.server;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.transport.http.server.AxisParams;
import org.apache.axis2.transport.http.server.HttpConnectionManager;
import org.apache.axis2.transport.http.server.IOProcessor;
import org.apache.axis2.transport.http.server.WorkerFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.params.HttpParams;

import com.misyshealthcare.connect.net.IConnectionDescription;


/**
 * A simple, but configurable and extensible HTTP server.
 */
public class SimpleHttpServer {

    private static Log LOG = LogFactory.getLog(SimpleHttpServer.class);

    private static final int SHUTDOWN_GRACE_PERIOD = 3000; // ms

    private IheHttpFactory httpFactory;
    private final IConnectionDescription connection;
    private final HttpParams params;
    private final WorkerFactory workerFactory;

    private IOProcessor listener = null;
    private ExecutorService listenerExecutor = null;
    private HttpConnectionManager connmanager = null;
    private ExecutorService requestExecutor = null;

    public SimpleHttpServer(ConfigurationContext configurationContext, WorkerFactory workerFactory,
    						IConnectionDescription connection) throws IOException {    	
        this(new IheHttpFactory(configurationContext, connection, workerFactory), connection);
    }

    public SimpleHttpServer(IheHttpFactory httpFactory, IConnectionDescription connection) throws IOException {
        this.httpFactory = httpFactory;
        this.connection = connection;
        this.workerFactory = httpFactory.newRequestWorkerFactory();
        this.params = httpFactory.newRequestConnectionParams();
        this.params.setIntParameter(AxisParams.LISTENER_PORT, connection.getPort());
    }

    public void init() throws IOException {
        requestExecutor = httpFactory.newRequestExecutor(connection.getPort());
        connmanager =
                httpFactory.newRequestConnectionManager(requestExecutor, workerFactory, params);
        listenerExecutor = httpFactory.newListenerExecutor(connection.getPort());
        listener = httpFactory.newRequestConnectionListener(connection, connmanager, params);
    }

    public void destroy() throws IOException, InterruptedException {
        // Attempt to terminate the listener nicely
        LOG.info("Shut down connection listener");
        this.listenerExecutor.shutdownNow();
        this.listener.destroy();
        this.listenerExecutor.awaitTermination(SHUTDOWN_GRACE_PERIOD, TimeUnit.MILLISECONDS);
        if (!this.listenerExecutor.isTerminated()) {
            // Terminate the listener forcibly
            LOG.info("Force shut down connection listener");
            this.listener.destroy();
            // Leave it up to the garbage collector to clean up the mess
            this.listener = null;
        }
        // Attempt to terminate the active processors nicely
        LOG.info("Shut down HTTP processors");
        this.requestExecutor.shutdownNow();
        this.requestExecutor.awaitTermination(SHUTDOWN_GRACE_PERIOD, TimeUnit.MILLISECONDS);
        if (!this.requestExecutor.isTerminated()) {
            // Terminate the active processors forcibly
            LOG.info("Force shut down HTTP processors");
            this.connmanager.shutdown();
            // Leave it up to the garbage collector to clean up the mess
            this.connmanager = null;
        }
        LOG.info("HTTP protocol handler shut down");
    }

    public void start() {
        this.listenerExecutor.execute(this.listener);
    }

    public boolean isRunning() {
        return this.listenerExecutor != null && !this.listenerExecutor.isShutdown();
    }

    public int getPort() {
        return this.connection.getPort();
    }

}

