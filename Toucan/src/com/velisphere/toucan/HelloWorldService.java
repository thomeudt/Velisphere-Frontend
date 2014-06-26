package com.velisphere.toucan;
 
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
 
@Path("/hello")
public class HelloWorldService {
 
	@GET
	@Path("/{param}")
	public Response getMsg(@PathParam("param") String msg) {
 
		String output = "Jersey says : " + msg;
 
		return Response.status(200).entity(output).build();
 
	} 
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Todo getXML() {
	    Todo todo = new Todo();
	    todo.setSummary("This is my first todo");
	    todo.setDescription("This is my first todo");
	 return todo;
	  }
	
	 // This can be used to test the integration with the browser
	  @GET
	  @Produces({ MediaType.TEXT_XML })
	  public Todo getHTML() {
	    Todo todo = new Todo();
	    todo.setSummary("This is my first todo");
	    todo.setDescription("This is my first todo");
	    return todo;
	  }

	 
 
}