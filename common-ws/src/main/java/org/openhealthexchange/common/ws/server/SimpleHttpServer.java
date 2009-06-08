package org.openhealthexchange.common.ws.server;

import java.io.IOException;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.transport.http.server.AxisParams;
import org.apache.axis2.transport.http.server.HttpConnectionManager;
import org.apache.axis2.transport.http.server.IOProcessor;
import org.apache.axis2.transport.http.server.WorkerFactory;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;

import edu.emory.mathcs.backport.java.util.concurrent.ExecutorService;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;


public class SimpleHttpServer {

	private static final Logger LOG = Logger.getLogger(SimpleHttpServer.class);

    private static final int SHUTDOWN_GRACE_PERIOD = 3000; // ms

    private IheHttpFactory httpFactory;
    private final int port;
    private final HttpParams params;
    private final WorkerFactory workerFactory;

    private IOProcessor listener = null;
    private ExecutorService listenerExecutor = null;
    private HttpConnectionManager connmanager = null;
    private ExecutorService requestExecutor = null;

    public SimpleHttpServer(ConfigurationContext configurationContext, WorkerFactory workerFactory,
                            int port) throws IOException {
        this(new IheHttpFactory(configurationContext, port, workerFactory), port);
    }

    public SimpleHttpServer(IheHttpFactory httpFactory, int port) throws IOException {
        this.httpFactory = httpFactory;
        this.port = port;
        this.workerFactory = httpFactory.newRequestWorkerFactory();
        this.params = httpFactory.newRequestConnectionParams();
        this.params.setIntParameter(AxisParams.LISTENER_PORT, port);
    }

    public void init() throws IOException {
        requestExecutor = httpFactory.newRequestExecutor(port);
        connmanager =
                httpFactory.newRequestConnectionManager(requestExecutor, workerFactory, params);
        listenerExecutor = httpFactory.newListenerExecutor(port);
        listener = httpFactory.newRequestConnectionListener(port, connmanager, params);
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
        return this.port;
    }


}
