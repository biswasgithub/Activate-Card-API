package com.registration.dao;

import java.util.List;

import com.registration.UserEntity.User;


public interface RegistrationDAO {

	List<User> getAllUsers();
	int userRegistration(User user);
	List<String> getAllActivateCards();
	int setPinNumber(User user, String new_pin);
}
