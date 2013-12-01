package de.server.main;

import java.net.BindException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.spi.container.servlet.ServletContainer;

/**
 * Deploy a RESTful service using Jersey in an embedded Jetty server.
 * 
 * Requires: Jetty 8 JARs.
 * 
 * @author James Brucker
 */
public class JettyServer_new {
	private Server server;
	private ServletHolder servletHolder;
	
	/**
	 * Create a Jetty Server using given port.
	 * @param port the port to listen on
	 */
	public JettyServer_new( int port ) {
		System.out.println("THATS A TEST OUTPUT");
		server = new Server(port);
		servletHolder = new ServletHolder(ServletContainer.class);
	}

	
	/**
	 * Set a servlet initialization parameter.
	 * For example, use a packaged-based configuration of Jersey 1.x you could set:
	 * <pre>
	   server.setInitParameter(
	       "com.sun.jersey.config.property.resourceConfigClass",
	       "com.sun.jersey.api.core.PackagesResourceConfig");
	   server.setInitParameter(
	       "com.sun.jersey.config.property.packages",
	       "todo.resource");
	   </pre>
	 * @param paramName name of parameter to set
	 * @param paramValue value of the parameter
	 */
	public void setInitParameter(String paramName, String paramValue) {
		servletHolder.setInitParameter( paramName, paramValue );
	}
	
	/**
	 * Start the server that listens on context path "/" for all requests.
	 * @throws BindException
	 * @throws Exception
	 */
	private void start( ) throws Exception  {		

// uncomment these to enable tracing of requests and responses	
//		servletHolder.setInitParameter("com.sun.jersey.config.feature.Debug", "true");
//		servletHolder.setInitParameter("com.sun.jersey.config.feature.Trace", "true");
//		servletHolder.setInitParameter("com.sun.jersey.spi.container.ContainerRequestFilters", 
//				"com.sun.jersey.api.container.filter.LoggingFilter");
//		servletHolder.setInitParameter("com.sun.jersey.spi.container.ContainerResponseFilters", 
//				"com.sun.jersey.api.container.filter.LoggingFilter");
		
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

		// Map the context path to Jetty servlet
		
		context.setContextPath("/");
		context.addServlet(servletHolder, "/*");
		
		server.setHandler(context);
 
		QueuedThreadPool qtp = new QueuedThreadPool(10);
		qtp.setName("ApiServe");
		server.setThreadPool(qtp);
        
		server.start();	
	}
	
	/**
	 * Stop the server
	 * @throws Exception if stop method encounters problem
	 */
	public void stop( ) throws Exception {
		if (server != null && server.isRunning()) server.stop();
	}
	
	/**
	 * Demonstrate how to start the JettyServer and issue a request.
	 * 
	 * @param args not used
	 * @throws BindException
	 * @throws Exception
	 */
	public static void main(String[] args) throws BindException, Exception {
		int port = 8080;
		JettyServer_new server = new JettyServer_new( port );
		
		// Add init parameters to discover resource classes
		
// configuration for a Jersey 1.x service:
		server.setInitParameter( 
				"com.sun.jersey.config.property.resourceConfigClass",
				"com.sun.jersey.api.core.PackagesResourceConfig");
// name of the package where your resource classes are:
		server.setInitParameter(
				"com.sun.jersey.config.property.packages", "de.server.resource"  );
		
		System.out.println("Starting server...");
		server.start( );
		System.out.printf("Server started on port %d\n",  port);
		
		
		// stop the server
		System.out.println("press enter to stop server...");
		System.in.read();
		server.stop();
	}
	
	/**
	 * This method doesn't belong here. Should be part of test framework.
	 * @return a Jersey client
	 */
	private static Client createClient() {
		Client client = Client.create();
		client.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);
		
		// Optional: add a filter to display request and response
		// Logging filter can use a java.util.logging.Logger or any PrintStream
		LoggingFilter logFilter = new LoggingFilter( System.out );
		// You can add filters to a client or a WebResource
		client.addFilter( logFilter );
		return client;
	}
}
