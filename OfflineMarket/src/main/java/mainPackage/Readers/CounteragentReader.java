package mainPackage.Readers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import mainPackage.MainRepository;
import mainPackage.Entities.Address;
import mainPackage.Entities.Counteragent;

public class CounteragentReader implements IReader {
	
	@Autowired
	MainRepository repo;
	
	public void read(InputStream input)
	{
		Instant instant = Instant.now();
		System.out.println("!!!CounteragentReader:" + instant);
		
		InputStreamReader reader = new InputStreamReader(input);
		BufferedReader bReader = new BufferedReader(reader);
		
		List<String> lines = bReader.lines().toList();
		
		int id = Integer.parseInt(lines.get(0));
		long inn = Long.parseLong(lines.get(1));
		String name = lines.get(2);
		String country = lines.get(3);
		String city = lines.get(4);
		String street = lines.get(5);
		String building = lines.get(6);	
		
		Counteragent agent = new Counteragent();
		agent.setId(id);
		agent.setInn(inn);
		agent.setName(name);
		
		Address address = new Address(country, city, street, building);
		
		agent.setAddress(address);
		
		repo.insertCounteragent(agent);
	}

}
