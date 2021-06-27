package com.tm.user.resource;

import com.tm.user.exception.UserException;
import com.tm.user.model.User;
import com.tm.user.repository.UserRepository;
import com.tm.user.repository.impl.UserRepositoryImpl;
import com.tm.user.request.UserRequest;
import com.tm.user.response.UserResponse;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("userresource")
public class UserResource {
	
	UserRepository userRepo = new UserRepositoryImpl();

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }
    
    /**
     * Method handling HTTP POST requests for registering new user. The object
     *  will be sent to the client as "JSON" media type.
     *
     * @return User that will be returned as a JSON response.
     * @throws UserException 
     */
    @Path("registeruser")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerNewUser(UserRequest userRequest) throws UserException {
    	String response = userRepo.addNewUser(userRequest);
    	return Response.ok(response).build();
    }
    
    /**
     * Method handling HTTP GET requests for user login. The returned object
     *  will be sent to the client as "text/plain" media type.
     *
     * @return User that will be returned as a text/plain response.
     * @throws UserException 
     */
    @Path("userlogin")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response userLogin(UserRequest userRequest) throws UserException {
    	UserResponse userResponse = userRepo.validateUser(userRequest);
    	return Response.status(200).entity(userResponse).build();
    }
    
}
