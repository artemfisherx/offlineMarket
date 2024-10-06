package mainPackage.Validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TextLengthValidator.class)
public @interface TextLength {
	
	String message() default "The length is wrong";
	Class<?> [] groups() default {};
	Class<? extends Payload> [] payload() default {};
	long min() default 1;
	long max() default Long.MAX_VALUE;

}
