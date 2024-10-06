package mainPackage.Formatters;

import java.util.Locale;

import org.springframework.format.Formatter;

import mainPackage.MainRepository;
import mainPackage.Entities.Counteragent;

public class CounteragentFormatter implements Formatter<Counteragent>{
	
	MainRepository repo;
	
	public CounteragentFormatter(MainRepository repo)
	{
		this.repo = repo;
	}
	
	@Override
	public String print(Counteragent agent, Locale locale)
	{
		System.out.println("!!!CounteragentFormatter print:" + agent.getName());		
		return agent.getName();
	}
	
	@Override
	public Counteragent parse(String text, Locale locale)
	{
		System.out.println("!!!CounteragentFormatter:parse");
		System.out.println("!!!CounteragentFormatter text:" + text);
		
		int id = Integer.valueOf(text);
		
		return repo.getCounteragent(id);
	}

}
