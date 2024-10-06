package mainPackage.Writers;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import mainPackage.MainRepository;
import mainPackage.Entities.Counteragent;
import mainPackage.Entities.Item;

public class CounteragentWriter implements IWriter{
	
	@Autowired
	private MainRepository repo;
	
	@Value("${agent.folder:OfflineMarketAgentDefaultFolder}")
	String agentFolder;	
	
	@Autowired
	private ServletContext context;
	
	private LocalDateTime dt = LocalDateTime.now();
	
	final private String dir = "counteragents";
	
	
	public void write(int id, ServletContext context) throws IOException
	{	
		Counteragent agent = repo.getCounteragent(id);
		
		String fileName = dt.format(DateTimeFormatter.ofPattern("yMdHms"));		

		String dirPath = System.getProperty("catalina.home") + "\\webapps\\" + agentFolder;	
		
		//String dirPath = context.getRealPath(dir);
		String dirFile = dirPath + "\\" + fileName + ".txt";
		
		if(!Files.exists(Path.of(dirPath)))
		{
			new File(dirPath).mkdirs();			
		}	
		
		new File(dirFile).createNewFile();
		
		
		try(PrintWriter writer = new PrintWriter(dirFile))
		{
			writer.println(agent.getId());
			writer.println(agent.getInn());
			writer.println(agent.getName());
			writer.println(agent.getAddress());			
		}
	}
}
