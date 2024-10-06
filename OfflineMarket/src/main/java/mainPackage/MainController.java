package mainPackage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import mainPackage.Entities.Item;

@Controller
@RequestMapping
public class MainController {
	
	@GetMapping
	public String showMainPage()
	{
		return "index";
	}	
	
}
