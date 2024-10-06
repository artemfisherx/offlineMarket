package mainPackage.Formatters;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.format.Formatter;

public class ZonedDateTimeFormatter implements Formatter<ZonedDateTime>{
	
	@Override
	public String print(ZonedDateTime zdt, Locale locale)
	{		
		String s = zdt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
		return s;
	}
	
	@Override
	public ZonedDateTime parse(String text, Locale locale)
	{		
		System.out.println("ZonedDateTimeFormatter parse:"+text);
		
		LocalDateTime ldt= LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
		
		return ZonedDateTime.of(ldt, ZoneId.systemDefault());
	}

}
