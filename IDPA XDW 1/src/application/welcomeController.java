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
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import javafx.stage.Stage;
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
	int historyCapacity = 10;
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
		dirPath.setText("");
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
							System.out.println("Pin: " + project);
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
							System.out.println("Unpin: " + project);
							System.out.println(historyFinal.unpin(new Project(project.getName(), project.getPath(), project.getAuthor())));
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
		        System.out.println(newSelection);
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
		System.out.println("P");
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
			
			history.addToRecent(new Project(project.getName(),dirPath.getText(), project.getOwner()));
			String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
			String filename = currentPath+ File.separator + "history.cfg";
			history.save(filename);
			dataWarehousingController.project = project;
			dataWarehousingController.projectPath = dirPath.getText() + File.separator + nameId.getText().trim();
			// Main.classes.pop();
//			final
			Parent root = FXMLLoader.load(getClass().getResource("dataWarehousing.fxml"));
			Scene scene = new Scene(root, 800, 450);
			Stage window = (Stage) (((Node) event.getSource()).getScene().getWindow());
			window.setResizable(false);
			window.setScene(scene);

		}
	}

	private boolean verifyAllCorrect() {
		// TODO Auto-generated method stub
		boolean verified = true;
		verified &= passId.getText().equals(confPassId.getText());
		verified &= dirPath.getText()!="";
		verified &= nameId.getText()!="";
		verified &= authorId.getText()!="";
		return verified;
	}
	
	

	@FXML
	public void handleOpenProject(ActionEvent event) throws IOException {
		File file;
		FileChooser fil_chooser = new FileChooser();
		fil_chooser.setInitialDirectory(new File(currentPath));
		fil_chooser.getExtensionFilters().add(new ExtensionFilter("XML Data Warehousing", "*xdw"));
		file = fil_chooser.showOpenDialog(null);
		handleOpenProject(file.getAbsolutePath());
		
	}
	
	public void handleOpenProject(String path) throws IOException{
		File file = new File(path);

		if (file != null) {
			currentPath = file.getAbsoluteFile().getParent();
			System.out.println(path);
			DataWarehousing project = DataWarehousing.load(file.getAbsolutePath());
			dataWarehousingController.projectPath = file.getParent();
			dataWarehousingController.project = project;
			history.addToRecent(new Project(project.getName(),file.getParent(), project.getOwner()));
			String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
			String filename = currentPath+ File.separator + "history.cfg";
			history.save(filename);
			
			// Main.classes.pop();
			Parent root = FXMLLoader.load(getClass().getResource("dataWarehousing.fxml"));
			
			Scene scene = new Scene(root, 800, 450);
//			new JMetro(scene, Style.DARK);
			Stage window = (Stage) (recentTable.getScene().getWindow());
			window.setResizable(false);
			window.setScene(scene);
		}
	}

}
