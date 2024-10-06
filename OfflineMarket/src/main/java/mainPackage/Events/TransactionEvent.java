package mainPackage.Events;

import org.springframework.context.ApplicationEvent;

import mainPackage.Entities.Counteragent;

public class TransactionEvent extends ApplicationEvent{
	
	Object object;
	String msg;
	
	public TransactionEvent(Object source, Object object, String msg)
	{
		super(source);
		this.object = object;
		this.msg = msg;
	}
	
	@Override
	public String toString()
	{
		Class<? extends Object> clazz = object.getClass();
		
		if(clazz==Counteragent.class)
			return ((Counteragent)object).toString() + " " + msg;
		
		return object.toString();
	}
	

}
