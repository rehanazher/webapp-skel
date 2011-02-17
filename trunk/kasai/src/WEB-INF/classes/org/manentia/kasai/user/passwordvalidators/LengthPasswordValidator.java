package org.manentia.kasai.user.passwordvalidators;

import java.util.ResourceBundle;

import org.apache.commons.lang.math.NumberUtils;
import org.manentia.kasai.Constants;

public class LengthPasswordValidator implements PasswordValidator {

	public boolean validate(String password) {
		boolean result = false;
		
		ResourceBundle res = ResourceBundle.getBundle(Constants.CONFIG_PROPERTY_FILE);
		int minLength = NumberUtils.toInt(res.getString("lengthPasswordValidator.minLength"));
		
		if (minLength>0){
			result = (password != null) && password.length()>=minLength;
		} else {
			result = true;
		}
		
		return result;
	}

}
