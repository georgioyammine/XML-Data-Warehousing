package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.event.ListSelectionEvent;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.AnchorPane;

public class welcomeController {

	@FXML
	JFXButton createProject;
	@FXML
	JFXButton openProject;
	@FXML
	JFXTextField nameId;
	@FXML
	JFXTextField authorId;
	@FXML
	Label dirPath;
	@FXML
	JFXPasswordField passId;
	@FXML
	JFXPasswordField confPassId;
	int historyCapacity = 20;
	History history;
	String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();

	final Clipboard clipboard = Clipboard.getSystemClipboard();
	final ClipboardContent content = new ClipboardContent();
	@FXML TableView recentTable;
	@FXML TableColumn RecentNameCol;
	@FXML TableColumn RecentAuthorCol;
	@FXML TableColumn RecentDirCol;
	@FXML TableColumn RecentModCol;
	@FXML TableView pinnedTable;
	@FXML TableColumn PinnedNameCol;
	@FXML TableColumn PinnedAuthorCol;
	@FXML TableColumn PinnedDirCol;
	@FXML TableColumn PinnedModCol;
	@FXML TableColumn PinnedUnpinCol;
	@FXML TableColumn RecentPinCol;
	@FXML AnchorPane recentPane;

	@FXML
	public void initialize() {
		passId.setVisible(false);
		confPassId.setVisible(false);
		dirPath.setText("");
		errorMessage.setText("");
		try {
			String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
			String filename = currentPath+ File.separator + "history.cfg";
			history = History.load(filename);
		} catch (Exception e) {
//			ex.printStackTrace();
			history = new History(historyCapacity);
		}

		final History historyFinal = history;

		RecentNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		RecentAuthorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
		RecentDirCol.setCellValueFactory(new PropertyValueFactory<>("path"));
		RecentModCol.setCellValueFactory(new PropertyValueFactory<>("LastModified"));

		PinnedNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		PinnedAuthorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
		PinnedDirCol.setCellValueFactory(new PropertyValueFactory<>("path"));
		PinnedModCol.setCellValueFactory(new PropertyValueFactory<>("LastModified"));

		Callback<TableColumn<ProjectData, Void>, TableCell<ProjectData, Void>> cellFactoryPin = new Callback<TableColumn<ProjectData, Void>, TableCell<ProjectData, Void>>() {
			@Override
			public TableCell<ProjectData, Void> call(final TableColumn<ProjectData, Void> param) {
				final TableCell<ProjectData, Void> cell = new TableCell<ProjectData, Void>() {

					private final Button btn = new Button("",new ImageView(new Image("pin.png", 20, 20, true, true)));

					{
						btn.setOnAction((ActionEvent event) -> {
							ProjectData project = getTableView().getItems().get(getIndex());
							historyFinal.pin(new Project(project.getName(), project.getPath(), project.getAuthor()));
							updatePinned();
						});
					}
					@Override
					public void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							setGraphic(btn);
						}
					}

				};
				return cell;
			}
		};
		RecentPinCol.setCellFactory(cellFactoryPin);

		Callback<TableColumn<ProjectData, Void>, TableCell<ProjectData, Void>> cellFactoryUnpin = new Callback<TableColumn<ProjectData, Void>, TableCell<ProjectData, Void>>() {
			@Override
			public TableCell<ProjectData, Void> call(final TableColumn<ProjectData, Void> param) {
				final TableCell<ProjectData, Void> cell = new TableCell<ProjectData, Void>() {

					private final Button btn = new Button("",new ImageView(new Image("unpin.png", 20, 20, true, true)));
					{
						btn.setOnAction((ActionEvent event) -> {
							ProjectData project = getTableView().getItems().get(getIndex());
							historyFinal.unpin(new Project(project.getName(), project.getPath(), project.getAuthor()));
							updatePinned();
						});
					}
					@Override
					public void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							setGraphic(btn);
						}
					}

				};
				return cell;
			}
		};
		PinnedUnpinCol.setCellFactory(cellFactoryUnpin);

		ArrayList<ProjectData> recentArr = new ArrayList<>();
		for(Project p:history.getRecent()) {
			recentArr.add(new ProjectData(p));
		}
		ObservableList<ProjectData> recentList = FXCollections.observableArrayList(recentArr);
		recentTable.setItems(recentList);

		updatePinned();
		pinnedTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    if (newSelection != null) {
		        try {
					handleOpenProject(((ProjectData)newSelection).getPath()+File.separator+((ProjectData)newSelection).getName()+".xdw");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});
		recentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    if (newSelection != null) {
		    	try {

					handleOpenProject(((ProjectData)newSelection).getPath()+File.separator+((ProjectData)newSelection).getName()+".xdw");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});
		history = historyFinal;
	}
	public void updatePinned() {
		ArrayList<ProjectData> pinnedArr = new ArrayList<>();
		for(Project p:history.getPinned()) {
			pinnedArr.add(new ProjectData(p));
		}
		ObservableList<ProjectData> pinnedList = FXCollections.observableArrayList(pinnedArr);
		pinnedTable.setItems(pinnedList);
		String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
		String filename = currentPath+ File.separator + "history.cfg";
		history.save(filename);
	}

	@FXML
	public void chooseDirHandler() {
		File file;
		DirectoryChooser dirChooser = new DirectoryChooser();
		// FileChooser fil_chooser = new FileChooser();
		// fil_chooser.setInitialDirectory(new File(currentPath));
		dirChooser.setInitialDirectory(new File(currentPath));
		// fil_chooser.getExtensionFilters().add(new ExtensionFilter("XML", "*.xml"));
		// // Change extension;
		// fil_chooser.
		file = dirChooser.showDialog(null);
		dirPath.setText(file.getAbsolutePath());

		if (file != null) {
			currentPath = file.getAbsoluteFile().getParent();
		}
	}

	@FXML
	public void handleCreateProject(ActionEvent event) throws IOException {
		if (verifyAllCorrect()) {

			DataWarehousing project = new DataWarehousing(nameId.getText().trim(), authorId.getText(),
					passId.getText());
			String projectFolder = dirPath.getText() + File.separator + nameId.getText().trim();
			new File(projectFolder).mkdirs();
			project.save(projectFolder + File.separator + nameId.getText().trim() + ".xdw");

			String outputFolder = dirPath.getText() + File.separator + nameId.getText().trim() + File.separator
					+ "output";
			new File(outputFolder).mkdirs();
			String srcFolder = dirPath.getText() + File.separator + nameId.getText().trim() + File.separator + "src";
			new File(srcFolder).mkdirs();
			String diffFolder = dirPath.getText() + File.separator + nameId.getText().trim() + File.separator + "src"
					+ File.separator + "diffs";
			new File(diffFolder).mkdirs();
			String availableFiles = dirPath.getText() + File.separator + nameId.getText().trim() + File.separator
					+ "src" + File.separator + "available files";
			new File(availableFiles).mkdirs();

			history.addToRecent(new Project(project.getName(),dirPath.getText() + File.separator + nameId.getText().trim(), project.getOwner()));
			String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
			String filename = currentPath+ File.separator + "history.cfg";
			history.save(filename);
			controllLoading.project = project;
			controllLoading.projectDir = dirPath.getText() + File.separator + nameId.getText().trim();
			controllLoading.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			// Main.classes.pop();
//			final
			Parent root = FXMLLoader.load(getClass().getResource("loadingScreen.fxml"));
//			Scene scene = new Scene(root, 800, 450);

			
			Stage splash  = new Stage();
			Stage stage = (Stage)  (recentTable.getScene().getWindow());
//			Platform.setImplicitExit(false);
			Scene scene = new Scene(root);
			splash.getIcons().add(new Image("icon-main@3x.png"));
			splash.setScene(scene);
	        splash.initStyle(StageStyle.UNDECORATED);
//	        splash.setOnHiding(event2 -> Platform.exit());
	    	stage.setOnHidden(null);
	    	
	        splash.show();
	        stage.hide();
		}
	}

	@FXML Label errorMessage;
	private boolean verifyAllCorrect() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		boolean verified = true;
		if(dirPath.getText().isEmpty()) {
			sb.append("Error: Directory Invalid! \n");
			verified = false;
		}
		if(nameId.getText().isEmpty()) {
			sb.append("Error: Project Name Invalid! \n");
			verified = false;
		}
		if(authorId.getText().isEmpty()) {
			sb.append("Error: Author Name Invalid! \n");
			verified = false;
		}
		if(!passId.getText().equals(confPassId.getText())) {
			sb.append("Error: Passwords do not match! \n");
			verified = false;
		}
		
