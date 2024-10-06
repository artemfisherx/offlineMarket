package mainPackage.Converters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import mainPackage.Entities.Counteragent;

public class CounteragentHttpMessageConverter extends AbstractHttpMessageConverter<Counteragent>{
	
	public CounteragentHttpMessageConverter()
	{		
		super(MediaType.ALL);
		System.out.println("!!!CounteragentHttpMessageConverter:constructor");
	}
	
	@Override
	protected boolean supports(Class<?> clazz)
	{
		return Counteragent.class.isAssignableFrom(clazz);
	}
	
	@Override
	protected Counteragent readInternal(Class<? extends Counteragent> clazz, HttpInputMessage inputMessage) 
			throws IOException, HttpMessageNotReadableException
	{
		InputStreamReader reader = new InputStreamReader(inputMessage.getBody());
		BufferedReader bReader = new BufferedReader(reader);
		
		System.out.println("!!!CounteragentHttpMessageConverter:readInternal");
		bReader.lines().forEach(System.out::println);		
		
		return null;
	}
	
	@Override
	protected void writeInternal(Counteragent agent, HttpOutputMessage outputMessage) 
			throws IOException, HttpMessageNotWritableException
	{
		System.out.println("!!!CounteragentHttpMessageConverter:writeInternal");
		OutputStream body = outputMessage.getBody();
		String id = String.valueOf(agent.getId())+"\n";		
		body.write(id.getBytes());		
	}

}
