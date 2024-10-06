package mainPackage;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import mainPackage.CustomExceptions.NotEnoughItemBalanceException;

@ControllerAdvice
public class MainControllerAdvice {
	
	@Autowired
	MessageSource msgSource;
		
	@ExceptionHandler
	public ResponseEntity NotEnoughItemBalanceExceptionHandler(NotEnoughItemBalanceException ex)
	{
		String message = msgSource.getMessage("not_enough_item_balance_ex", new Object[] { ex.getItem(), ex.getStore()}, Locale.getDefault());
		System.out.println("!!!"+message);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(message);
	}
	

}
