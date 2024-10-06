package mainPackage.Formatters;

import java.util.Locale;

import org.springframework.format.Formatter;

import mainPackage.MainRepository;
import mainPackage.Entities.Employee;

public class EmployeeFomatter implements Formatter<Employee>{
	
	MainRepository repo;
	
	public EmployeeFomatter(MainRepository repo)
	{
		this.repo = repo;
	}
	
	@Override
	public String print(Employee object, Locale locale)
	{
		return object.getName() + " " + object.getSurname();
	}
	
	@Override
	public Employee parse(String text, Locale locale)
	{
		int id = Integer.valueOf(text.split("\s+")[0]);
		
		System.out.println("EmployeeFomatter parse:"+id);
		
		return repo.getEmployee(id);		
	}

}
