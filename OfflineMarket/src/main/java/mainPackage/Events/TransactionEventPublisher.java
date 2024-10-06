package mainPackage.Events;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;


public class TransactionEventPublisher implements ApplicationEventPublisherAware{
	
	ApplicationEventPublisher publisher;
	
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher publisher)
	{
		this.publisher = publisher;
	}
	
	public void publish(Object source, Object object, String msg)
	{
		publisher.publishEvent(new TransactionEvent(source, object, msg));
	}

}
