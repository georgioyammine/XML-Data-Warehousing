package application;

import java.io.Serializable;

public class Project implements Serializable{
	private static final long serialVersionUID = 1L; 
	private String name, path, author;
	
	public Project(String name, String path, String author) {
		this.name = name;
		this.path = path;
		this.author = author;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}
	
	public String getAuthor() {
		return author;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Project)) {
		    return false;
		}
		
		if(this.name.equals(((Project) obj).getName())&&this.path.equals(((Project) obj).getPath())&&this.author.equals(((Project) obj).getAuthor()))
			return true;
		return false;
	}
}