package application;

import java.awt.Toolkit;
import java.io.File;
import java.io.StringWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
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
import javafx.scene.input.KeyEvent;
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
	@FXML
	JFXTextField searchBar;
	@FXML
	JFXToggleButton advancedSearch;

	JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
	ArrayList<String> words = new ArrayList<String>(); // Use to populate auto-completion search bar;

	File file;
	HashSet<String> possibleWordsSet = new HashSet<String>();
	HashSet<String> allNodesInVersion = new HashSet<String>();

	String versionPath;
	String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();

	final Clipboard clipboard = Clipboard.getSystemClipboard();
	final ClipboardContent content = new ClipboardContent();
	static DataWarehousing project;
	static String projectPath;
	Element queryingVersionRoot;

	private AutoCompletionBinding autoCompletionBinding;

	@FXML
	public void applySettingsHandle() {
	}

	@FXML
	public void copyHandle() {
	}

	@FXML
	public void initialize() {
		searchBar.setVisible(false);

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
										getTableView().getItems().get(getIndex()).getVersion());

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
							Data data = getTableView().getItems().get(getIndex());
							System.out.println("Query: " + data);
							mainTabPane.getSelectionModel().select(2);

							versionPath = projectPath + File.separator + "src" + File.separator + "available files"
									+ File.separator + project.getName() + "_v" + data.getVersion()
									+ ".xml";
							System.out.println(versionPath);
							versionBox.getSelectionModel().select(data.getVersion() - 1);
							File xmlFile = new File(versionPath);
							// generateXPaths(xmlFile);

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

					// private void generateXPaths(File xmlFile, Node root) throws
					// ParserConfigurationException, SAXException, IOException {
					// DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					// DocumentBuilder db = dbf.newDocumentBuilder();
					// Document doc = db.parse(xmlFile);
					//
					// NodeList list = root.getChildNodes();
					//
					// for(int i = 0; i<list.getLength();i++) {
					//
					// }
					//
					// }

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

