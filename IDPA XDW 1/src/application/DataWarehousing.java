package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.annotation.XmlElementDecl;

import org.w3c.dom.Element;
import org.w3c.dom.Node;



public class DataWarehousing  implements Serializable {
	private static final long serialVersionUID = 1L; 
	
	private String name;
	private String owner;
	private String password;
	ArrayList<Version> versions;
	
	
	public DataWarehousing(String name, String owner, String password) {
		super();
		this.name = name;
		this.owner = owner;
		this.password = getPass(password);
		versions = new ArrayList<>();
	}

	public boolean addNewVersion(String owner, String pathOfNewVersion) throws Exception {
		Date currentDate = new Date(System.currentTimeMillis());
		pathOfNewVersion.replaceFirst("^/(.:/)", "$1");
		File file = new File(pathOfNewVersion);
		String base = dataWarehousingController.projectPath;
		
		if(versions.isEmpty()) {
			String path =  base +  File.separator + "src"+ File.separator+"available files";
			new File(path).mkdirs();
//			Paths.get(ClassLoader.getSystemResource(file.getAbsolutePath()).toURI());
//			String mainPath = Paths.get(uri).toString();
//			Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(path));
			Files.copy(Paths.get(file.getAbsolutePath()),
					Paths.get(path+File.separator+name+"_v"+(versions.size()+1)+".xml"));
//			Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(path));
			
			File copied = new File(path+File.separator+name+"_v"+(versions.size()+1)+".xml");

			String relativePath = File.separator + "src"+ File.separator+"available files"+File.separator+name+"_v1"+".xml";
			
			versions.add(new Version(1, owner,currentDate, copied.length(), relativePath, -1, true, -1));
			
			
		}
		else {
			
			
		String prevFile = base+File.separator+versions.get(versions.size()-1).getRelativePath();
		
		String path =  base +  File.separator + "src"+ File.separator+"available files";
		ArrayList<Object> TED = XMLDiffAndPatch.getDiffNode(pathOfNewVersion, prevFile, true);
		if((double)TED.get(1)==1)
			return false;
		new File(path).mkdirs();
		
		Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(path+File.separator+name+"_v"+(versions.size()+1)+".xml"));
		File copied = new File(path+File.separator+name+"_v"+(versions.size()+1)+".xml");
//		String relativePath = File.separator + "src"+ File.separator+"available files"+File.separator+name+"_v"+(versions.size()+1);
		
		
//		String newFile = base+relativePath;
		// get Diff with previous
		String diffFolder = base +  File.separator + "src"+ File.separator+"diffs";
		new File(diffFolder).mkdirs();
		String relativePath = "src"+ File.separator+"available files" + File.separator+name+"_v"+(versions.size()+1)+".xml";
		
		
		Node diff = (Node) TED.get(0);
		String diffPath = XMLDiffAndPatch.WriteXMLtoFile2(diff, "Diff_"+(versions.size()+1)+"to"+(versions.size())+".xml", diffFolder, true);
		versions.add(new Version(versions.size()+1, owner,currentDate, copied.length(), relativePath, ((double) TED.get(1))*100, true, new File(diffPath).length()));
		
		///////////////////////////
		versions.get(versions.size()-2).makeUnavailable();
		}
		return true;
	}



	private String getPass(String password) {
		// TODO Auto-generated method stub
		
		
		return password;
	}
	
//	ArrayList<Version> versions;
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
	public static DataWarehousing load(String filename) {
		DataWarehousing input = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
			input = (DataWarehousing) ois.readObject();
		} catch (Exception e) {
		}
		return input;
	}

	public String getName() {
		return name;
	}

	public String getOwner() {
		return owner;
	}

	public int getNumberOfVersions() {
		return versions.size();
	}
	
	public boolean getThisVersion(Version version) throws Exception {
		Node node = getThisVersionAsNode(version);
		String base = dataWarehousingController.projectPath;
		String outputFolder = base +  File.separator + "output";
		String diffPath = XMLDiffAndPatch.WriteXMLtoFile2(node, name+"_v"+(version.getNumber())+".xml", outputFolder, true);
		System.out.println(diffPath);
		return true;
	}
	
	public Node getThisVersionAsNode(Version version) throws Exception {
		// TODO Auto-generated method stub
		Version closestAvailable = getClosestAvailable(version);
		String base = dataWarehousingController.projectPath;
		String diffFolder = base +  File.separator + "src"+ File.separator+"diffs";
		
		Node node = XMLDiffAndPatch.getRootNodeFromFile(base +File.separator+ closestAvailable.getRelativePath());
		if(closestAvailable.getNumber()>version.getNumber()) {
			for(int i = closestAvailable.getNumber(); i>version.getNumber();i--) {
				String diffFile = diffFolder+File.separator+"Diff_"+i+"to"+(i-1)+".xml";
				node = XMLDiffAndPatch.applyPatchXMLNode(node, diffFile, false);
			}
		}
		String diffFile = diffFolder+File.separator+"Diff_"+(version.getNumber()+1)+"to"+(version.getNumber())+".xml";
		Node ESroot = XMLDiffAndPatch.getRootNodeFromFile(diffFile);
		String targetHash = ((Element) ESroot).getElementsByTagName("Patched_File").item(0).getAttributes().item(0)
				.getNodeValue();
		
		File tmpInput2 = File.createTempFile("patch1", ".tmp");
		tmpInput2.deleteOnExit();
		XMLDiffAndPatch.WriteXMLtoFile(node, tmpInput2.getAbsolutePath(), true,true);
		long crc2 = XMLDiffAndPatch.checksumBufferedInputStream(tmpInput2);
		String hashInput2 = Long.toHexString(crc2);
		tmpInput2.delete();
		

		if (!targetHash.equals(hashInput2)) {
			System.out.println("Wrong Result Expected: Hash checksum does not match");
		} else {
			System.out.println("Patch successful, hash checksum matches!");
		}
		
		return node;
	}
	

	private Version getClosestAvailable(Version version) {
		// TODO Auto-generated method stub
		return versions.get(versions.size()-1);
	}

}
