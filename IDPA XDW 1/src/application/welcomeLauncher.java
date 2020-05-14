package application;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Stack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class welcomeLauncher extends Application {
	static Stack<String> classes = new Stack<>();
	static String launchName = "welcomeScreen.fxml";
	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource(launchName));

		Scene scene = new Scene(root);
//		new JMetro(scene, Style.DARK);
		stage.setScene(scene);
		if(launchName.equals("loadingScreen.fxml"))
			stage.initStyle(StageStyle.UNDECORATED);
//		else
//			stage.setResizable(false);
		stage.setTitle("XDW - XML Data Warehousing ");
		// stage.getIcons().add(new Image("iconN (1).png"));
		stage.getIcons().add(new Image("icon-main@3x.png"));
		// stage.getIcons().add(new Image("icon-main@2x.png"));
		// stage.getIcons().add(new Image("icon-main.png"));
		// stage.getIcons().add(new Image(Main.class.getResourceAsStream( "icon.ico"
		// )));
		stage.show();
		

	}

	public static void main(String[] args) {
		if(args.length==0)
			launch(args);
		else {
		if(args[0].isEmpty())
			launch(args);
		else {
		try {
			System.out.println(Arrays.toString(args));
			StringBuilder sb = new StringBuilder();
			for(String str : args)
				sb.append(str+" ");
			sb.delete(sb.length()-1, sb.length());
			handleOpenProject(sb.toString());
		}
		catch(Exception e) {
			e.printStackTrace();
			launch(args);
		}
		}
		}
	}
	public static void handleOpenProject(String path) throws IOException, URISyntaxException{
		File file = new File(path);
		History history;
//		dirPath.setText("");
		try {
			String currentPath = welcomeLauncher.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			currentPath = currentPath.substring(0,currentPath.lastIndexOf("/"));
			String filename = currentPath.replace("/", File.separator)+ File.separator + "history.cfg";
			System.out.println(filename);
			history = History.load(filename);
		} catch (Exception e) {
//			ex.printStackTrace();
			history = new History(20);
		}
		if (file != null) {
			String currentPath = file.getAbsoluteFile().getParent();
			System.out.println(path);
			DataWarehousing project = DataWarehousing.load(file.getAbsolutePath());
			controllLoading.projectDir = file.getParent();
			controllLoading.project = project;
			history.addToRecent(new Project(project.getName(),file.getParent(), project.getOwner()));
			try {
				currentPath = welcomeLauncher.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
				currentPath = currentPath.substring(0,currentPath.lastIndexOf("/"));
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String filename = currentPath.replace("/", File.separator)+ File.separator + "history.cfg";
			history.save(filename);
			launchName = "loadingScreen.fxml";
			launch(null);
			// Main.classes.pop();
//			new JMetro(scene, Style.DARK);

		}
	}
}
