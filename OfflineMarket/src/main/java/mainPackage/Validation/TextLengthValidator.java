package mainPackage.Validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TextLengthValidator implements ConstraintValidator<TextLength, String>
{
	long min;
	long max;
	
	@Override
	public void initialize(TextLength txl)
	{
		min = txl.min();
		max = txl.max();		
		
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context)
	{						
		if(value.length()>=min&&value.length()<=max)
			return true;
		else
			return false;
	}

}
