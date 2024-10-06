package mainPackage.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import mainPackage.MainRepository;
import mainPackage.Entities.Employee;
// НЕ ДОДЕЛАЛ. отчеты в sql реализованы в виде функций и лежат в resources/initDb в виде отдельных файлов
//initDb/movement_and_balance_by_date.sql
//initDb/movement_by_date.sql
//initDb/sales_by_employee.sql
//initDb/sales_by_period.sql
@Controller
@RequestMapping("/report")
public class ReportController {
	
	@Autowired
	MainRepository repo;
	
	@GetMapping
	public String showReportsPage()
	{
		return "reports";
	}
	
	@ModelAttribute("employees")
	public List<Employee> getAllEmployees()
	{
		return repo.getAllEmployees();
	}

}
