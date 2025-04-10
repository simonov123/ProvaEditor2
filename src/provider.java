import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class provider {
	File file;
	String filename;
	public void init(File filen,String filenamen) {
		file=filen;
		filename=filenamen;
	
	}
	public String getcontent() throws FileNotFoundException, IOException {
		StringBuilder content=new StringBuilder();
		try (BufferedReader reader=new BufferedReader(new FileReader(filename))){
			String line="";
			while((line=reader.readLine())!=null) {
				content.append(line).append("\n");
			}
		}
		return content.toString();
	}
	public void makefile() throws IOException {
		file.createNewFile();
	}
	public void writecontent(String content) throws IOException {
		FileWriter writer=new FileWriter(filename);
		writer.write(content);
		writer.flush();
		writer.close();
	}
	public void rename(String newname) {
		File newf=new File(file.getParent(),newname);
		file.renameTo(newf);
		this.init(newf,newf.getPath());
	}
	public void delete() {
		file.delete();
		this.init(null,null);
	}
}
