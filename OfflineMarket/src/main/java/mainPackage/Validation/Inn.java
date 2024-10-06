package mainPackage.Validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=InnValidator.class)
public @interface Inn {
	
	String message() default "INN must have 10 or 12 digits";
	Class<?> [] groups() default {};
	Class<? extends Payload> [] payload() default {};

}
