package mainPackage.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mainPackage.MainRepository;
import mainPackage.Entities.Employee;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
	
	@Autowired
	MainRepository repo;
	
	@RequestMapping
	public String showEmpolyeeListPage(Model model, @RequestParam(required=false, name="delete") Integer id)
	{
		if(id!=null)
			repo.deleteEmployee(id);
		
		List<Employee> employees = repo.getAllEmployees();
		model.addAttribute("employees", employees);
		
		return "employeeList";
	}
	
	@GetMapping("/edit/{id}")
	public String showAddEditEmployeePage(@PathVariable(name="id", required=false) Integer id, Model model)
	{
		if(id==null)
			model.addAttribute("employee", new Employee());
		else
		{
			Employee employee = repo.getEmployee(id);
			model.addAttribute("employee", employee);
		}			
		
		return "addEditEmployee";		
	}
	
	@PostMapping("/save")
	public String saveEmployee(Employee employee)
	{	
		if(employee.getId()==0)
			repo.insertEmployee(employee);
		else
			repo.updateEmployee(employee);
		
		return "redirect:/employee";
	}	
	

}
