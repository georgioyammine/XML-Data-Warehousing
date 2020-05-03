package application;

import java.awt.Toolkit;
import java.io.File;
import java.io.StringWriter;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTreeTableView;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;

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
	@FXML
	JFXButton addVersion;
	@FXML
	Label name;
	@FXML
	Label author;
	@FXML
	Label nbOfVersions;
	@FXML
	JFXComboBox versionBox;
	@FXML
	JFXCheckBox deltaOnly;
	@FXML
	JFXComboBox filter1;
	@FXML
	JFXComboBox filter2;
	@FXML
	JFXComboBox filter3;
	@FXML
	JFXComboBox filter4;
	@FXML
	TextArea queryResult;
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
								getThisVersion(
										Integer.parseInt(getTableView().getItems().get(getIndex()).getVersion()));

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
							mainTabPane.getSelectionModel().select(2);
							// versionBox.setValue(value);
							versionBox.getSelectionModel().select(Integer.parseInt(data.getVersion()) - 1);
							// versionBox.

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
		filter1.getItems().addAll("All");
		filter1.getSelectionModel().select(0);
		filter2.setVisible(false);
		filter3.setVisible(false);
		filter4.setVisible(false);
		queryResult.setEditable(false);
		deltaOnly.setDisable(true);
		// tableview.setItems(studentsModels);
	}
	

	// add your data here from any source
	// private ObservableList<Data> studentsModels =
	// FXCollections.observableArrayList(
	// new Data("7", "24/04/2020", "15 kbs", "10 kbs", "51,66%", "Available"),
	// new Data("7", "14/03/2020", "30 kbs", "15 kbs", "60%", "Not Available"),
	// new Data("7", "14/03/2020", "30 kbs", "15 kbs", "60%", "Not Available"));
	@FXML
	TableColumn authorCol;

	private void getThisVersion(int i) throws Exception {
		// TODO Auto-generated method stub
		project.getThisVersion(project.versions.get(i - 1));

	}

	@FXML
	public void versionBoxChanged() throws Exception {
		if((int)versionBox.getValue()==1) {
			deltaOnly.setDisable(true);
			deltaOnly.setSelected(false);
		}
		else
			deltaOnly.setDisable(false);
	}
	
	@FXML
	public void updateDropBoxes() throws Exception {
		filter1.getItems().clear();
		filter2.setVisible(false);
		filter3.setVisible(false);
		filter4.setVisible(false);
		filter2.getItems().clear();
		filter3.getItems().clear();
		filter4.getItems().clear();
		if (deltaOnly.isSelected())
			filter1.getItems().addAll("All", "Node Updates", "Node Deletions", "Node Insertions");
		else
			filter1.getItems().addAll("All");
		filter1.getSelectionModel().select(0);
	}

	@FXML
	public void updateDropBoxes2() throws Exception {
		if (filter1.getItems().isEmpty())
			return;
		filter2.getItems().clear();
		filter2.setVisible(false);
		if (deltaOnly.isSelected()) {
			if (!((String) filter1.getValue()).equalsIgnoreCase("All")) {
				if (((String) filter1.getValue()).equalsIgnoreCase("Node Updates")) {
					filter2.setVisible(true);
					filter2.getItems().addAll("All", "Labels", "Attributes", "Text Nodes");
				} else {
					if (((String) filter1.getValue()).equalsIgnoreCase("Node Deletions")) {
						filter2.setVisible(true);
						filter2.getItems().addAll("All");
					} else {
						filter2.setVisible(true);
						filter2.getItems().addAll("All");
					}
				}
				filter2.getSelectionModel().select(0);
			}
		} else {

		}
	}

	@FXML
	public void updateDropBoxes3() throws Exception {
		if (filter2.getItems().isEmpty())
			return;
		filter3.getItems().clear();
		filter3.setVisible(false);
		if (deltaOnly.isSelected()) {
			if (!((String) filter2.getValue()).equalsIgnoreCase("All")) {
				if (((String) filter2.getValue()).equalsIgnoreCase("Labels")) {
					filter3.setVisible(true);
					// TO-DO
					filter3.getItems().addAll("All", "#1", "#2");
				} else {
					if (((String) filter2.getValue()).equalsIgnoreCase("Attributes")) {
						filter3.setVisible(true);
						filter3.getItems().addAll("All", "Updates", "Deletions", "Insertions");
					} else { // text nodes
						filter3.setVisible(true);
						filter3.getItems().addAll("All", "Word Updates", "Word Deletions", "Word Insertions");
					}
				}
				filter3.getSelectionModel().select(0);
			}
		} else {

		}
	}

	@FXML
	public void updateDropBoxes4() throws Exception {
		if (filter3.getItems().isEmpty())
			return;
		System.out.println("S3");
		// filter1.getItems().clear();
		// if(deltaOnly.isSelected())
		// filter1.getItems().addAll("All","Node Updates","Node Deletions","Node
		// Insertions");
		// else
		// filter1.getItems().addAll("All");
	}

	public String getDiffRelativePath(int vnew) {
		return "src"+File.separator+"diffs"+File.separator+"Diff_"+(vnew)+"to"+(vnew-1)+".xml";
	}
	
	@FXML
	public void queryHandle() throws Exception {
		if (deltaOnly.isSelected()) {
			try {
				// changes only
				String diffPath = projectPath + File.separator
						+ getDiffRelativePath((int)versionBox.getValue());
				// get the diff and reverse it
//				XMLDIF
				Node reversedDiff = XMLDiffAndPatch.reverseXMLESNode(diffPath);
				StringBuilder expression = new StringBuilder("//Edit_Script");
				switch ((String) filter1.getValue()) {
				case "Node Updates":
					expression.append("/Update");
					break;
				case "Node Deletions":
					expression.append("/Delete");
					break;
				case "Node Insertions":
					expression.append("/Insert");
					break;

				default:
					break;
				}

				if (!filter2.getItems().isEmpty()) {
					switch ((String) filter2.getValue()) {
					case "Labels":
						expression.append("//Label");
						break;
					case "Attributes":
						expression.append("//Attributes");
						break;
					case "Text Nodes":
						expression.append("/*[@type='Text_Node']");
						break;

					default:
						break;
					}
				}

				if (!filter3.getItems().isEmpty()) {
					switch ((String) filter3.getValue()) {
					case "Updates":
						expression.append("/Update_Attribute");
						break;
					case "Deletions":
						expression.append("/Delete_Attribute");
						break;
					case "Insertions":
						expression.append("/Insert_Attribute");
						break;
					case "Word Updates":
						expression.append("/Update_Word");
						break;
					case "Word Deletions":
						expression.append("/Delete_Word");
						break;
					case "Word Insertions":
						expression.append("/Insert_Word");
						break;

					default:
						break;
					}
					// filter3.getItems().addAll("All","Updates","Deletions","Insertions");
					// filter3.getItems().addAll("All","Word Updates","Word Deletions","Word
					// Insertions");
				}

				if (!filter4.getItems().isEmpty()) {
					switch ((String) filter4.getValue()) {
					case "Labels":
						expression.append("//Label");
						break;
					case "Attributes":
						expression.append("//Attributes");
						break;
					case "Text Nodes":
						expression.append("/[@type='Text_Node']");
						break;

					default:
						break;
					}
				}
				System.out.println(expression.toString());

				Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
				Element root2 = doc.createElement("Results");
				doc.appendChild(doc.importNode(reversedDiff, true));

				XPath xPath = XPathFactory.newInstance().newXPath();
				// filter1.getValue(); //"All","Node Updates","Node Deletions","Node
				// Insertions";
		
				NodeList nodeList = (NodeList) xPath.compile(expression.toString()).evaluate(doc,
						XPathConstants.NODESET);
				System.out.println(nodeList.getLength());
				Document newXmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
				Element root = newXmlDocument.createElement("Results");
				newXmlDocument.appendChild(root);
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					Node copyNode = newXmlDocument.importNode(node, true);
					root.appendChild(copyNode);
				}
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
				// initialize StreamResult with File object to save to file
				StreamResult result = new StreamResult(new StringWriter());
				DOMSource source = new DOMSource(newXmlDocument);
				transformer.transform(source, result);
				String xmlString = result.getWriter().toString();
				queryResult.setText(xmlString.substring(55));

			} catch (Exception e) {
				e.printStackTrace();
				queryResult.setText("Query Invalid!");
			}
		} else {

		}

	}

	@FXML
	public void addVersionHandler() throws Exception {
		FileChooser fil_chooser = new FileChooser();
		fil_chooser.setInitialDirectory(new File(currentPath));
		fil_chooser.getExtensionFilters().add(new ExtensionFilter("XML", "*.xml"));
		file = fil_chooser.showOpenDialog(null);
		// JFileChooser fileChooserx = new JFileChooser();
		// fileChooserx.setCurrentDirectory(new File(currentPath));
		// fileChooserx.show

		if (file != null) {
			currentPath = file.getAbsoluteFile().getParent();
			System.out.println("add version");
			processAddVersion.restart();
			// project.addNewVersion(project.getOwner(), file.getAbsolutePath());

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
	@FXML
	JFXTabPane mainTabPane;

	private void updateInfo() {
		// TODO Auto-generated method stub
		name.setText(project.getName());
		author.setText(project.getOwner());
		nbOfVersions.setText(project.versions.size() + "");
		ArrayList<Data> alist = new ArrayList<>();
		ArrayList<Integer> arl = new ArrayList<>();
		for (Version v : project.versions) {
			alist.add(new Data(v));
			arl.add(v.getNumber());
		}
		ObservableList<Data> list = FXCollections.observableArrayList(alist);
		tableview.setItems(list);
		versionBox.setItems(FXCollections.observableArrayList(arl));
		versionBox.getSelectionModel().select(0);
	}

	private void saveVersion() {
		// TODO Auto-generated method stub
		project.save(projectPath + File.separator + project.getName() + ".xdw");
	}

}
