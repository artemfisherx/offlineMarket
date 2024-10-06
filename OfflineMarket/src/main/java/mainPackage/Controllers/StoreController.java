package mainPackage.Controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.DeferredResult;

import jakarta.servlet.http.HttpServletResponse;
import mainPackage.MainRepository;
import mainPackage.Entities.Store;

@Controller
@RequestMapping("/store")
public class StoreController {
	
	@Autowired
	MainRepository repo;
	
	@Autowired
	HttpServletResponse response;
	
	@RequestMapping
	public String showStoreListPage(Model model, @RequestParam(required=false, name="delete") Integer id,
			@RequestParam(required=false, name="getbalance") Boolean getBalance)
	{	
		if(getBalance!=null)
			getBalanceFile();		
		
		if(id!=null)
			repo.deleteStore(id);
		
		List<Store> stores = repo.getAllStores();
		model.addAttribute("stores", stores);
		
		return "storeList";
	}
	
	@GetMapping("/edit/{id}")
	public String showAddEditStorePage(@PathVariable(name="id", required=false) Integer id, Model model)
	{
		if(id==null)
			model.addAttribute("store", new Store());
		else
		{
			Store store = repo.getStore(id);
			model.addAttribute("store", store);
		}			
		
		return "addEditStore";		
	}
	
	@PostMapping("/save")
	public String saveStore(Store store)
	{	
		if(store.getId()==0)
			repo.insertStore(store);
		else
			repo.updateStore(store);
		
		return "redirect:/store";
	}	
	
	private volatile int index=0;
	
	
	private DeferredResult<Void> getBalanceFile()
	{
		DeferredResult<Void> result = new DeferredResult<>();						
		
		List<HashMap<String, String>> balance = repo.getItemsBalance();
		
		String filename = "Balance " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) + ".txt";
		response.setHeader("Content-Disposition", "attachment; filename="+filename);
		
		try
		{
			for(HashMap<String, String> row:balance)
			{
				String str = row.get("item") + " - " + " " + row.get("store") + " - " + row.get("balance") + "\n";
				response.getWriter().write(str);
			}
			
			response.getWriter().close();
		}
		catch(IOException ex)
		{
			
		}
							
		return result;		
	}

}
