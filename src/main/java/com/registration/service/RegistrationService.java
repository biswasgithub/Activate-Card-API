package com.registration.service;

import java.util.List;

import com.registration.UserEntity.User;


public interface RegistrationService {

	List<User> getAllUsers();
	int registerUser(User user) throws Exception;
	String setPinNumber(User user,String new_pin) throws Exception;
}
