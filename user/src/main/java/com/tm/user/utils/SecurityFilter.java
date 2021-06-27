package com.tm.user.utils;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.tm.user.model.User;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
public class SecurityFilter implements ContainerRequestFilter{
	
	private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
	private static final String AUTHORIZATION_HEADER_PREFIX = "Basic ";
	private static final String SECURED_URI_PREFIX = "secureduser";
	
	EntityManagerFactory factory;
	EntityManager entityManager;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		if(requestContext.getUriInfo().getPath().contains(SECURED_URI_PREFIX)) {
			List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);
			if(authHeader!=null && !authHeader.isEmpty()) {
				String authToken = authHeader.get(0);
				authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX,"");
				byte[] decoded = Base64.getDecoder().decode(authToken);
				String decodedString = new String(decoded);
				StringTokenizer tokenizer = new StringTokenizer(decodedString,":");
				String userName = tokenizer.nextToken();
				String password = tokenizer.nextToken();
				beginTransaction();
				User user = entityManager.find(User.class, userName);
				endTransaction();
				if(user!=null && PasswordUtils.verifyUserPassword(password,
						user.getSecurePassword(),user.getSalt()) && "A".equals(user.getRole())) {
					return;
				}
			}
			Response unauthorizedStatus = Response
					.status(Response.Status.UNAUTHORIZED)
					.entity("User has no access")
					.build();
			requestContext.abortWith(unauthorizedStatus);
		}
	}
	
	private void beginTransaction() {
		factory = Persistence.createEntityManagerFactory("UserUnit");
    	entityManager = factory.createEntityManager();
    	entityManager.getTransaction().begin();
	}
	
	private void endTransaction() {
		entityManager.getTransaction().commit();
    	entityManager.close();
    	factory.close();
	}
}

