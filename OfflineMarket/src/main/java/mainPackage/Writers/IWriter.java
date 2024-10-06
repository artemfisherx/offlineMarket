package mainPackage.Writers;

import java.io.IOException;

import jakarta.servlet.ServletContext;

public interface IWriter {
	
	void write(int id, ServletContext context) throws IOException;

}
