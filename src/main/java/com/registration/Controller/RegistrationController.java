package com.registration.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.registration.UserEntity.User;
import com.registration.exception.CardAlreadyExistException;
import com.registration.exception.PinSetUnsuccessfullException;
import com.registration.service.RegistrationService;

@CrossOrigin("*")
@RestController
@RequestMapping("RegisterAPI")
public class RegistrationController {

	@Autowired
	private RegistrationService registrationService;

	@Autowired
	private Environment environment;

	@GetMapping("/getAllUsers")
	public List<User> getAllUsers() {
		List<User> userList = registrationService.getAllUsers();
		return userList;
	}

	@PostMapping("/addUser")
	public String addUser(@RequestBody User user) throws Exception {
		String returnMessage = null;
		HttpStatus status = null;
		String new_pin = user.getPin();

		/*
		 * String new_pin = "5463"; User user = new User();
		 * user.setFirst_name("Souvik"); user.setLast_Name("Roy");
		 * user.setEmail("milton.juit08@gmail.com"); user.setCardNumber("123456789555");
		 * user.setCvv("435"); user.setExpiryMonth("09"); user.setExpiryYear("2025");
		 */

		try {
			registrationService.registerUser(user);
			returnMessage = environment.getProperty("RegistrationController.SUCCESSFULLY_ADDED");

			status = HttpStatus.OK;

			String message = registrationService.setPinNumber(user, new_pin);
			System.out.println("***********" + message);

		} catch (CardAlreadyExistException | PinSetUnsuccessfullException e) {
			System.out.println(e);

			if (e.getMessage().contains("RegistrationService")) {
				returnMessage = environment.getProperty(e.getMessage());
				status = HttpStatus.BAD_REQUEST;
			}
		}

		return returnMessage;
	}

}
