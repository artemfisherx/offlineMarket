package mainPackage;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import jakarta.servlet.ServletRegistration;
import jakarta.servlet.*;

public class OfflineMarket extends AbstractAnnotationConfigDispatcherServletInitializer{

	@Override
	protected Class<?> [] getRootConfigClasses()
	{
		return null;
	}
	
	@Override
	protected Class<?> [] getServletConfigClasses()
	{
		return new Class<?>[]{MainConfig.class};
	}
	
	@Override
	protected String[] getServletMappings()
	{
		return new String[] {"/*"};
	}
	
	@Override
	protected void customizeRegistration(ServletRegistration.Dynamic registration)
	{
		registration.setMultipartConfig(new MultipartConfigElement("/", 500000, 500000, 500000));
		
	}
	
	/*
	@Override
	public void onStartup(ServletContext context) throws ServletException
	{
		super.onStartup(context);
		context.setInitParameter("spring.profiles.active", "FirstStart");
	}
	*/

}
