package mainPackage.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mainPackage.MainRepository;
import mainPackage.Entities.Counteragent;
import mainPackage.Entities.Item;
import mainPackage.Entities.Receipt;
import mainPackage.Entities.Store;

@Controller
@RequestMapping("/receipt")
public class ReceiptController {
	
	@Autowired
	MainRepository repo;	
	
	@GetMapping
	public String showReceiptList(@RequestParam(required=false, name="delete") Integer id)
	{	
		if(id!=null)
			repo.deleteReceipt(id);
		return "receiptList";
	}
	
	@GetMapping("/edit/{id}")
	public String showAddEditReceiptPage(@PathVariable(name="id", required=false) Integer id, Model model)	
	{
		Receipt receipt;
		if(id!=null)
			receipt = repo.getReceipt(id);
		else
			receipt = new Receipt();
		
		model.addAttribute("receipt", receipt);
		
		return "addEditReceipt";	
	}
	
	@PostMapping("/save")
	public String saveReceipt(Receipt receipt, BindingResult result)
	{
		if(result.hasErrors())
			result.getAllErrors().forEach(o->System.out.println(o.getDefaultMessage()));
				
		if(receipt.getId()==0)
			repo.insertReceipt(receipt);
		else
			repo.updateReceipt(receipt);
				
		return "redirect:/receipt";
		
	}
	
	@ModelAttribute("items")
	private List<Item> getAllItems()
	{
		return repo.getAllItems();
	}
	
	@ModelAttribute("stores")
	private List<Store> getAllStores()
	{
		return repo.getAllStores();
	}
	
	@ModelAttribute("suppliers")
	private List<Counteragent> getAllAgents()
	{
		return repo.getAllCounteragents();
	}
	
	@ModelAttribute("receipts")
	private List<Receipt> getAllReceipts()
	{
		return repo.getAllReceipts();
	}

}
