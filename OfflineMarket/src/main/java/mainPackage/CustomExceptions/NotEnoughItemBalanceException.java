package mainPackage.CustomExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class NotEnoughItemBalanceException extends RuntimeException{
	
	private String item;
	private String store;
	
	public NotEnoughItemBalanceException(String message, String item, String store)
	{
		super(message);
		this.item = item;
		this.store = store;
	}

	public String getItem() {
		return item;
	}

	public String getStore() {
		return store;
	}

}