//		verified &= passId.getText().equals(confPassId.getText());
//		verified &= dirPath.getText()!="";
//		verified &= nameId.getText()!="";
//		verified &= authorId.getText()!="";
		errorMessage.setText(sb.toString());
		return verified;
	}



	@FXML
	public void handleOpenProject(ActionEvent event) throws IOException {
		File file;
		FileChooser fil_chooser = new FileChooser();
		fil_chooser.setInitialDirectory(new File(currentPath));
		fil_chooser.getExtensionFilters().add(new ExtensionFilter("XML Data Warehousing", "*xdw"));
		file = fil_chooser.showOpenDialog(null);
		try {
			handleOpenProject(file.getAbsolutePath());
		} catch(NullPointerException e) {};

	}

	public void handleOpenProject(String path) throws IOException{
		File file = new File(path);

		if (file != null) {
			currentPath = file.getAbsoluteFile().getParent();
			System.out.println(path);
			DataWarehousing project = DataWarehousing.load(file.getAbsolutePath());

			
//			dataWarehousingController.projectPath = file.getParent();
//			dataWarehousingController.project = project;
			history.addToRecent(new Project(project.getName(),file.getParent(), project.getOwner()));
			String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
			String filename = currentPath+ File.separator + "history.cfg";
			history.save(filename);

			controllLoading.project = project;
			controllLoading.projectDir = file.getParent();
			controllLoading.stage = (Stage) recentTable.getScene().getWindow();
			// Main.classes.pop();
//			final
			Parent root = FXMLLoader.load(getClass().getResource("loadingScreen.fxml"));
//			Scene scene = new Scene(root, 800, 450);

			Stage splash  = new Stage();
			Stage stage = (Stage)  (recentTable.getScene().getWindow());
//			Platform.setImplicitExit(false);
			Scene scene = new Scene(root);
			splash.getIcons().add(new Image("icon-main@3x.png"));
			splash.setScene(scene);
	        splash.initStyle(StageStyle.UNDECORATED);
//	        splash.setOnHiding(event -> Platform.exit());
	        stage.setOnHidden(null);
	    	stage.hide();
	        splash.show();
	        
			
			// Main.classes.pop();
//			Parent root = FXMLLoader.load(getClass().getResource("dataWarehousing.fxml"));

//			Scene scene = new Scene(root, 800, 450);
//			new JMetro(scene, Style.DARK);
//			Stage window = (Stage) (recentTable.getScene().getWindow());
//			window.setResizable(true);
//			window.setScene(scene);
		}
	}

}
