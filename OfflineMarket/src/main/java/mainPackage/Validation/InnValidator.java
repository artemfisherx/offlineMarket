package mainPackage.Validation;

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class InnValidator implements ConstraintValidator<Inn, Long>{
	
	@Override
	public boolean isValid(Long value, ConstraintValidatorContext context)
	{			
		boolean res1 = Pattern.matches("^\\d{10}$", value.toString());
		boolean res2 = Pattern.matches("^\\d{12}$", value.toString());
		
		if(res1||res2)
			return true;
		else 
			return false;		
	}

}
