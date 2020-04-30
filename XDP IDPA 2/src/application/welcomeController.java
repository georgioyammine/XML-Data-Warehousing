package application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class welcomeController {

	@FXML JFXButton createProject;
	@FXML JFXButton openProject;
	@FXML JFXTextField nameId;
	@FXML JFXTextField authorId;
	@FXML Label dirPath;
	@FXML JFXPasswordField passId;
	@FXML JFXPasswordField confPassId;

	String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();

	final Clipboard clipboard = Clipboard.getSystemClipboard();
	final ClipboardContent content = new ClipboardContent();
	@FXML public void initialize() {
		dirPath.setText("");
	}

	@FXML public void chooseDirHandler() {
		File file;
		DirectoryChooser dirChooser = new DirectoryChooser();
//		FileChooser fil_chooser = new FileChooser();
//		fil_chooser.setInitialDirectory(new File(currentPath));
		dirChooser.setInitialDirectory(new File(currentPath));
//		fil_chooser.getExtensionFilters().add(new ExtensionFilter("XML", "*.xml")); // Change extension;
//		fil_chooser.
		file = dirChooser.showDialog(null);
		dirPath.setText(file.getAbsolutePath());

		if (file != null) {
			currentPath = file.getAbsoluteFile().getParent();
		}
	}

	@FXML public void handleCreateProject(ActionEvent event) throws IOException {
		if(verifyAllCorrect()) {
			if(passId.getText().equals(confPassId.getText())) {
				DataWarehousing project = new DataWarehousing(nameId.getText().trim(), authorId.getText(), passId.getText());
				String projectFolder = dirPath.getText()+File.separator+nameId.getText().trim();
				new File(projectFolder).mkdirs();
				project.save(projectFolder+File.separator+nameId.getText().trim()+".xdw");
				
				String outputFolder = dirPath.getText()+File.separator+nameId.getText().trim()+File.separator+"output";
				new File(outputFolder).mkdirs();
				String srcFolder = dirPath.getText()+File.separator+nameId.getText().trim()+File.separator+"src";
				new File(srcFolder).mkdirs();
				String diffFolder = dirPath.getText()+File.separator+nameId.getText().trim()+File.separator+"src"+File.separator+"diffs";
				new File(diffFolder).mkdirs();
				String availableFiles = dirPath.getText()+File.separator+nameId.getText().trim()+File.separator+"src"+ File.separator+"available files";
				new File(availableFiles).mkdirs();
				
				dataWarehousingController.project = project;
				dataWarehousingController.projectPath = dirPath.getText()+File.separator+nameId.getText().trim();
//				Main.classes.pop();
				Parent root = FXMLLoader.load(getClass().getResource("dataWarehousing.fxml"));
				Scene scene = new Scene(root, 800, 450);
				Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
				window.setResizable(false);
				window.setScene(scene);

		
			}
		}
	}

	private boolean verifyAllCorrect() {
		// TODO Auto-generated method stub
		return true;
	}

	@FXML public void handleOpenProject(ActionEvent event) throws IOException {
		File file;
		FileChooser fil_chooser = new FileChooser();
		fil_chooser.setInitialDirectory(new File(currentPath));
		fil_chooser.getExtensionFilters().add(new ExtensionFilter("XML Data Warehousing", "*xdw"));
		file = fil_chooser.showOpenDialog(null);

		if (file != null) {
			currentPath = file.getAbsoluteFile().getParent();
			
			DataWarehousing project = DataWarehousing.load(file.getAbsolutePath());
			dataWarehousingController.projectPath = file.getParent();
			dataWarehousingController.project = project;
			
//			Main.classes.pop();
			Parent root = FXMLLoader.load(getClass().getResource("dataWarehousing.fxml"));
			Scene scene = new Scene(root, 800, 450);
			Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
			window.setResizable(false);
			window.setScene(scene);
		}
	}

}
