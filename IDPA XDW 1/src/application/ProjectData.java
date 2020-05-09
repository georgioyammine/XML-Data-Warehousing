package application;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import javafx.beans.property.SimpleStringProperty;

public class ProjectData implements Serializable{
	private static final long serialVersionUID = 1L; 
	private SimpleStringProperty name, path, author, dateMod;
	
	public ProjectData(Project p) {
		this.name = new SimpleStringProperty(p.getName());
		this.path = new SimpleStringProperty(p.getPath());
		this.author = new SimpleStringProperty(p.getAuthor());
		File file = new File(path.get());			
		Date date = new Date(file.lastModified());
		this.dateMod = new SimpleStringProperty(date.toString());
	}

	@Override
	public String toString() {
		return "ProjectData [name=" + name + ", path=" + path + ", author=" + author + ", dateMod=" + dateMod + "]";
	}

	public String getName() {
		return name.get();
	}

	public String getPath() {
		return path.get();
	}
	
	public String getAuthor() {
		return author.get();
	}

	public String getLastModified() {
		return dateMod.get();
	}
}