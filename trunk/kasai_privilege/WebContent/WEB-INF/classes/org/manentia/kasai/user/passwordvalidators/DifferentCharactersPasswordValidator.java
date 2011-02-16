package org.manentia.kasai.user.passwordvalidators;

import java.util.ResourceBundle;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.manentia.kasai.Constants;

public class DifferentCharactersPasswordValidator implements PasswordValidator {

	public boolean validate(String password) {
		boolean result = false;
		
		ResourceBundle res = ResourceBundle.getBundle(Constants.CONFIG_PROPERTY_FILE);
		int minNumeric = NumberUtils.toInt(res.getString("differentCharactersPasswordValidator.minNumeric"));
		int minAlpha = NumberUtils.toInt(res.getString("differentCharactersPasswordValidator.minAlpha"));
		int minOther = NumberUtils.toInt(res.getString("differentCharactersPasswordValidator.minOther")); 
		
		password = StringUtils.defaultString(password);
		
		int numericCount = 0;
		int alphaCount = 0;
		int otherCount = 0;
		
		for (char c : password.toCharArray()){
			if (CharUtils.isAsciiAlpha(c)){
				alphaCount++;
			} else if (CharUtils.isAsciiNumeric(c)){
				numericCount++;
			} else {
				otherCount++;
			}
		}
		
		if (numericCount>= minNumeric && alphaCount>=minAlpha && otherCount>=minOther){
			result = true;
		}
		
		return result;
	}

}
