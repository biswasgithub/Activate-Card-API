package com.registration.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.registration.UserEntity.User;
import com.registration.dao.RegistrationDAO;
import com.registration.exception.CardAlreadyExistException;
import com.registration.exception.InvalidCardNumberException;
import com.registration.exception.InvalidCvvException;
import com.registration.exception.InvalidEmailException;
import com.registration.exception.InvalidExpiryMonthException;
import com.registration.exception.InvalidExpiryYearException;
import com.registration.exception.InvalidFirstNameException;
import com.registration.exception.InvalidLastNameException;
import com.registration.exception.InvalidPinException;
import com.registration.exception.PinSetUnsuccessfullException;

@Service
public class RegistrationServiceImplementation implements RegistrationService {

	@Autowired
	private RegistrationDAO registrationDao;
	
	@Autowired
	private JavaMailSender sender;

	@Override
	public int registerUser(User user) throws Exception {

		int result = 0;
		List<String> active_Cards = registrationDao.getAllActivateCards();
		// List<String> registered_Emails = registrationDao.getAllRegisteredEmails();
		validateUser(user);

		if (active_Cards.contains(user.getCardNumber())) {
			throw new CardAlreadyExistException("RegistrationService.CARD_EXISTS");
		}

		else {
			User new_user = new User();
			new_user.setFirst_name(user.getFirst_name());
			new_user.setLast_Name(user.getLast_Name());
			new_user.setEmail(user.getEmail());
			new_user.setCardNumber(user.getCardNumber());
			new_user.setCvv(user.getCvv());
			new_user.setExpiryMonth(user.getExpiryMonth());
			new_user.setExpiryYear(user.getExpiryYear());
			new_user.setPin("XXXXX");
			result = registrationDao.userRegistration(new_user);
		}

		return result;
	}

	@Override
	public String setPinNumber(User user, String new_pin) throws Exception {
		int result=0;
		String message="";
		validatePin(new_pin);
		List<String> active_cards = registrationDao.getAllActivateCards();
		if (active_cards.contains(user.getCardNumber())) 
		{
			result=registrationDao.setPinNumber(user, new_pin);
			if(result<=0)
				throw new PinSetUnsuccessfullException("RegistrationService.Pin_Not_Set");
			message=sendMail(user);	
		}
		
		return message;
	}
	
	public String sendMail(User user) {
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setTo(user.getEmail());
			helper.setText("Pin set successfully");
			helper.setSubject("Notification regarding set pin");
		}
		catch(MessagingException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return "Error while sending mail";
		}
		sender.send(message);
		return "Pin set successfully!";
	}

	public void validatePin(String pin) throws InvalidPinException {
		if(!isValidPin(pin))
			throw new InvalidPinException("RegistrationService.INVALID_PIN");
	}
	
	public boolean isValidPin(String pin) {
		boolean result = false;
		String regex = "^[0-9]{4}$";

		Pattern pattren = Pattern.compile(regex);
		Matcher matcher = pattren.matcher(pin);
		if (matcher.matches())
			result = true;
		return result;
	}

	public void validateUser(User user) throws Exception {

		if (!isValidFirstName(user.getFirst_name()))
			throw new InvalidFirstNameException("RegistrationService.INVALID_First_Name");
		if (!isValidLastName(user.getLast_Name()))
			throw new InvalidLastNameException("RegistrationService.INVALID_Last_Name");
		if (!isValidEmail(user.getEmail()))
			throw new InvalidEmailException("RegistrationService.INVALID_Email");
		if (!isValidCardNumber(user.getCardNumber()))
			throw new InvalidCardNumberException("RegistrationService.INVALID_Card_Number");
		if (!isValidCVV(user.getCvv()))
			throw new InvalidCvvException("RegistrationService.INVALID_CVV");
		if (!isValidExpiryMonth(user.getExpiryMonth()))
			throw new InvalidExpiryMonthException("RegistrationService.INVALID_Expiry_Month");
		if (!isValidExpiryYear(user.getExpiryYear()))
			throw new InvalidExpiryYearException("RegistrationService.INVALID_Expiry_Year");
	}

	public boolean isValidFirstName(String f_name) {
		boolean result = false;
		String regex = "^[a-zA-Z]{3,15}+$";

		Pattern pattren = Pattern.compile(regex);
		Matcher matcher = pattren.matcher(f_name);
		if (matcher.matches())
			result = true;
		return result;
	}

	public boolean isValidLastName(String l_name) {
		boolean result = false;
		String regex = "^[a-zA-Z]{3,15}+$";

		Pattern pattren = Pattern.compile(regex);
		Matcher matcher = pattren.matcher(l_name);
		if (matcher.matches())
			result = true;
		return result;
	}

	public boolean isValidEmail(String email) {
		boolean result = false;
		String regex = "^[A-Za-z0-9+_.-]+@(.+)$";

		Pattern pattren = Pattern.compile(regex);
		Matcher matcher = pattren.matcher(email);
		if (matcher.matches())
			result = true;
		return result;
	}

	public boolean isValidCardNumber(String CardNumber) {
		boolean result = false;
		// String regex = "^4[0-9]{12}(?:[0-9]{3})?$";
		String regex = "^[0-9]{12}";
		Pattern pattren = Pattern.compile(regex);
		Matcher matcher = pattren.matcher(CardNumber);
		if (matcher.matches())
			result = true;
		return result;
	}

	public boolean isValidCVV(String cvv) {
		boolean result = false;
		String regex = "^[0-9]{3}";
		Pattern pattren = Pattern.compile(regex);
		Matcher matcher = pattren.matcher(cvv);
		if (matcher.matches())
			result = true;
		return result;
	}

	public boolean isValidExpiryMonth(String month) {
		boolean result = false;
		String regex = "^[0-9]{1,2}$";

		Pattern pattren = Pattern.compile(regex);
		Matcher matcher = pattren.matcher(month);
		if (matcher.matches())
			result = true;
		return result;
	}

	public boolean isValidExpiryYear(String year) {
		boolean result = false;
		String regex = "^[0-9]{4}$";

		Pattern pattren = Pattern.compile(regex);
		Matcher matcher = pattren.matcher(year);
		if (matcher.matches())
			result = true;
		return result;
	}

	@Override
	public List<User> getAllUsers() {
		List<User> allUsers=registrationDao.getAllUsers();
		return allUsers;
	}

}
