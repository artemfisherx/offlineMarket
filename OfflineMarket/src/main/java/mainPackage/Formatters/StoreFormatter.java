package mainPackage.Formatters;

import java.util.Locale;

import org.springframework.format.Formatter;

import mainPackage.MainRepository;
import mainPackage.Entities.Store;

public class StoreFormatter implements Formatter<Store>{
	
	MainRepository repo;
	
	public StoreFormatter(MainRepository repo)
	{
		this.repo = repo;
	}
	
	@Override
	public String print(Store store, Locale locale)
	{
		return store.getName();
	}
	
	@Override
	public Store parse(String text, Locale locale)
	{
		int id = Integer.valueOf(text);
		
		return repo.getStore(id);		
	}

}
