package com.tm.user.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.tm.user.constants.UserConstants;
import com.tm.user.model.User;
import com.tm.user.repository.UserRepository;
import com.tm.user.response.UserResponse;

public class UserRepositoryImpl implements UserRepository{
	
	static EntityManagerFactory factory;
	static EntityManager entityManager;
	
	@Override
	public UserResponse addNewUser(User user) {
		beginTransaction();
		UserResponse userResponse = new UserResponse();
		user.setRole(UserConstants.PASSENGER);
		entityManager.persist(user);
		endTransaction();
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
	public String validateUser(UserResponse userResponse) {
		beginTransaction();
		User user = entityManager.find(User.class, userResponse.getUserName());
		endTransaction();
		if(user!=null && user.getPassword().equals(userResponse.getPassword())) {
			return "LOGIN SUCCESS!";
		}
		return "LOGIN FAILED";
	}

}
