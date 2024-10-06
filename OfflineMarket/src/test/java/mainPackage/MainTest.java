package mainPackage;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.CookieResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.context.WebApplicationContext;

import mainPackage.Controllers.CounteragentController;
import mainPackage.Controllers.OrderController;
import mainPackage.Entities.Employee;
import mainPackage.Entities.Employee.TZone;
import mainPackage.Entities.Order;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes= {MainConfig.class})
@WebAppConfiguration
//@Sql(statements= {"INSERT INTO employees(name,surname,position,tzone) VALUES('test','test','test','test')"})
public class MainTest {
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	MockHttpSession session;
	
	@Test
	public void testOrder() throws Exception 
	{
		session.setAttribute("user", "Alex");
		
		WebTestClient client = MockMvcWebTestClient.bindToApplicationContext(context).configureClient().build();
		client.get().uri("/order").exchange().expectStatus().isOk();
	}
	
	@Test
	public void testEmployee() throws Exception
	{
		MultiValueMap<String, String> attrs = new HttpHeaders();
		
		attrs.add("id", "0");
		attrs.add("name", "Test name");
		attrs.add("surname", "Test surname");
		attrs.add("position", "Test position");		
		attrs.add("tzone", "Miami");		
		
		//WebTestClient client = MockMvcWebTestClient.bindToApplicationContext(context).build();				
		//client.post().uri(b->b.path("/employee/save").build(attrs))
		//.exchange().expectStatus().is3xxRedirection();
		
		//MockMvc mock = webAppContextSetup(context).build();
		//mock.perform(post("/employee/save").params(attrs)).andDo(print());
	}
	
	@Test
	public void testCounteragent() throws Exception
	{
		MockMvc mock = MockMvcBuilders.webAppContextSetup(context).build();
		
		mock.perform(MockMvcRequestBuilders.get("/counteragent"))
			.andExpect(MockMvcResultMatchers.handler().handlerType(CounteragentController.class));
		
		MultiValueMap<String, String> params = new HttpHeaders();
		params.add("id", "0");
		params.add("inn", "123456789");
		params.add("name", "Bob");
		params.add("address.country", "Russia");
		params.add("address.city", "Moscow");
		params.add("address.street", "Lenina");
		params.add("address.building", "1A");
		
		
		mock.perform(post("/counteragent/save").params(params)).andDo(print());
	}

}
