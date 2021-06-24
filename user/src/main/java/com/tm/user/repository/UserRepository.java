package com.tm.user.repository;

import com.tm.user.exception.UserException;
import com.tm.user.model.User;
import com.tm.user.request.UserRequest;
import com.tm.user.response.UserResponse;

public interface UserRepository {

	UserResponse addNewUser(User user) throws UserException;

	UserResponse validateUser(UserRequest userRequest) throws UserException;

	String assignAdminRole(String userId) throws UserException;

}
