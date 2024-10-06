package mainPackage;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.PathResource;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.resource.CachingResourceTransformer;
import org.springframework.web.servlet.resource.ContentVersionStrategy;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
import org.springframework.web.servlet.resource.VersionResourceResolver;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import mainPackage.Controllers.CounteragentController;
import mainPackage.Controllers.EmployeeController;
import mainPackage.Controllers.ItemHandler;
import mainPackage.Controllers.OrderController;
import mainPackage.Controllers.ReceiptController;
import mainPackage.Controllers.StoreController;
import mainPackage.Converters.CounteragentHttpMessageConverter;
import mainPackage.Converters.StringToTZoneConverter;
import mainPackage.Entities.Counteragent;
import mainPackage.Events.TransactionEventListener;
import mainPackage.Events.TransactionEventPublisher;
import mainPackage.Formatters.CounteragentFormatter;
import mainPackage.Formatters.EmployeeFomatter;
import mainPackage.Formatters.ItemFormatter;
import mainPackage.Formatters.StoreFormatter;
import mainPackage.Formatters.ZonedDateTimeAnnotationFormatterFactory;
import mainPackage.Formatters.ZonedDateTimeFormatter;
import mainPackage.Readers.CounteragentReader;
import mainPackage.Writers.CounteragentWriter;

@Configuration
@ComponentScan
@EnableTransactionManagement(proxyTargetClass=true)
@PropertySource("classpath:params.properties")
public class MainConfig extends WebMvcConfigurationSupport
{
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Value("${database.url}")
	private String databaseUrl;
	
	@Value("${database.username}")
	private String databaseUsername;
	
	@Value("${database.password}")
	private String databasePassword;	
	
	@Value("${item.folder:OfflineMarketItemDefaultFolder}")
	private String itemFolder;
	
	@Autowired
	MainRepository repo;
	
	@Bean
	public SpringResourceTemplateResolver templateResolver(){
	    // SpringResourceTemplateResolver automatically integrates with Spring's own
	    // resource resolution infrastructure, which is highly recommended.
	    SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
	    templateResolver.setApplicationContext(this.applicationContext);
	    templateResolver.setPrefix("/WEB-INF/views/");
	    templateResolver.setSuffix(".html");
	    // HTML is the default value, added here for the sake of clarity.
	    templateResolver.setTemplateMode(TemplateMode.HTML);
	    // Template cache is true by default. Set to false if you want
	    // templates to be automatically updated when modified.
	    templateResolver.setCacheable(false);
	    return templateResolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine(){
	    // SpringTemplateEngine automatically applies SpringStandardDialect and
	    // enables Spring's own MessageSource message resolution mechanisms.
	    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
	    templateEngine.setTemplateResolver(templateResolver());
	    // Enabling the SpringEL compiler with Spring 4.2.4 or newer can
	    // speed up execution in most scenarios, but might be incompatible
	    // with specific cases when expressions in one template are reused
	    // across different data types, so this flag is "false" by default
	    // for safer backwards compatibility.
	    templateEngine.setEnableSpringELCompiler(true);
	    return templateEngine;
	}
	
	@Bean
	public ThymeleafViewResolver viewResolver(){
	    ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
	    viewResolver.setTemplateEngine(templateEngine());
	    // NOTE 'order' and 'viewNames' are optional
	    viewResolver.setOrder(1);
	    //viewResolver.setViewNames(new String[] {".html", ".xhtml"});
	    return viewResolver;
	}
	
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry)
	{
		String dirPath = System.getProperty("catalina.home") + "\\webapps\\" + itemFolder + "\\";	
		
		VersionResourceResolver resolver = new VersionResourceResolver();
		resolver.addContentVersionStrategy("/**");
		
		registry.addResourceHandler("/styles/**","/images/**", "/scripts/**")
		.addResourceLocations("/resources/styles/", "/resources/images", "file:///"+dirPath, "/resources/scripts/")
		.setCachePeriod(0)
		.resourceChain(true)
		.addResolver(resolver);		
		
	}
	
	@Bean
	public ResourceUrlEncodingFilter resourceUrlEncodingFilter() // не работает!!
	{
		return new ResourceUrlEncodingFilter();
	}	
	
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer()
	{
		PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
		configurer.setLocations(new ClassPathResource("dbconfig.properties"));
		
		
		return configurer;
	}
	
	
	// Repository settings
	
	@Bean
	public DataSource dataSource()
	{		
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
    	dataSource.setDriverClassName("org.postgresql.Driver");
    	dataSource.setUrl(databaseUrl);
    	dataSource.setUsername(databaseUsername);
    	dataSource.setPassword(databasePassword);
    	return dataSource;

	}
	
	@Bean
	public PlatformTransactionManager getTransactionManager()
	{
		return new JdbcTransactionManager(dataSource());
	}
	
	// transactions
	
	@Bean
	public TransactionEventPublisher transactionEventPublisher()
	{
		return new TransactionEventPublisher();
	}
	
	@Bean 
	public TransactionEventListener transactionEventListener()
	{
		return new TransactionEventListener();
	}
	
	
	// i18
	
	@Bean
	public MessageSource messageSource()
	{
		ReloadableResourceBundleMessageSource msg = new ReloadableResourceBundleMessageSource();
		msg.setBasename("classpath:captions");
		msg.setCacheMillis(1);
		
		return msg;
	}
	
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor()
	{
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry)
	{
		registry.addInterceptor(localeChangeInterceptor());
	}
	
