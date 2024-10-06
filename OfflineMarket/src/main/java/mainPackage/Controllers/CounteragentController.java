package mainPackage.Controllers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import mainPackage.MainRepository;
import mainPackage.Entities.Counteragent;
import mainPackage.Readers.CounteragentReader;
import mainPackage.Writers.CounteragentWriter;

@Controller
@RequestMapping("/counteragent")
@DependsOn({"MainRepository","counteragentReader"})
public class CounteragentController {
	
	@Autowired
	MainRepository repo;
	
	@Autowired
	CounteragentWriter writer;
	
	@Autowired
	ObjectFactory<CounteragentReader> reader;
	
	@RequestMapping
	public String showCounteragentListPage(Model model, @RequestParam(name="download") Optional<Integer> id, 
			@RequestParam(name="agentfile", required=false) MultipartFile file, 
			@RequestParam(name="delete", required=false) Integer delId ,HttpServletRequest request)
	{			
		
		if(id.isPresent())
		{
			try 
			{
				writer.write(id.get(), request.getServletContext());
			}
			catch(IOException ex)
			{
				System.out.println("!!!ERROR Download counteragent");
				System.out.println(ex);
			}			
		}
		
		if(delId!=null)
		{
			repo.deleteCounteragent(delId);
		}
		
		if(file!=null)
		{			
			try 
			{ 
				InputStream input = file.getInputStream();
				reader.getObject().read(input);
			}
			catch(IOException ex)
			{
				System.out.println("!!!ERROR Upload counteragent");
				System.out.println(ex);
			}
		}
		
		List<Counteragent> agents = repo.getAllCounteragents();		
		model.addAttribute("agents", agents);		
		
		return "counteragentList";
	}
	
	
	
	
	@GetMapping("/add")
	public String showAddEditCounteragentPage(Model model)
	{
		Counteragent counteragent = new Counteragent(); 
		
		model.addAttribute(counteragent);
		
		return "addEditCounteragent";
	}
	
	
	@GetMapping("/edit/{id}")
	public String showAddEditCounteragentPage(Model model, @PathVariable("id") int id)
	{			
		Counteragent counteragent = repo.getCounteragent(id);
		
		model.addAttribute(counteragent);
		
		return "addEditCounteragent";
	}
	
		
	@PostMapping("/save")
	public String saveCounteragent(@Valid Counteragent counteragent, BindingResult result, Model model)
	{
		if(result.hasErrors())
		{
			model.addAttribute(counteragent);
			return "addEditCounteragent";
		}
		
		if(counteragent.getId()==0)
			repo.insertCounteragent(counteragent);
		else
			repo.updateCounteragent(counteragent);
		
		return "redirect:/counteragent";
	}	
	
	// AUTOLOADER
	/* 
	@PostConstruct
	public void initLoad()
	{
		try
		{
			Path path = Path.of("c:/testagent.txt");
			InputStream input = Files.newInputStream(path, StandardOpenOption.READ);			
			reader.getObject().read(input);
		}
		catch(IOException ex)
		{
			System.out.println("!!!AUTOLOAD Error");
			System.out.println(ex);
		}
			
	}	
	*/
	

}
