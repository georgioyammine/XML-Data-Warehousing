package application;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.controls.JFXTreeTableView;
import com.sun.prism.impl.ps.BaseShaderContext.MaskType;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.ChartType;
import eu.hansolo.tilesfx.Tile.ImageMask;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.Tile.TextSize;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.chart.ChartData;
import eu.hansolo.tilesfx.colors.Bright;
import eu.hansolo.tilesfx.colors.Dark;
import eu.hansolo.tilesfx.tools.FlowGridPane;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
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
	JFXButton queryBtn;
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
	@FXML
	AnchorPane githubPane;
	@FXML
	AnchorPane directoryPane;
	@FXML
	AnchorPane storagePane;
	@FXML
	AnchorPane calendarPane;
	@FXML
	AnchorPane anchorUser;

	private Tile gitHubTile;
	private Tile directoryTile;
	private Tile openProjectTile;
	private Tile createProjectTile;
	private Tile calendarTile;
	private Tile storageTile;
	private Tile homeTile;
	private Tile addVersionTile;
	private Tile numberTile;
	private Tile percentageTile;
	private Tile clockTile;
	private Tile gaugeTile;
	private Tile sparkLineTile;
	private Tile areaChartTile;
	private Tile lineChartTile;
	private Tile highLowTile;
	private Tile timerControlTile;
	private Tile nameTile;
	private Tile authorTile;
	private Tile xdpTile;
	private Tile dayTile;
	private Tile matrixTile;

	private final double TILE_WIDTH = 300;
	private final double TILE_HEIGHT = 300;
	FlowGridPane pane;

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


	@FXML
	public void applySettingsHandle() {
	}

	@FXML
	public void copyHandle() {
	}

	@FXML
	public void initialize() throws IOException {
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

					private final Button btn = new Button("",
							new ImageView(new Image("images/download2.png", 20, 20, true, true)));

					{
						btn.setOnAction((ActionEvent event) -> {
							try {
								System.out.println("pressed");
								getThisVersion(getTableView().getItems().get(getIndex()).getVersion());

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

					private final Button btn = new Button("",
							new ImageView(new Image("images/inspect.png", 20, 20, true, true)));

					{
						btn.setOnAction((ActionEvent event) -> {
							Data data = getTableView().getItems().get(getIndex());
							System.out.println("Query: " + data);
							mainTabPane.getSelectionModel().select(2);

							versionPath = projectPath + File.separator + "src" + File.separator + "available files"
									+ File.separator + project.getName() + "_v" + data.getVersion() + ".xml";
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

		initializeStoragePane();
		initializeCalendarData();
		updateDirectoryPane();
		updateGitHubPane();
		initializeHomeTile();
		initializeVersionPane();
		initializeNumberTile();
		initializeInfo();
		initializeXDPTile();
		initializeDayTile();
		initializeMatrixTile();

		percentageTile = TileBuilder.create().skinType(SkinType.PERCENTAGE).prefSize(TILE_WIDTH, TILE_HEIGHT)
				.title("Percentage Tile").unit("%").description("Test").maxValue(60).build();

		clockTile = TileBuilder.create().skinType(SkinType.CLOCK).textSize(TextSize.BIGGER).prefSize(TILE_WIDTH, TILE_HEIGHT).title("Clock Tile")
				.running(true).build();

		// remove
		gaugeTile = TileBuilder.create().skinType(SkinType.GAUGE).prefSize(TILE_WIDTH, TILE_HEIGHT).title("Gauge Tile")
				.unit("V").threshold(75).build();

		//remove
		sparkLineTile = TileBuilder.create().skinType(SkinType.SPARK_LINE).prefSize(TILE_WIDTH, TILE_HEIGHT)
				.title("SparkLine Tile").unit("mb")
				.gradientStops(new Stop(0, Tile.GREEN), new Stop(0.5, Tile.YELLOW), new Stop(1.0, Tile.RED))
				.strokeWithGradient(true)
				// .smoothing(true)
				.build();

		// sparkLineTile.valueProperty().bind(value);

		// remove
		areaChartTile = TileBuilder.create().skinType(SkinType.SMOOTHED_CHART).prefSize(TILE_WIDTH, TILE_HEIGHT)
				.title("SmoothedChart Tile").chartType(ChartType.AREA)
				// .animated(true)
				.smoothing(true).tooltipTimeout(1000)
				// .tilesFxSeries(new TilesFXSeries<>(
				// Tile.BLUE,
				// new LinearGradient(0, 0, 0, 1,
				// true, CycleMethod.NO_CYCLE,
				// new Stop(0, Tile.BLUE),
				// new Stop(1, Color.TRANSPARENT))))
				.build();

		// remove
		lineChartTile = TileBuilder.create().skinType(SkinType.SMOOTHED_CHART).prefSize(TILE_WIDTH, TILE_HEIGHT)
				.title("SmoothedChart Tile")
				// .animated(true)
				.smoothing(false)
				// .series(series2, series3)
				.build();

		//remove
		highLowTile = TileBuilder.create().skinType(SkinType.HIGH_LOW).prefSize(TILE_WIDTH, TILE_HEIGHT)
				.title("HighLow Tile").unit("\u20AC").description("Test").text("Whatever text").referenceValue(6.7)
				.value(8.2).build();

		// remove
		timerControlTile = TileBuilder.create().skinType(SkinType.TIMER_CONTROL).prefSize(TILE_WIDTH, TILE_HEIGHT)
				.title("TimerControl Tile").text("Whatever text").secondsVisible(true).dateVisible(true)
				// .timeSections(timeSection)
				.running(true).build();

		pane = new FlowGridPane(6, 3, matrixTile, clockTile,numberTile, storageTile, directoryTile,addVersionTile,
										calendarTile, homeTile, nameTile, authorTile ,gaugeTile, sparkLineTile,
										 dayTile, lineChartTile, highLowTile, timerControlTile, xdpTile, gitHubTile);
		pane.setPrefSize(homeTab.getPrefWidth(), homeTab.getPrefHeight());
		pane.setAlignment(Pos.CENTER);
		pane.setCenterShape(true);
		pane.setHgap(5);
		pane.setVgap(5);
		pane.setPadding(new Insets(5, 5, 5, 5));
		pane.setBackground(new Background(new BackgroundFill(Color.web("#101214"), CornerRadii.EMPTY, Insets.EMPTY)));
		homeTab.getChildren().add(pane);
		System.out.println(homeTab.getPrefHeight());

		initializeTilePressed();
		initializeBindings();
		
		Platform.runLater(()->{
			System.out.println(homeTab.getScene());
		authorTile.widthProperty().addListener((obs, oldVal, newVal) -> {
			if (matrixTile != null) {
				matrixTile.setChartData(getWeeklyData());	
			}
			if(storageTile!=null) {
				storageTile.setValue(getSavedSpace());
			}
		});

		authorTile.heightProperty().addListener((obs, oldVal, newVal) -> {
			if (matrixTile != null) {
				matrixTile.setChartData(getWeeklyData());
			}
			if(storageTile!=null) {
				storageTile.setValue(getSavedSpace());
			}
		});
		});

	}

	private void initializeMatrixTile() {
		matrixTile = TileBuilder.create().skinType(SkinType.MATRIX).prefSize(TILE_WIDTH, TILE_HEIGHT)
				.title("Number of version per day").text("Any Text").textSize(TextSize.BIGGER).textVisible(false).animated(true).matrixSize(8, 20)
				.chartData(getWeeklyData()).maxValue(20).build();

	}

	private ArrayList<ChartData> getWeeklyData() {
		int[] count = new int[8];
		ArrayList<ChartData> weeklyData = new ArrayList<ChartData>();
		ArrayList<Version> v = project.versions;
		Date date = new Date(System.currentTimeMillis());
		for (int i = v.size() - 1; i >= 0; i--) {
			if (v.get(i).getDateCreated().getMonth() == date.getMonth()
					&& v.get(i).getDateCreated().getDate() >= date.getDate() - 7
					&& v.get(i).getDateCreated().getDate() <= date.getDate()
					&& v.get(i).getDateCreated().getYear() == date.getYear()) {
				count[7 - (date.getDate() - v.get(i).getDateCreated().getDate())]++;
			} else
				break;

		}
		for (int i = 0; i < count.length; i++) {
			LocalDate date1 = LocalDate.now().minusDays(7-i);
			ChartData newData = new ChartData(date1.getMonth().toString() + ", " + date1.getDayOfMonth(), count[i], Tile.BLUE);
			newData.setValue(count[i]);
			weeklyData.add(newData);
		}
		return weeklyData;

	}

	private void initializeDayTile() {
		dayTile = TileBuilder.create().skinType(SkinType.DATE).textSize(TextSize.BIGGER).prefSize(TILE_WIDTH, TILE_HEIGHT).build();

	}

	private void initializeXDPTile() {
		xdpTile = TileBuilder.create().skinType(SkinType.IMAGE).prefSize(TILE_WIDTH, TILE_HEIGHT)
				.title("XML Diff and Patch App").textSize(TextSize.BIGGER).image(new Image(("icon-main@3x.png")))
				.textAlignment(TextAlignment.CENTER).build();

	}

	private void initializeBindings() {
		mainTabPane.prefWidthProperty().bind(mainAnchor.widthProperty());
		mainTabPane.prefHeightProperty().bind(mainAnchor.heightProperty());

		anchorUser.prefWidthProperty().bind(mainTabPane.widthProperty());
		anchorUser.prefHeightProperty().bind(mainTabPane.heightProperty().subtract(60));

		homeTab.prefWidthProperty().bind(mainTabPane.widthProperty());
		homeTab.prefHeightProperty().bind(mainTabPane.heightProperty().subtract(60));

		pane.prefWidthProperty().bind(mainTabPane.widthProperty());
		pane.prefHeightProperty().bind(mainTabPane.heightProperty().add(-60));

		tableview.prefWidthProperty().bind(mainTabPane.widthProperty());
		tableview.prefHeightProperty().bind(mainTabPane.heightProperty().add(-57));

		dateCol.prefWidthProperty().bind(mainTabPane.widthProperty().subtract(verCol.widthProperty())
				.subtract(getCol.widthProperty()).subtract(queryCol.widthProperty()).subtract(10).divide(6.1));
		authorCol.prefWidthProperty().bind(mainTabPane.widthProperty().subtract(verCol.widthProperty())
				.subtract(getCol.widthProperty()).subtract(queryCol.widthProperty()).subtract(10).divide(6.1));
		sizeCol.prefWidthProperty().bind(mainTabPane.widthProperty().subtract(verCol.widthProperty())
				.subtract(getCol.widthProperty()).subtract(queryCol.widthProperty()).subtract(10).divide(6.1));
		diffCol.prefWidthProperty().bind(mainTabPane.widthProperty().subtract(verCol.widthProperty())
				.subtract(getCol.widthProperty()).subtract(queryCol.widthProperty()).subtract(10).divide(6.1));
		simCol.prefWidthProperty().bind(mainTabPane.widthProperty().subtract(verCol.widthProperty())
				.subtract(getCol.widthProperty()).subtract(queryCol.widthProperty()).subtract(10).divide(6.1));
		statCol.prefWidthProperty().bind(mainTabPane.widthProperty().subtract(verCol.widthProperty())
				.subtract(getCol.widthProperty()).subtract(queryCol.widthProperty()).subtract(10).divide(6.1));

		queryTab.prefWidthProperty().bind(mainTabPane.widthProperty());
		queryTab.prefHeightProperty().bind(mainTabPane.heightProperty().subtract(60));

		versionLabel.setLayoutX(0);
		versionLabel.translateXProperty().bind(mainTabPane.widthProperty().multiply(59.0 / 800));

		versionBox.setLayoutX(0);
		// versionBox.setLayoutY(0);
		versionBox.translateXProperty().bind(versionLabel.translateXProperty().add(80.0));
		// versionBox.translateYProperty().bind(mainTabPane.heightProperty().subtract(60).multiply(25.0/390));

		deltaOnly.setLayoutX(0);
		// deltaOnly.setLayoutY(0);
		deltaOnly.translateXProperty().bind(mainTabPane.widthProperty().multiply(620.0 / 800));
		// deltaOnly.translateYProperty().bind(mainTabPane.heightProperty().subtract(60).multiply(0.0641));

		advancedSearch.setLayoutX(0);
		// advancedSearch.setLayoutY(0);
		advancedSearch.translateXProperty().bind(mainTabPane.widthProperty().multiply(343.0 / 800));
		// advancedSearch.translateYProperty().bind(mainTabPane.heightProperty().subtract(60).multiply(12.0/390));

		filter1.setLayoutX(0);
		// filter1.setLayoutY(0);
		filter1.translateXProperty().bind(mainTabPane.widthProperty().multiply(30.0 / 800));
		filter1.prefWidthProperty().bind(mainTabPane.widthProperty().multiply(125.0 / 800));
		// filter1.translateYProperty().bind(mainTabPane.heightProperty().subtract(60).multiply(81.0/390));
		//
		//// versionBox.setLayoutX(0);
		//// versionBox.setLayoutY(0);
		//// filter1.translateXProperty().bind(mainTabPane.widthProperty().multiply(30.0/800));
		//// filter1.translateYProperty().bind(mainTabPane.heightProperty().subtract(60).multiply(81.0/390));
		//
		filter2.setLayoutX(0);
		// filter2.setLayoutY(0);
		filter2.translateXProperty().bind(mainTabPane.widthProperty().multiply(165.0 / 800));
		filter2.prefWidthProperty().bind(mainTabPane.widthProperty().multiply(125.0 / 800));
		// filter2.translateYProperty().bind(mainTabPane.heightProperty().subtract(60).multiply(81.0/390));

		filter3.setLayoutX(0);
		// filter3.setLayoutY(0);
		filter3.translateXProperty().bind(mainTabPane.widthProperty().multiply(301.0 / 800));
		filter3.prefWidthProperty().bind(mainTabPane.widthProperty().multiply(125.0 / 800));
		// filter3.translateYProperty().bind(mainTabPane.heightProperty().subtract(60).multiply(81.0/390));

		filter4.setLayoutX(0);
		// filter4.setLayoutY(0);
		filter4.translateXProperty().bind(mainTabPane.widthProperty().multiply(437.0 / 800));
		filter4.prefWidthProperty().bind(mainTabPane.widthProperty().multiply(125.0 / 800));
		// filter4.translateYProperty().bind(mainTabPane.heightProperty().subtract(60).multiply(81.0/390));

		searchBar.setLayoutX(0);
		// searchBar.setLayoutY(0);
		searchBar.translateXProperty().bind(mainTabPane.widthProperty().multiply(27.0 / 800));
		searchBar.prefWidthProperty().bind(mainTabPane.widthProperty().multiply(547.0 / 800));
		// searchBar.translateYProperty().bind(mainTabPane.heightProperty().subtract(60).multiply(81.0/390));

		queryBtn.setLayoutX(0);
		// queryBtn.setLayoutY(0);
		queryBtn.translateXProperty().bind(mainTabPane.widthProperty().multiply(615.0 / 800));
		// queryBtn.translateYProperty().bind(mainTabPane.heightProperty().subtract(60).multiply(87.0/390));
		////
		//// queryBtn.translateXProperty().bind(mainTabPane.widthProperty().multiply(615.0/800));
		//// queryBtn.translateYProperty().bind(mainTabPane.heightProperty().subtract(60).multiply(87.0/390));
		////
		// queryResult.setLayoutX(0);
		// queryResult.setLayoutY(0);
		// queryResult.translateXProperty().bind(mainTabPane.widthProperty().multiply(20.0/800));
		// queryResult.translateYProperty().bind(mainTabPane.heightProperty().subtract(60).multiply(146.0/390));

		// verCol.prefWidthProperty().bind(mainTabPane.widthProperty().divide(9));
		// verCol.prefWidthProperty().bind(mainTabPane.widthProperty().divide(9));
		// verCol.prefWidthProperty().bind(mainTabPane.widthProperty().divide(9));
		// col

		queryResult.prefWidthProperty().bind(mainTabPane.widthProperty().subtract(38));
		queryResult.prefHeightProperty().bind(mainTabPane.heightProperty().subtract(60).subtract(165));

	}

	private void initializeNumberTile() {
		numberTile = TileBuilder.create().skinType(SkinType.NUMBER).prefSize(TILE_WIDTH, TILE_HEIGHT)
				.title("Number of Versions").textSize(TextSize.BIGGER)
				// .text("Whatever text")
				.value(project.versions.size())
				// .unit("versions")
				.description("Versions").textVisible(true).build();

	}

	public void initializeHomeTile() {
		homeTile = TileBuilder.create().skinType(SkinType.IMAGE).prefSize(TILE_WIDTH, TILE_HEIGHT)
				.title("Return to HomePage").textSize(TextSize.BIGGER).image(new Image("home1.png"))
				// .backgroundColor(Color.TRANSPARENT)
				.build();
	}

	public void initializeVersionPane() {
		addVersionTile = TileBuilder.create().skinType(SkinType.IMAGE).prefSize(TILE_WIDTH, TILE_HEIGHT)
				.title("Add New Version").textSize(TextSize.BIGGER).image(new Image("images/addVersion.png"))
				.backgroundImageKeepAspect(true)
				// .backgroundColor(Color.TRANSPARENT)
				.build();
	}

	public void initializeTilePressed() throws MalformedURLException {
		URL url = new URL("https://github.com/georgioyammine/XML-Data-Warehousing");
		directoryTile.setOnMouseClicked(event -> {
			try {
				Desktop.getDesktop().browse(new File(projectPath).toURI());
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(projectPath);
		});
		gitHubTile.setOnMouseClicked(event -> {
			try {
				Desktop.getDesktop().browse(url.toURI());
			} catch (IOException | URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		homeTile.setOnMouseClicked(event -> {
			try {
				Parent root = FXMLLoader.load(getClass().getResource("welcomeScreen.fxml"));
				Scene scene = new Scene(root, 800, 450);
				Stage window = (Stage) ((tableview.getScene().getWindow()));
				window.setResizable(false);
				window.setScene(scene);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		addVersionTile.setOnMouseClicked(event -> {
			try {
				addVersionHandler();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		xdpTile.setOnMouseClicked(event -> {
			try {
				Parent root = FXMLLoader.load(getClass().getResource("mainScene.fxml"));
				Scene scene = new Scene(root, 800, 450);
				Stage window = (Stage) ((tableview.getScene().getWindow()));
				window.setResizable(false);
				window.setScene(scene);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

	}

	private void initializeInfo() {
		nameTile = TileBuilder.create().skinType(SkinType.TEXT).prefSize(TILE_WIDTH, TILE_HEIGHT).title("Project Name")
				.description(project.getName()).textSize(TextSize.BIGGER).descriptionAlignment(Pos.CENTER).backgroundImage(new Image("images/project.png"))
				.backgroundImageOpacity(0.5)
				.textVisible(true).build();
		authorTile = TileBuilder.create().
				skinType(SkinType.TEXT).
				prefSize(TILE_WIDTH, TILE_HEIGHT)
				.title("Project Author").
				description(project.getOwner())
				.textSize(TextSize.BIGGER)
				.backgroundImage(new Image("images/user.png"))
				.backgroundImageOpacity(0.5)
				.descriptionAlignment(Pos.CENTER)
				.customFont(new Font(100))
				.customFontEnabled(true)
				.textVisible(true).build();
	}

	private void initializeStoragePane() {

		double spaceSaved = getSavedSpace();
		storageTile = TileBuilder.create().skinType(SkinType.BAR_GAUGE).prefSize(TILE_WIDTH, TILE_HEIGHT).minValue(0)
				.maxValue(100).startFromZero(true).title("Space Saved").textSize(TextSize.BIGGER)
				.unit("%")
				.gradientStops(new Stop(0, Bright.RED), new Stop(0.1, Bright.RED), new Stop(0.2, Bright.ORANGE_RED),
						new Stop(0.3, Bright.ORANGE), new Stop(0.4, Bright.YELLOW_ORANGE), new Stop(0.5, Bright.YELLOW),
						new Stop(0.6, Bright.GREEN_YELLOW), new Stop(0.7, Bright.GREEN),
						new Stop(0.8, Bright.BLUE_GREEN), new Stop(1.0, Dark.BLUE))
				.strokeWithGradient(true).animated(true).build();
//		storageTile.setValue(spaceSaved);
//		System.out.println(spaceSaved);
	}

	private double getSavedSpace() {
		long originalSize = 0;
		long directorySize = FileUtils.sizeOfDirectory(new File(projectPath))
				- FileUtils.sizeOfDirectory(new File(projectPath + File.separator + "output"));
		for (Version v : project.versions)
			originalSize += v.getSizeInBytes();
		double spaceSaved = (1 - (directorySize / (double) originalSize)) * 100;
		if(spaceSaved == Double.NEGATIVE_INFINITY)
			return 0;
		return spaceSaved;
	}

	private void updateDirectoryPane() {
		directoryTile = TileBuilder.create().skinType(SkinType.IMAGE).prefSize(TILE_WIDTH, TILE_HEIGHT)
				.title("Open Project Directory").textSize(TextSize.BIGGER).image(new Image("folder2.png"))
				// .backgroundColor(Color.TRANSPARENT)
				.build();
	}

	private void updateGitHubPane() {
		gitHubTile = TileBuilder.create().skinType(SkinType.IMAGE).prefSize(TILE_WIDTH, TILE_HEIGHT)
				.title("Open GitHub").textSize(TextSize.BIGGER).image(new Image("githubw.png"))
				// .backgroundColor(Color.TRANSPARENT)
				.build();
	}

	private void initializeCalendarData() {
		List<ChartData> calendarData = getCalendarData();
		calendarTile = TileBuilder.create().skinType(SkinType.CALENDAR).prefSize(TILE_WIDTH, TILE_HEIGHT)
				.title("Calendar").textSize(TextSize.BIGGER).chartData(calendarData).build();

	}

	private List<ChartData> getCalendarData() {
		ZonedDateTime now = ZonedDateTime.now();
		List<ChartData> calendarData = new ArrayList<ChartData>();
		Date date = new Date(System.currentTimeMillis());

		for (int i = 0; i < project.versions.size(); i++) {
			if (project.versions.get(i).getDateCreated().getMonth() == date.getMonth()) {
				calendarData.add(new ChartData("Item " + (i + 1),
						now.plusDays((long) (project.versions.get(i).getDateCreated().getDate()) - 1).toInstant()));
			}
		}
		return calendarData;
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
			NodeList nodeList = (NodeList) xPath.compile(expression.toString()).evaluate(root, XPathConstants.NODESET);
			for (int i = 0; i < nodeList.getLength(); i++)
				possibleWordsSet.add("/" + nodeList.item(i).getNodeName());

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
		if (versionBox.getValue() == null)
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
		if (filter1.getItems().isEmpty()) {
			filter2.getItems().clear();
			filter2.setVisible(false);
			return;
		}
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
		if (filter2.getItems().isEmpty()) {
			filter3.getItems().clear();
			filter3.setVisible(false);
			return;
		}
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
		if (filter3.getItems().isEmpty()) {
			filter4.getItems().clear();
			filter4.setVisible(false);
			return;
		}
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
					System.out.println("Hi");
					// getTreeView(xmlString);

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

				NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(queryingVersionRoot,
						XPathConstants.NODESET);
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					String dotRep = getDotPosition(node);
					sb.append("//" + dotRep + " | ");

					// System.out.println(node + " " + dotRep);
				}
				sb.delete(sb.length() - 3, sb.length());
				System.out.println(sb.toString());

				String diffPath = projectPath + File.separator + getDiffRelativePath((int) versionBox.getValue());
				Node reversedDiff = XMLDiffAndPatch.reverseXMLESNode(diffPath);
				// Util.print(reversedDiff, 0);

				Document newXmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
				Element root = newXmlDocument.createElement("Results");
				if (!sb.toString().isEmpty()) {

					Node updNode = (Node) xPath.compile("//Update").evaluate(reversedDiff, XPathConstants.NODE);
					if (updNode != null) {
						Document updDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
						updDoc.appendChild(updDoc.importNode(updNode, true));
						Node node = newXmlDocument.createElement("Update");
						NodeList results = (NodeList) xPath.compile(sb.toString()).evaluate(updDoc,
								XPathConstants.NODESET);
						for (int i = 0; i < results.getLength(); i++) {
							node.appendChild(newXmlDocument.importNode(results.item(i), true));
						}
						root.appendChild(newXmlDocument.importNode(node, true));
					}
					Node delNode = (Node) xPath.compile("//Delete").evaluate(reversedDiff, XPathConstants.NODE);
					if (delNode != null) {
						Document updDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
						updDoc.appendChild(updDoc.importNode(delNode, true));
						Node node = newXmlDocument.createElement("Delete");
						NodeList results = (NodeList) xPath.compile(sb.toString()).evaluate(updDoc,
								XPathConstants.NODESET);
						for (int i = 0; i < results.getLength(); i++) {
							node.appendChild(newXmlDocument.importNode(results.item(i), true));
						}
						root.appendChild(newXmlDocument.importNode(node, true));
					}
				}

				// NodeList nodeList2 = (NodeList)
				// xPath.compile(sb.toString()).evaluate(reversedDiff, XPathConstants.NODESET);
				// System.out.println(nodeList2.getLength());

				newXmlDocument.appendChild(root);
				//
				// for (int i = 0; i < nodeList2.getLength(); i++) {
				// Node node = nodeList2.item(i);
				// Node copyNode = newXmlDocument.importNode(node, true);
				// root.appendChild(copyNode);
				// System.out.println("Node:"+node);
				// }
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

				NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(queryingVersionRoot,
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
			}
		}
	}

	public String getDotPosition(Node node) {
		Stack<Node> stack = new Stack<Node>();

		while (node.getParentNode() != null) {
			stack.add(node);
			node = node.getParentNode();
		}
		node = stack.pop();
		StringBuilder sb = new StringBuilder("B");
		while (!stack.isEmpty()) {
			System.out.println(node);
			System.out.println(stack);
			System.out.println(sb);
			NodeList nodeList = node.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				if (nodeList.item(i).isEqualNode(stack.peek())) {
					node = stack.pop();
					sb.append("." + (i + 1));
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
						
						calendarTile.setChartData(getCalendarData());
						numberTile.setValue(project.getNumberOfVersions());
						storageTile.setValue(getSavedSpace());
					});
					return null;

				}
			};
		}
	};
	@FXML
	JFXTabPane mainTabPane;
	@FXML
	AnchorPane mainAnchor;
	@FXML
	AnchorPane homeTab;
	@FXML
	AnchorPane versionTab;
	@FXML
	AnchorPane queryTab;
	@FXML
	Label versionLabel;

	private void updateInfo() {
		// TODO Auto-generated method stub
		// name.setText(project.getName());
		// author.setText(project.getOwner());
		// nbOfVersions.setText(project.versions.size() + "");
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

	public void getTreeView(String result) {
		Document xmlDoc = convertStringToXMLDocument(result);
		Node root = (Node) xmlDoc.getElementsByTagName("Result");
		NodeList nodeList = root.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++)
			System.out.println(nodeList.item(i).getNodeName());
		System.out.println("Hi");
	}

	private static Document convertStringToXMLDocument(String xmlString) {
		// Parser that produces DOM object trees from XML content
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		// API to obtain DOM Document instance
		DocumentBuilder builder = null;
		try {
			// Create DocumentBuilder with default configuration
			builder = factory.newDocumentBuilder();

			// Parse the content to Document object
			Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
