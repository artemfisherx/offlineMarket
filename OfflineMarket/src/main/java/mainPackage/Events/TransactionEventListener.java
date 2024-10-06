package mainPackage.Events;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class TransactionEventListener {
	
	@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
	public void handleEventBeforeCommit(TransactionEvent event)
	{
		System.out.println("BEFORE_COMMIT:" + event);
	}
	
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleEventAfterCommit(TransactionEvent event)
	{
		System.out.println("AFTER_COMMIT:" + event);
	}
	
	@TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
	public void handleEventAfterRollback(TransactionEvent event)
	{
		System.out.println("AFTER_ROLLBACK:" + event);
	}
	
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
	public void handelEventAfterCompletion(TransactionEvent event)
	{	
		System.out.println("AFTER_COMPLETION:" + event);		
	}

}
