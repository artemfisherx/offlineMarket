package mainPackage.Controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Part;
import mainPackage.MainRepository;
import mainPackage.Converters.StringToItemTypeConverter;
import mainPackage.Entities.Counteragent;
import mainPackage.Entities.Item;
import mainPackage.Entities.Item.ItemType;

public class ItemHandler {	
	
	private MainRepository repo;
	private String itemFolder;
	
	public ItemHandler(MainRepository repo, String itemFolder)
	{
		this.repo = repo;	
		this.itemFolder = itemFolder;
	}
	
	public ServerResponse showItemListPage(ServerRequest request)
	{			
		if(request.param("delete").isPresent())
		{
			int id = Integer.valueOf(request.param("delete").get());
			repo.deleteItem(id);
		}
		
		List<Item> items = repo.getAllItems();
		Map<String, List<Item>> model = new HashMap<>();	
		model.put("items", items);
		return ServerResponse.ok().render("itemList", model);
	}
	
	public ServerResponse showAddEditItemPage(ServerRequest request)
	{
		Map<String, Object> model = new HashMap<>();
		List<Counteragent> agents = repo.getAllCounteragents();
		model.put("agents", agents);
		
		String id="";
		try
		{
			id = request.pathVariable("id");
		}
		catch(IllegalArgumentException ex) {}
		
		
		Item item;
		if(id.length()>0)
		{
			int idd = Integer.parseInt(id);
			item = repo.getItem(idd);				
		}
		else
			item = new Item();	
		
		model.put("item", item);
		
		System.out.println("!!!showAddEditItemPage:itemType:"+item.getItemType().ordinal());
					
		return ServerResponse.ok().render("addEditItem", model);
	}
	
	public ServerResponse saveItem(ServerRequest request)
	{				
		try
		{		
			System.out.println("!!!!PARAMS:"+request.params().size());
			
			if(request.multipartData()==null)
				System.out.println("!!!!PARTS: null");
				
			System.out.println("!!!!PARTS:"+request.multipartData().size());
			
			int id = Integer.valueOf(request.param("id").get());
			String itemNumber = request.param("itemNumber").get();
			String name = request.param("name").get();
			
			int mId = Integer.valueOf(request.param("manufacturer").get());
			Counteragent manufacturer = repo.getCounteragent(mId);
			
			int sId = Integer.valueOf(request.param("supplier").get());
			Counteragent supplier = repo.getCounteragent(sId);
			
			String image = request.param("image").get();			
			
			StringToItemTypeConverter converter = new StringToItemTypeConverter();			
			ItemType itemType = converter.convert(request.param("itemType").get());									
			
			Item item = new Item();
			item.setId(id);
			item.setItemNumber(itemNumber);
			item.setName(name);
			item.setManufacturer(manufacturer);
			item.setSupplier(supplier);
			item.setImage(image);
			item.setItemType(itemType);						
			
			Part newImage = request.multipartData().getFirst("newImage");			
			if(newImage.getSize()>0)
			{	
				
				String nameImg = String.valueOf(Instant.now().getEpochSecond())
						+ "." + newImage.getSubmittedFileName().split("\\.")[1];				
				String dirPath = System.getProperty("catalina.home") + "\\webapps\\" + itemFolder;
				
				System.out.println("!!!dirPath:"+dirPath);
				
				if(!Files.exists(Path.of(dirPath)))
				{
					new File(dirPath).mkdirs();
				}
				
				InputStream img = newImage.getInputStream();				
				Files.copy(img, Path.of(dirPath + "//" + nameImg), StandardCopyOption.REPLACE_EXISTING);
				item.setImage(nameImg);
			}
			
			System.out.println("!!!saveItem:itemType:"+item.getItemType().ordinal());
			
			if(item.getId()==0)
				repo.insertItem(item);
			else
				repo.updateItem(item);
			
			return showItemListPage(request);
			
		}
		catch(IOException|ServletException ex)
		{
			Map<String, Object> model = new HashMap<>();
			model.put("error", "Item has not been saved. Try again.");
			model.put("item", new Item());
			model.put("agents", repo.getAllCounteragents());
			
			System.out.println(ex);			
			return ServerResponse.ok().render("addEditItem", model);
		}		
	}

}
