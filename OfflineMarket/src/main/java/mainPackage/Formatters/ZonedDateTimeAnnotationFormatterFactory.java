package mainPackage.Formatters;

import java.time.ZonedDateTime;
import java.util.Set;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import mainPackage.Annotations.OrderDateTime;

public class ZonedDateTimeAnnotationFormatterFactory implements AnnotationFormatterFactory<OrderDateTime>{

	@Override
	public Set<Class<?>> getFieldTypes()
	{
		return Set.of(ZonedDateTime.class);
	}
	
	@Override
	public Parser<?> getParser(OrderDateTime annotation, Class<?> fieldType)
	{
		System.out.println("!!!ZonedDateTimeAnnotationFormatterFactory:getParser");
		
		return getFormatter();
	}
	
	@Override
	public Printer<?> getPrinter(OrderDateTime annotation, Class<?> fieldType)
	{
		System.out.println("!!!ZonedDateTimeAnnotationFormatterFactory:getPrinter");
		
		return getFormatter();
	}
	
	private ZonedDateTimeFormatter getFormatter()
	{
		return new ZonedDateTimeFormatter();
	}	
	
}
