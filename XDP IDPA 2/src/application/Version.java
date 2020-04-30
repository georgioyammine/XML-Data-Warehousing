package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Version  implements Serializable {
	private static final long serialVersionUID = 1L; 
	private int number;
	private String owner;
	private Date dateCreated;
	private long sizeInBytes;
	private String relativePath;
	private double similarity;
	private boolean status;
	private long diffSizeInBytes;
	
	public Version(int number, String owner, Date dateCreated, long sizeInBytes, String relativePath, double similarity,
			boolean status, long diffSizeInBytes) {

		this.number = number;
		this.owner = owner;
		this.dateCreated = dateCreated;
		this.sizeInBytes = sizeInBytes;
		this.relativePath = relativePath;
		this.similarity = similarity;
		this.status = status;
		this.diffSizeInBytes = diffSizeInBytes;
	}

	public int getNumber() {
		return number;
	}

	public String getOwner() {
		return owner;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public long getSizeInBytes() {
		return sizeInBytes;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public double getSimilarity() {
		return similarity;
	}

	public boolean getStatus() {
		return status;
	}

	public long getDiffSizeInBytes() {
		return diffSizeInBytes;
	}

	public void makeAvailable() {
		// do the patching and save the file
		// TO DO
		
		
		
		this.status = true;
	}
	public boolean makeUnavailable() {
		// delete the file
		String base = dataWarehousingController.projectPath;
		base = base+File.separator+relativePath;
		File toDelete = new File(base);
		this.status = false;
		return toDelete.delete();
	}

	





}
