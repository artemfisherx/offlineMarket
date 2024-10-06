package mainPackage.Controllers;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import mainPackage.MainRepository;
import mainPackage.AdditionalEntities.ItemStore;
import mainPackage.Entities.Counteragent;
import mainPackage.Entities.Employee;
import mainPackage.Entities.Item;
import mainPackage.Entities.Order;
import mainPackage.Entities.Store;
import mainPackage.Formatters.CounteragentFormatter;
import mainPackage.Formatters.EmployeeFomatter;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	MainRepository repo;	
	
	@GetMapping
	public String showOrderListPage(@RequestParam(name="delete", required=false) Integer id, Model model)
	{
		if(id!=null)
			repo.deleteOrder(id);
		
		List<Order> orders = repo.getAllOrders();
		model.addAttribute("orders", orders);
		return "orderList";
	}
	
	@GetMapping("/edit/{id}")
	public String showAddEditOrderPage(@PathVariable(required=false, name="id") Integer id, Model model)
	{
		Order order;
		
		if(id==null)
			order = new Order();
		else
			order = repo.getOrder(id);
		
		
		System.out.println("!!!DateTime:"+order.getDatetime());
		
		List<Employee> employees = repo.getAllEmployees();
		List<Counteragent> agents = repo.getAllCounteragents();
		List<Item> items = repo.getAllItems();
		List<Store> stores = repo.getAllStores();
		
		model.addAttribute("order", order);
		model.addAttribute("employees", employees);
		model.addAttribute("agents", agents);
		model.addAttribute("items", items);
		model.addAttribute("stores", stores);
		
		return "addEditOrder";
	}
	
	@PostMapping("/save")
	@ResponseBody
	public void save(
			@RequestParam("id") int id,
			@RequestParam("datetime") String datetime,
			@RequestParam("seller") int sellerId,
			@RequestParam("client") int clientid,
			@RequestParam("ids") List<Integer> ids,
			@RequestParam("items") List<Integer> items,
			@RequestParam("stores") List<Integer> stores,
			@RequestParam("count") List<Integer> counts			
			)
	{
		LocalDateTime ldt = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
		ZonedDateTime zdt = ZonedDateTime.of(ldt, ZoneId.systemDefault());
		Employee seller = repo.getEmployee(sellerId);
		Counteragent client = repo.getCounteragent(clientid);
		
		List<ItemStore> positions = new ArrayList<>();
		
		for(int i=0;i<items.size();i++)
		{
			Item item = repo.getItem(items.get(i));
			Store store = repo.getStore(stores.get(i));
			int posId = ids.get(i);
			int count = counts.get(i);
			
			ItemStore pos = new ItemStore(posId, item, store, count);
			positions.add(pos);
		}
		
		Order order = new Order();
		order.setId(id);
		order.setSeller(seller);
		order.setClient(client);
		order.setPositions(positions);
		order.setDatetime(zdt);
		
		if(id==0)
			repo.insertOrder(order);
		else 
			repo.updateOrder(order);
		
	}
	

}
