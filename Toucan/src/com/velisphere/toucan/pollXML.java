package com.velisphere.toucan;
 
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
 
@Path("/pollXML")
public class pollXML  {
 
	/**
	@GET
	@Path("/{param}")
	public Response getMsg(@PathParam("param") String msg) {
 
		String output = "Jersey says : " + msg;
 
		return Response.status(200).entity(output).build();
 
	} 
	**/
	
	@GET
	@Path("/xml")
	@Produces({ MediaType.APPLICATION_XML })
	public Todo getXML() {
	    Todo todo = new Todo();
	    todo.setSummary("VeliSphere");
	    todo.setDescription("First Sphere");
	 return todo;
	  }
	
	 // This can be used to test the integration with the browser
	
	@GET
	@Path("/text")
	  @Produces({ MediaType.TEXT_XML })
	  public Todo getHTML() {
	    Todo todo = new Todo();
	    todo.setSummary("VeliSphere");
	    todo.setDescription("First Sphere");
	    return todo;
	  }
	 

	 
 
}