	//mapping
	
	
	@Autowired
	public void setHandlerMapping(RequestMappingHandlerMapping mapping, StoreController storeCont, 
			EmployeeController empCont, OrderController orderCont, ReceiptController receiptCont) throws NoSuchMethodException 
	{	
		RequestMappingInfo storeInfo = RequestMappingInfo.paths("/store/add")
				.methods(RequestMethod.GET).build();
		Method storeMethod = storeCont.getClass().getMethod("showAddEditStorePage", Integer.class, Model.class);
		mapping.registerMapping(storeInfo, storeCont, storeMethod);	
		
		RequestMappingInfo empInfo = RequestMappingInfo.paths("/employee/add")
				.methods(RequestMethod.GET).build();
		Method empMethod = empCont.getClass().getMethod("showAddEditEmployeePage", Integer.class, Model.class);
		mapping.registerMapping(empInfo, empCont, empMethod);
		
		RequestMappingInfo orderInfo = RequestMappingInfo.paths("/order/add")
				.methods(RequestMethod.GET).build();
		Method orderMethod = orderCont.getClass().getMethod("showAddEditOrderPage", Integer.class, Model.class);
		mapping.registerMapping(orderInfo, orderCont, orderMethod);		
		
		RequestMappingInfo receiptInfo = RequestMappingInfo.paths("/receipt/add")
				.methods(RequestMethod.GET).build();
		Method receiptMethod = receiptCont.getClass().getMethod("showAddEditReceiptPage", Integer.class, Model.class);
		mapping.registerMapping(receiptInfo, receiptCont, receiptMethod);	
		
	}
	

	public MultipartResolver multipartResolver()
	{
		return new StandardServletMultipartResolver();		
	}
	
	@Bean
	@Scope(value="prototype", proxyMode=ScopedProxyMode.TARGET_CLASS)
	public CounteragentWriter counteragentWriter()
	{
		return new CounteragentWriter();
	}
	
	@Bean
	@Scope("prototype")
	public CounteragentReader counteragentReader()
	{
		return new CounteragentReader();
	}
	
	@Bean
	@Profile("FirstStart")
	public DataSourceInitializer dataSourceInitializer()
	{
		DataSource source = dataSource();
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("initDb/counteragents.sql"));
		populator.addScript(new ClassPathResource("initDb/stores.sql"));
		populator.addScript(new ClassPathResource("initDb/employees.sql"));
		populator.addScript(new ClassPathResource("initDb/items.sql"));
		populator.addScript(new ClassPathResource("initDb/orders.sql"));
		
		populator.addScript(new ClassPathResource("initDb/order_positions.sql"));
		populator.addScript(new ClassPathResource("initDb/before_insert_update_order_positions_func.sql"));
		populator.addScript(new ClassPathResource("initDb/before_insert_update_order_positions_trigger.sql"));
		populator.addScript(new ClassPathResource("initDb/after_delete_order_positions_func.sql"));
		populator.addScript(new ClassPathResource("initDb/after_delete_order_positions_trigger.sql"));
		
		populator.addScript(new ClassPathResource("initDb/items_stores.sql"));
		
		populator.addScript(new ClassPathResource("initDb/receipts.sql"));		
		populator.addScript(new ClassPathResource("initDb/after_insert_receipts_func.sql"));
		populator.addScript(new ClassPathResource("initDb/after_insert_receipts_trigger.sql"));
		populator.addScript(new ClassPathResource("initDb/after_update_receipts_func.sql"));
		populator.addScript(new ClassPathResource("initDb/after_update_receipts_trigger.sql"));
		populator.addScript(new ClassPathResource("initDb/after_delete_receipts_func.sql"));
		populator.addScript(new ClassPathResource("initDb/after_delete_receipts_trigger.sql"));
		
		populator.addScript(new ClassPathResource("initDb/movement_and_balance_by_date.sql"));
		populator.addScript(new ClassPathResource("initDb/movement_by_date.sql"));
		populator.addScript(new ClassPathResource("initDb/sales_by_employee.sql"));
		populator.addScript(new ClassPathResource("initDb/sales_by_period.sql"));
		
		populator.addScript(new ClassPathResource("initDb/add_stores.sql"));
		
		DataSourceInitializer initializer = new DataSourceInitializer();
		initializer.setDatabasePopulator(populator);
		initializer.setDataSource(source);		
		
		return initializer;		
	}
		
	@Bean
	public RouterFunction<ServerResponse> itemRouter()
	{
		ItemHandler handler = new ItemHandler(repo, itemFolder);
		return RouterFunctions.route().path("/item", p-> p
				.GET("/add", handler::showAddEditItemPage)
				.GET("/edit/{id}", handler::showAddEditItemPage)				
				.GET(handler::showItemListPage)
				.POST("/save", handler::saveItem)
				)
				.build();		
	}
	
	
	@Override
	protected void addFormatters(FormatterRegistry registry)
	{		
		registry.addFormatter(new CounteragentFormatter(repo));
		registry.addConverter(new StringToTZoneConverter());
		registry.addFormatter(new EmployeeFomatter(repo));
		registry.addFormatter(new ItemFormatter(repo));
		registry.addFormatter(new StoreFormatter(repo));
		//registry.addFormatter(new ZonedDateTimeFormatter());
		registry.addFormatterForFieldAnnotation(new ZonedDateTimeAnnotationFormatterFactory());
	}	

}
