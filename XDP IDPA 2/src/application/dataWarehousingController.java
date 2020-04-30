package application;

import com.jfoenix.controls.JFXTreeTableView;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;

import java.awt.Toolkit;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Label;

public class dataWarehousingController {

	@FXML
	JFXTreeTableView treetableview;
	@FXML
	TreeTableColumn versionId;
	@FXML
	TreeTableColumn dateId;
	@FXML
	TableView tableview;
	@FXML
	TableColumn Col1;
	@FXML
	TableColumn Col2;
	@FXML
	TableColumn verCol;
	@FXML
	TableColumn dateCol;
	@FXML
	TableColumn sizeCol;
	@FXML
	TableColumn diffCol;
	@FXML
	TableColumn simCol;
	@FXML
	TableColumn statCol;
	@FXML
	TableColumn getCol;
	@FXML
	TableColumn queryCol;
	@FXML JFXButton addVersion;
	@FXML Label name;
	@FXML Label author;
	@FXML Label nbOfVersions;
	
	File file;
	String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
	

	final Clipboard clipboard = Clipboard.getSystemClipboard();
	final ClipboardContent content = new ClipboardContent();
	static DataWarehousing project;
	static String projectPath;


	@FXML
	public void applySettingsHandle() {
	}

	@FXML
	public void copyHandle() {
	}

	@FXML
	public void initialize() {

		// make sure the property value factory should be exactly same as the e.g
		// getStudentId from your model class

		verCol.setCellValueFactory(new PropertyValueFactory<>("version"));
		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
		diffCol.setCellValueFactory(new PropertyValueFactory<>("diffSize"));
		simCol.setCellValueFactory(new PropertyValueFactory<>("similarity"));
		statCol.setCellValueFactory(new PropertyValueFactory<>("status"));
		authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
		
		
		// lastName.setCellValueFactory(new PropertyValueFactory<>("LastName"));
		// add your data to the table here.
		Callback<TableColumn<Data, Void>, TableCell<Data, Void>> cellFactory = new Callback<TableColumn<Data, Void>, TableCell<Data, Void>>() {
	        @Override
	        public TableCell<Data, Void> call(final TableColumn<Data, Void> param) {
	            final TableCell<Data, Void> cell = new TableCell<Data, Void>() {

	                private final Button btn = new Button("Get");

	                {
	                    btn.setOnAction((ActionEvent event) -> {
	                    	try {
	                    		System.out.println("pressed");
								getThisVersion(Integer.parseInt(getTableView().getItems().get(getIndex()).getVersion()));
								
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	                    	
	                        Data data = getTableView().getItems().get(getIndex());
	                        System.out.println("get data: " + data);
	                        Toolkit.getDefaultToolkit().beep();
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
	    getCol.setCellFactory(cellFactory);

	    Callback<TableColumn<Data, Void>, TableCell<Data, Void>> cellFactoryQ = new Callback<TableColumn<Data, Void>, TableCell<Data, Void>>() {
	        @Override
	        public TableCell<Data, Void> call(final TableColumn<Data, Void> param) {
	            final TableCell<Data, Void> cell = new TableCell<Data, Void>() {

	                private final Button btn = new Button("Query");

	                {
	                    btn.setOnAction((ActionEvent event) -> {
//	                    	
	                        Data data = getTableView().getItems().get(getIndex());
	                        System.out.println("Query: " + data);
	                        
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
	    queryCol.setCellFactory(cellFactoryQ);
	    updateInfo();

//		tableview.setItems(studentsModels);
	}

	// add your data here from any source
//	private ObservableList<Data> studentsModels = FXCollections.observableArrayList(
//			new Data("7", "24/04/2020", "15 kbs", "10 kbs", "51,66%", "Available"),
//			new Data("7", "14/03/2020", "30 kbs", "15 kbs", "60%", "Not Available"),
//			new Data("7", "14/03/2020", "30 kbs", "15 kbs", "60%", "Not Available"));
	@FXML TableColumn authorCol;
	
	private void getThisVersion(int i) throws Exception {
		// TODO Auto-generated method stub
		project.getThisVersion(project.versions.get(i-1));
		
	}

	@FXML public void addVersionHandler() throws Exception {
		FileChooser fil_chooser = new FileChooser();
		fil_chooser.setInitialDirectory(new File(currentPath));
		fil_chooser.getExtensionFilters().add(new ExtensionFilter("XML", "*.xml"));
		file = fil_chooser.showOpenDialog(null);
//		JFileChooser fileChooserx = new JFileChooser();
//		fileChooserx.setCurrentDirectory(new File(currentPath));
//		fileChooserx.show
		
		if (file != null) {
			currentPath = file.getAbsoluteFile().getParent();
			System.out.println("add version");
			processAddVersion.restart();
//			project.addNewVersion(project.getOwner(), file.getAbsolutePath());
			
		}
	}
	@SuppressWarnings("rawtypes")
	Service processAddVersion = new Service() {
		@Override
		protected Task createTask() {
			return new Task<Void>() {
				@Override
				public Void call() throws Exception {
					double t1 = System.currentTimeMillis();
					project.addNewVersion(project.getOwner(), file.getAbsolutePath());
					System.out.println(System.currentTimeMillis() - t1 + "ms");
					Platform.runLater(() -> {
						saveVersion();
						updateInfo();
					});
					return null;
					
				}
			};
		}
	};

	private void updateInfo() {
		// TODO Auto-generated method stub
		name.setText(project.getName());
		author.setText(project.getOwner());
		nbOfVersions.setText(project.versions.size()+"");
		ArrayList<Data> alist = new ArrayList<>();
		for(Version v : project.versions) {
			alist.add(new Data(v));
		}
		ObservableList<Data> list = FXCollections.observableArrayList(alist);
		tableview.setItems(list);
	}
	private void saveVersion() {
		// TODO Auto-generated method stub
		project.save(projectPath+File.separator+project.getName()+".xdw");
	}


}


