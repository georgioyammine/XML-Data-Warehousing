package application;

import javafx.beans.property.SimpleStringProperty;

public class Data {
	private SimpleStringProperty version;
	private SimpleStringProperty date;
	private SimpleStringProperty size;
	private SimpleStringProperty diffSize;
	private SimpleStringProperty similarity;
	private SimpleStringProperty status;
	private SimpleStringProperty author;
	
//	public Data(String version, String date, String size, String diffSize, String similarity, String status) {
//		this.version = new SimpleStringProperty(version);
//		this.date = new SimpleStringProperty(date);
//		this.size = new SimpleStringProperty(size);
//		this.diffSize = new SimpleStringProperty(diffSize);
//		this.similarity = new SimpleStringProperty(similarity);
//		this.status = new SimpleStringProperty(status);
//	}
	
	public Data(Version version) {
		this.version = new SimpleStringProperty(version.getNumber()+"");
		this.date = new SimpleStringProperty(version.getDateCreated()+"");
		this.size = new SimpleStringProperty(version.getSizeInBytes()+"");
		this.diffSize = new SimpleStringProperty(version.getDiffSizeInBytes()==-1?"N/A":version.getDiffSizeInBytes()+"");
		this.similarity = new SimpleStringProperty(version.getSimilarity()==-1?"N/A":version.getSimilarity()+"");
		this.status = new SimpleStringProperty(version.getStatus()?"Available":"Unavailable");
		this.author = new SimpleStringProperty(version.getOwner());
	}

	public String getVersion() {
		return version.get();
	}
	
	public String getAuthor() {
		return author.get();
	}

//	public void setVersion(String version) {
//		this.version = new SimpleStringProperty(version);
//	}

	public String getDate() {
		return date.get();
	}

//	public void setDate(String date) {
//		this.date = new SimpleStringProperty(date);
//	}

	public String getSize() {
		return size.get();
	}

//	public void setSize(String size) {
//		this.size = new SimpleStringProperty(size);
//	}

	public String getDiffSize() {
		return diffSize.get();
	}

//	public void setDiffSize(String diffSize) {
//		this.diffSize = new SimpleStringProperty(diffSize);
//	}

	public String getSimilarity() {
		return similarity.get();
	}

//	public void setSimilarity(String similarity) {
//		this.similarity = new SimpleStringProperty(similarity);
//	}

	public String getStatus() {
		return status.get();
	}

//	public void setStatus(String status) {
//		this.status = new SimpleStringProperty(status);
//	}

	@Override
	public String toString() {
		return "Data [version=" + version + ", date=" + date + ", size=" + size + ", diffSize=" + diffSize
				+ ", similarity=" + similarity + ", status=" + status + "]";
	}

}
