package mainPackage.Formatters;

import java.util.Locale;

import org.springframework.format.Formatter;

import mainPackage.MainRepository;
import mainPackage.Entities.Item;

public class ItemFormatter implements Formatter<Item>{
	
	MainRepository repo;
	
	public ItemFormatter(MainRepository repo)
	{
		this.repo = repo;
	}

	@Override
	public String print(Item item, Locale locale)
	{
		System.out.println("!!!ItemFormatter print:"+item.getName());
		return item.getItemNumber() + " " + item.getName();
	}
	
	@Override
	public Item parse(String text, Locale locale)
	{
		System.out.println("!!!ItemFormatter parse:"+text);
		
		int id = Integer.valueOf(text);
		return repo.getItem(id);		
	}
}
