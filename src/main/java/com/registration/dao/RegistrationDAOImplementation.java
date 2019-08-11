package com.registration.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.registration.UserEntity.User;

@Repository("RegistrationDAO")
public class RegistrationDAOImplementation implements RegistrationDAO {
	
	@Autowired
	JdbcTemplate template;

	public int userRegistration(User user) {
		int result=0;
		String query = "insert into user values(?,?,?,?,?,?,?,?)";
		result=template.update(query,user.getFirst_name(),user.getLast_Name(),user.getEmail(),user.getCardNumber(),user.getCvv(),
				user.getExpiryMonth(),user.getExpiryYear(),user.getPin());

		return result;
	}

	@Override
	public List<String> getAllActivateCards() {
		
		List<String> listOfActivateCards=new ArrayList<>();
		String query="select first_name,last_Name,email,cardNumber,cvv,expiryMonth,expiryYear,pin from user";
		RowMapper<User> rowMapper = new BeanPropertyRowMapper<User>(User.class);

		List<User> users= this.template.query(query, rowMapper);
		
		for(User u:users) {
			listOfActivateCards.add(u.getCardNumber());
		}
		return listOfActivateCards;
	}


	@Override
	public int setPinNumber(User user, String new_pin) {
		
		String query="Update user set pin=? where email=?";
		
		return this.template.update(query, new_pin,user.getEmail());
	}

	@Override
	public List<User> getAllUsers() {
		String query="select first_name,last_Name,email,cardNumber,cvv,expiryMonth,expiryYear,pin from user";
		RowMapper<User> rowMapper = new BeanPropertyRowMapper<User>(User.class);

		List<User> users= this.template.query(query, rowMapper);
		return users;
	}
	
	
}
