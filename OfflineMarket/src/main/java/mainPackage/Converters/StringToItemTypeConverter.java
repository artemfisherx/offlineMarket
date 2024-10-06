package mainPackage.Converters;

import org.springframework.core.convert.converter.Converter;

import mainPackage.Entities.Item.ItemType;

public class StringToItemTypeConverter implements Converter<String, ItemType>{
	
	@Override
	public ItemType convert(String text)
	{
		if(text.equalsIgnoreCase("Wallpaper")) return ItemType.Wallpaper;
		if(text.equalsIgnoreCase("Glue")) return ItemType.Glue;
		if(text.equalsIgnoreCase("Tool")) return ItemType.Tool;
		
		throw new IllegalArgumentException("Cannot convert from String to ItemType: inputted text is wrong!");
	}

}
