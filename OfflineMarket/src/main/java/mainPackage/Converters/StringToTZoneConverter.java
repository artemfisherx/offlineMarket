package mainPackage.Converters;

import org.springframework.core.convert.converter.Converter;

import mainPackage.Entities.Employee.TZone;

public class StringToTZoneConverter implements Converter<String, TZone>{
	
	@Override
	public TZone convert(String text)
	{
		if(text.equalsIgnoreCase("Moscow")) return TZone.Moscow;
		if(text.equalsIgnoreCase("Miami")) return TZone.Miami;
		if(text.equalsIgnoreCase("Beijing")) return TZone.Beijing;
		
		throw new IllegalArgumentException("Cannot convert from String to TZone: inputted text is wrong!");
	}

}
