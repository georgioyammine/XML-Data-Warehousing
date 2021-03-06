package com.georgioyammine.data;

import java.io.File;
import java.util.Date;

import com.georgioyammine.classes.Project;

import javafx.beans.property.SimpleStringProperty;

public class ProjectData{
	private SimpleStringProperty name, path, author, dateMod;

	public ProjectData(Project p) {
		this.name = new SimpleStringProperty(p.getName());
		this.path = new SimpleStringProperty(p.getPath());
		this.author = new SimpleStringProperty(p.getAuthor());
		File file = new File(path.get()+File.separator+name.get()+".xdw");
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