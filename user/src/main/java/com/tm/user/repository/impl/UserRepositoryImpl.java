package com.tm.user.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.exception.ConstraintViolationException;

import com.tm.user.constants.UserConstants;
import com.tm.user.exception.UserException;
import com.tm.user.model.User;
import com.tm.user.repository.UserRepository;
import com.tm.user.request.UserRequest;
import com.tm.user.response.UserResponse;



public class UserRepositoryImpl implements UserRepository{
	
	static EntityManagerFactory factory;
	static EntityManager entityManager;
	
	@Override
	public UserResponse addNewUser(User user) throws UserException {
		UserResponse userResponse = new UserResponse();
		try {
			beginTransaction();
			user.setRole(UserConstants.PASSENGER);
			entityManager.persist(user);
			endTransaction();
		}catch(Exception e) {
			throw new UserException("This username already exists.Please try a different one");
		}
		userResponse.setFirstName(user.getFirstName());
		userResponse.setLastName(user.getLastName());
		userResponse.setRole(user.getRole());
		userResponse.setUserName(user.getUserName());
		return userResponse;
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


	@Override
	public UserResponse validateUser(UserRequest userRequest) throws UserException {
		beginTransaction();
		User user = entityManager.find(User.class, userRequest.getUserName());
		endTransaction();
		UserResponse userResponse = new UserResponse();
		if(user!=null && user.getPassword().equals(userRequest.getPassword())) {
			userResponse.setFirstName(user.getFirstName());
			userResponse.setLastName(user.getLastName());
			userResponse.setRole(user.getRole());
			userResponse.setUserName(user.getUserName());	
		}else {
			throw new UserException("INVALID LOGIN CREDENTIALS");
		}
		return userResponse;
	}

	@Override
	public String assignAdminRole(String userId) throws UserException {
		beginTransaction();
		try {
			User user = entityManager.find(User.class, userId);
			if(user!=null) {
				user.setRole(UserConstants.ADMIN);
				entityManager.merge(user);
			}
		}catch(Exception e) {
			throw new UserException("Some error occured while updating to admin role");
		}
		endTransaction();
		return "userId: " + userId + " is updated to admin role successfully";
	}

}
