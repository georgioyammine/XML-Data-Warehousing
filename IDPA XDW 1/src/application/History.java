package application;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class History  implements Serializable {
	private static final long serialVersionUID = 1L; 
	
	private ArrayList<Project> recent;
	private ArrayList<Project> pinned;
	private int capacity;
	
	public History(int capacity) {
		this.recent = new ArrayList<>();
		this.pinned = new ArrayList<>();
		if(capacity>0)
			this.capacity = capacity;
		else
			this.capacity = 10;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ArrayList<Project> getRecent() {
		return new ArrayList<Project>(recent);
	}

	public ArrayList<Project> getPinned() {
		return new ArrayList<Project>(pinned);
	}
	
	public boolean pin(Project p) {
		if(pinned.contains(p))
			pinned.remove(p);
		pinned.add(0, p);
		if(pinned.size()>capacity) {
			pinned.remove(capacity);
		}
		return true;
	}
	
	public boolean unpin(Project p) {
		if(pinned.contains(p)) {
			pinned.remove(p);
			return true;
		}
		return false;
	}
	
	public void addToRecent(Project p) {
		if(recent.contains(p)) 
			recent.remove(p);
		recent.add(0, p);
		if(recent.size()>capacity) 
			recent.remove(capacity);
	}

	@SuppressWarnings("resource")
	public boolean save(String filename) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
			oos.writeObject(this);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("resource")
	public static History load(String filename) throws Exception{
		History input = null;
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
		input = (History) ois.readObject();
		return input;
	}
	
	@SuppressWarnings("unused")
	private boolean changeCapacity(int n) {
		if(n>0) {
			if(n<this.capacity) {
				recent = (ArrayList<Project>) recent.subList(0, n);
				pinned = (ArrayList<Project>) pinned.subList(0, n);
			}
			capacity = n;
			return true;
		}
		return false;
	}

}
