package com.tm.user.repository;

import com.tm.user.model.User;
import com.tm.user.response.UserResponse;

public interface UserRepository {

	UserResponse addNewUser(User user);

	String validateUser(UserResponse userResponse);

}
