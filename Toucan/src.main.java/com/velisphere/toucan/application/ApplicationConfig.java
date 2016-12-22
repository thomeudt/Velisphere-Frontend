package com.velisphere.toucan.application;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
 





import javax.ws.rs.ApplicationPath;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;






import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.velisphere.toucan.amqp.AMQPServer;
import com.velisphere.toucan.amqp.EventInitiator;
import com.velisphere.toucan.amqp.EventResponder;
import com.velisphere.toucan.amqp.ServerParameters;

 
@ApplicationPath("/service")
public class ApplicationConfig extends Application {
 
	
	
    @Override
    public Set<Class<?>> getClasses() {
        
    
    	// Start the AMQP handling component
    	
    	EventResponder eventResponder = new EventResponder();
		EventInitiator initiator = new EventInitiator();
		initiator.addListener(eventResponder);
	
		
		AMQPServer.startServer(initiator);
    	
    	
    	
        Set<Class<?>> resources = new java.util.HashSet<>();
        
        System.out.println("Requesting server parameters from BlenderServer...");            
        
        getConfigData();

        System.out.println("Requesting server parameters from BlenderServer completed.");            
        
        
        System.out.println("REST configuration starting: getClasses()");            
        
        //features
        //this will register Jackson JSON providers
        resources.add(JacksonFeature.class);
        resources.add(MultiPartFeature.class);

        //we could also use this:
        //resources.add(JacksonJaxbJsonProvider.class);
        
        //instead let's do it manually:
        resources.add(com.velisphere.toucan.providers.MyJacksonJsonProvider.class);
        
        
        resources.add(com.velisphere.toucan.webservices.Analytics.class);
        resources.add(com.velisphere.toucan.webservices.Endpoint.class);
        resources.add(com.velisphere.toucan.webservices.EndpointClass.class);
        resources.add(com.velisphere.toucan.webservices.FileStorage.class);
        resources.add(com.velisphere.toucan.webservices.Message.class);
        resources.add(com.velisphere.toucan.webservices.Properties.class);
        resources.add(com.velisphere.toucan.webservices.Property.class);
        resources.add(com.velisphere.toucan.webservices.PropertyClass.class);
        resources.add(com.velisphere.toucan.webservices.Provisioning.class);
        resources.add(com.velisphere.toucan.webservices.Sphere.class);
        resources.add(com.velisphere.toucan.webservices.User.class);
        resources.add(com.velisphere.toucan.webservices.Vendor.class);
       
        
        //==> we could also choose packages, see below getProperties()
        
        System.out.println("REST configuration ended successfully.");
        
        return resources;
    }
    
    @Override
    public Set<Object> getSingletons() {
        return Collections.emptySet();
    }
    
    @Override
    public Map<String, Object> getProperties() {
        Map<String, Object> properties = new HashMap<>();
        
        //in Jersey WADL generation is enabled by default, but we don't 
        //want to expose too much information about our apis.
        //therefore we want to disable wadl (http://localhost:8080/service/application.wadl should return http 404)
        //see https://jersey.java.net/nonav/documentation/latest/user-guide.html#d0e9020 for details
        properties.put("jersey.config.server.wadl.disableWadl", true);
        
        //we could also use something like this instead of adding each of our resources
        //explicitly in getClasses():
        //properties.put("jersey.config.server.provider.packages", "com.nabisoft.tutorials.mavenstruts.service");
        
        
        return properties;
    }    
    
    
    private void getConfigData()
    {
    	Client client = ClientBuilder.newClient();
		WebTarget target = client.target( "http://www.connectedthingslab.com:8080/BlenderServer/rest/config/get/general" );
		Response response = target.path("RABBIT").request().get();
		ServerParameters.rabbit_ip = response.readEntity(String.class);
		System.out.println(" [IN] BlenderServer provided Rabbit IP: " + ServerParameters.rabbit_ip);
		response = target.path("VOLT").request().get();
		ServerParameters.volt_ip = response.readEntity(String.class);
		System.out.println(" [IN] BlenderServer provided Volt IP: " + ServerParameters.volt_ip);
		response = target.path("VERTICA").request().get();
		ServerParameters.vertica_ip = response.readEntity(String.class);
		System.out.println(" [IN] BlenderServer provided Vertica IP: " + ServerParameters.vertica_ip);
    	
    }
}