//		updateInitialSuggestions();
		searchBar.setOnKeyReleased((KeyEvent e) -> {

			switch (e.getCode()) {

			case BACK_SPACE:
				System.out.println(searchBar.getText());
				updateSuggestionBackSpace(searchBar.getText());
				break;
			case KP_DOWN:
				break;
			case DOWN:
				break;
			case KP_UP:
				break;
			case UP:
				break;
			default:
				switch (searchBar.getText().charAt(searchBar.getText().length() - 1)) {
				case '/':
					updateSuggestionChilds(searchBar.getText());
					break;
				case '@':
					updateSuggestionsAttr(searchBar.getText());
					break;
				case '[':
					positionSuggestions(searchBar.getText());
					break;
				default:
					break;

				}
				break;
			}
		});

		searchBar.setPromptText("XPath Expression");

		autoCompletePopup.setSelectionHandler(event -> {
			searchBar.setText(event.getObject());
			searchBar.positionCaret(searchBar.getText().length());
		});
		autoCompletePopup.getSuggestions().addAll(possibleWordsSet);
		searchBar.textProperty().addListener(observable -> {
			autoCompletePopup.filter(s -> (s.toLowerCase().contains(searchBar.getText().toLowerCase())));
			if (!autoCompletePopup.getFilteredSuggestions().isEmpty()) {
				autoCompletePopup.show(searchBar);
			} else {
				autoCompletePopup.hide();
			}
		});

	}

	@FXML
	public void toggleHandle() {
		if (advancedSearch.isSelected()) {
			try {
				if (deltaOnly.isSelected())
					queryingVersionRoot = (Element) (project
							.getThisVersionAsNode(project.versions.get((int) versionBox.getValue() - 2)));
				else
					queryingVersionRoot = (Element) project
							.getThisVersionAsNode(project.versions.get((int) versionBox.getValue() - 1));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			searchBar.setVisible(true);
			filter1.setVisible(false);
			filter2.setVisible(false);
			filter3.setVisible(false);
			filter4.setVisible(false);
		} else {
			searchBar.setVisible(false);
			searchBar.clear();
			autoCompletePopup.hide();
			filter1.getSelectionModel().select(0);
			filter1.setVisible(true);
		}
	}

	private void positionSuggestions(String text) {
		try {
			autoCompletePopup.getSuggestions().clear();
			if (text.length() > 2) {
				Element root = queryingVersionRoot;

				XPath xPath = XPathFactory.newInstance().newXPath();
				String expression = text.substring(0, text.lastIndexOf("["));
				NodeList nodeList = (NodeList) xPath.compile(expression.toString()).evaluate(root,
						XPathConstants.NODESET);
				ArrayList<String> arl = new ArrayList<String>();
				for (int i = 1; i <= nodeList.getLength(); i++) {
					arl.add(text + i + "]");
				}
				autoCompletePopup.getSuggestions().addAll(arl);
				autoCompletePopup.show(searchBar);
				System.out.println("Position: " + expression);
			}

		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void updateSuggestionsAttr(String text) {
		System.out.println("Entered suggestionsAtr");
		try {
			if (text.length() > 2) {
				autoCompletePopup.getSuggestions().clear();
				Element root = queryingVersionRoot;

				XPath xPath = XPathFactory.newInstance().newXPath();
				String expression = text.substring(0, text.lastIndexOf("[")) + "/@*";
				NodeList nodeList = (NodeList) xPath.compile(expression.toString()).evaluate(root,
						XPathConstants.NODESET);
				possibleWordsSet = new HashSet<>();
				for (int i = 0; i < nodeList.getLength(); i++) {
					possibleWordsSet.add(text + nodeList.item(i).getNodeName() + "=");
				}

				autoCompletePopup.getSuggestions().addAll(possibleWordsSet);
				autoCompletePopup.show(searchBar);
				System.out.println(possibleWordsSet);
				System.out.println("Attributes:" + expression);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void updateSuggestionBackSpace(String text) {
		if (text.length() > 2) {
			try {

				String expression;

				if (text.lastIndexOf("/") > text.lastIndexOf("@") && text.lastIndexOf("/") > text.lastIndexOf("[")) {
					expression = text.substring(0, text.lastIndexOf("/") + 1);
					updateSuggestionChilds(expression);
				}
				if (text.lastIndexOf("@") > text.lastIndexOf("/") && text.lastIndexOf("@") > text.lastIndexOf("[")) {
					expression = text.substring(0, text.lastIndexOf("@") + 1);
					updateSuggestionsAttr(expression);
				}
				if (text.lastIndexOf("[") > text.lastIndexOf("/") && text.lastIndexOf("[") > text.lastIndexOf("@")) {
					expression = text.substring(0, text.lastIndexOf("[") + 1);
					positionSuggestions(expression);

					System.out.println("Backspace:" + expression);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else

		{
			autoCompletePopup.hide();
			updateInitialSuggestions();
		}

	}

	private void updateInitialSuggestions() {
		try {
			autoCompletePopup.getSuggestions().clear();
			Element root = queryingVersionRoot;
			NodeList nl = root.getElementsByTagName("*");
			
			possibleWordsSet = new HashSet<>();
			

			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "/*";
			NodeList nodeList = (NodeList) xPath.compile(expression.toString()).evaluate(root,
					XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++) 
				possibleWordsSet.add("/"+nodeList.item(i).getNodeName());
			
			for (int i = 0; i < nl.getLength(); i++)
				possibleWordsSet.add("//" + nl.item(i).getNodeName());
			
			
			
			autoCompletePopup.getSuggestions().addAll(possibleWordsSet);
			autoCompletePopup.hide();
			autoCompletePopup.show(searchBar);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// }
	}

	private void updateSuggestionChilds(String text) {
		if (text.length() > 2) {
			try {
				autoCompletePopup.getSuggestions().clear();
				Element root = queryingVersionRoot;
				XPath xPath = XPathFactory.newInstance().newXPath();
				String expression = text + "*";
				NodeList nodeList = (NodeList) xPath.compile(expression.toString()).evaluate(root,
						XPathConstants.NODESET);
				possibleWordsSet = new HashSet<>();
				for (int i = 0; i < nodeList.getLength(); i++) {
					possibleWordsSet.add(text + nodeList.item(i).getNodeName());
				}

				autoCompletePopup.getSuggestions().addAll(possibleWordsSet);
				autoCompletePopup.show(searchBar);
				System.out.println("Children: " + expression);
				//
				// if (autoCompletionBinding != null) {
				// autoCompletionBinding.dispose();
				//
				// }

				// autoCompletionBinding = TextFields.bindAutoCompletion(searchBar,
				// possibleWordsSet);
				// autoCompletionBinding.setHideOnEscape(true);
				// autoCompletionBinding.set

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			updateInitialSuggestions();
		}
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

	private String getAbsolutePath(String relativePath) {
		return projectPath + File.separator + relativePath;
	}

	@FXML
	public void versionBoxChanged() throws Exception {
		if(versionBox.getValue()==null)
			return;
		if ((int) versionBox.getValue() == 1) {
			deltaOnly.setDisable(true);
			deltaOnly.setSelected(false);
		} else
			deltaOnly.setDisable(false);
		if (advancedSearch.isSelected()) {
			if (deltaOnly.isSelected())
				queryingVersionRoot = (Element) (project
						.getThisVersionAsNode(project.versions.get((int) versionBox.getValue() - 2)));
			else
				queryingVersionRoot = (Element) project
						.getThisVersionAsNode(project.versions.get((int) versionBox.getValue() - 1));
			updateInitialSuggestions();
		}
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

		if (advancedSearch.isSelected()) {
			if (deltaOnly.isSelected())
				queryingVersionRoot = (Element) (project
						.getThisVersionAsNode(project.versions.get((int) versionBox.getValue() - 2)));
			else
				queryingVersionRoot = (Element) project
						.getThisVersionAsNode(project.versions.get((int) versionBox.getValue() - 1));
		}
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
				if (!((String) filter2.getValue()).equalsIgnoreCase("Labels")) {

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
		filter4.getItems().clear();
		filter4.setVisible(false);
		if (deltaOnly.isSelected()) {
			if (((String) filter3.getValue()).equalsIgnoreCase("Updates")) {
				filter4.setVisible(true);
				filter4.getItems().addAll("All", "Key & Value", "Key", "Value");
				filter4.getSelectionModel().select(0);
			}
		}
	}

	public String getDiffRelativePath(int vnew) {
		return "src" + File.separator + "diffs" + File.separator + "Diff_" + (vnew) + "to" + (vnew - 1) + ".xml";
	}

	@FXML
	public void queryHandle() throws Exception {
		if (!advancedSearch.isSelected()) {
			if (deltaOnly.isSelected()) {
				try {
					// changes only
					String diffPath = projectPath + File.separator + getDiffRelativePath((int) versionBox.getValue());
					// get the diff and reverse it
					// XMLDIF
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

					}

					if (!filter4.getItems().isEmpty()) {
						switch ((String) filter4.getValue()) {
						case "Key & Value":
							expression.append("/*[@change='both']");
							break;
						case "Key":
							expression.append("/*[@change='key']");
							break;
						case "Value":
							expression.append("/*[@change='value']");
							break;

						default:
							break;
						}
					}

					System.out.println(expression.toString());

					Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
					doc.appendChild(doc.importNode(reversedDiff, true));

					XPath xPath = XPathFactory.newInstance().newXPath();

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
		} else {
			String expression = searchBar.getText();

			if (deltaOnly.isSelected()) {
				// TODO

				XPath xPath = XPathFactory.newInstance().newXPath();

				NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(queryingVersionRoot, XPathConstants.NODESET);
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					String dotRep = getDotPosition(node);
					sb.append("//"+dotRep +" | " );

//					System.out.println(node + " " + dotRep);
				}
				sb.delete(sb.length()-3, sb.length());
				System.out.println(sb.toString());
				
				String diffPath = projectPath + File.separator + getDiffRelativePath((int) versionBox.getValue());
				Node reversedDiff = XMLDiffAndPatch.reverseXMLESNode(diffPath);
//				Util.print(reversedDiff, 0);

				Document newXmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
				Element root = newXmlDocument.createElement("Results");
				if(!sb.toString().isEmpty()) {
					
					Node updNode = (Node) xPath.compile("//Update").evaluate(reversedDiff,
							XPathConstants.NODE);
					if(updNode!=null) {
						Document updDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
						updDoc.appendChild(updDoc.importNode(updNode, true));
						Node node = newXmlDocument.createElement("Update");
						NodeList results = (NodeList) xPath.compile(sb.toString()).evaluate(updDoc,
								XPathConstants.NODESET);
						for(int i = 0;i<results.getLength();i++) {
							node.appendChild(newXmlDocument.importNode(results.item(i),true));
						}
						root.appendChild(newXmlDocument.importNode(node,true));
					}
					Node delNode = (Node) xPath.compile("//Delete").evaluate(reversedDiff,
							XPathConstants.NODE);
					if(delNode!=null) {
						Document updDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
						updDoc.appendChild(updDoc.importNode(delNode, true));
						Node node = newXmlDocument.createElement("Delete");
						NodeList results = (NodeList) xPath.compile(sb.toString()).evaluate(updDoc,
								XPathConstants.NODESET);
						for(int i = 0;i<results.getLength();i++) {
							node.appendChild(newXmlDocument.importNode(results.item(i),true));
						}
						root.appendChild(newXmlDocument.importNode(node,true));
					}
				}
				
//				NodeList nodeList2 = (NodeList) xPath.compile(sb.toString()).evaluate(reversedDiff, XPathConstants.NODESET);
//				System.out.println(nodeList2.getLength());
				
				newXmlDocument.appendChild(root);
//
//				for (int i = 0; i < nodeList2.getLength(); i++) {
//					Node node = nodeList2.item(i);
//					Node copyNode = newXmlDocument.importNode(node, true);
//					root.appendChild(copyNode);
//					System.out.println("Node:"+node);
//				}
				Transformer transformer = TransformerFactory.newInstance().newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
				// initialize StreamResult with File object to save to file
				StreamResult result = new StreamResult(new StringWriter());
				DOMSource source = new DOMSource(newXmlDocument);
				transformer.transform(source, result);
				String xmlString = result.getWriter().toString();
				queryResult.setText(xmlString.substring(55));

			} else {

				XPath xPath = XPathFactory.newInstance().newXPath();

				NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(queryingVersionRoot, XPathConstants.NODESET);
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
			}
		}
	}
	public String getDotPosition(Node node) {
		Stack<Node> stack = new Stack<Node>();

		while(node.getParentNode()!=null) {
			stack.add(node);
			node = node.getParentNode();
		}
		node = stack.pop();
		StringBuilder sb = new StringBuilder("B");
		while(!stack.isEmpty()) {
			System.out.println(node);
			System.out.println(stack);
			System.out.println(sb);
			NodeList nodeList = node.getChildNodes();
			for(int i = 0; i<nodeList.getLength();i++) {
				if(nodeList.item(i).isEqualNode(stack.peek())) {
					node = stack.pop();
					sb.append("." + (i+1));
					break;
				}
			}
			System.out.println(sb);
		}
		return sb.toString();
	}

	@FXML
	public void addVersionHandler() throws Exception {
		FileChooser fil_chooser = new FileChooser();
		fil_chooser.setInitialDirectory(new File(currentPath));
		fil_chooser.getExtensionFilters().add(new ExtensionFilter("XML", "*.xml"));
		file = fil_chooser.showOpenDialog(null);

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
