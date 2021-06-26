package com.tm.user.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

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
	public String addNewUser(UserRequest userRequest) throws UserException {
		beginTransaction();
		User user = entityManager.find(User.class, userRequest.getUserName());
		endTransaction();
		if(user!=null) {
			throw new UserException(UserConstants.DUPLICATE_USER_NAME);
		}
		User newUser = new User();
		newUser.setFirstName(userRequest.getFirstName());
		newUser.setUserName(userRequest.getUserName());
		newUser.setPassword(userRequest.getPassword());
		newUser.setRole(UserConstants.PASSENGER);
		if(userRequest.getLastName()!=null)
			newUser.setLastName(userRequest.getLastName());
		try {
			beginTransaction();
			entityManager.persist(newUser);
			endTransaction();
		}catch(PersistenceException e) {
			throw new UserException(UserConstants.USER_REGISTRATION_ERROR);
		}
		return UserConstants.USER_REGISTRATION_SUCCESS;
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
			throw new UserException(UserConstants.LOGIN_ERROR);
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
			throw new UserException(UserConstants.ASSIGN_ADMIN_ERROR);
		}
		endTransaction();
		return "userId: " + userId + " is updated to admin role successfully";
	}

}
