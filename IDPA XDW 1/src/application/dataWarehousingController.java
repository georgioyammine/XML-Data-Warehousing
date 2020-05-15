package application;

import static java.time.temporal.ChronoUnit.DAYS;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.ConcurrentSkipListMap;

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
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.controls.JFXTreeTableView;

import eu.hansolo.fx.dotmatrix.DotMatrix;
import eu.hansolo.fx.dotmatrix.DotMatrix.DotShape;
import eu.hansolo.fx.dotmatrix.DotMatrixBuilder;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.Tile.TextSize;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.addons.Indicator;
import eu.hansolo.tilesfx.chart.ChartData;
import eu.hansolo.tilesfx.chart.RadarChart.Mode;
import eu.hansolo.tilesfx.colors.Bright;
import eu.hansolo.tilesfx.colors.Dark;
import eu.hansolo.tilesfx.skins.LeaderBoardItem;
import eu.hansolo.tilesfx.tools.FlowGridPane;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;

public class dataWarehousingController<T> {

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
	// @FXML
	// AnchorPane dotPane;
	@FXML
	JFXTextField newUserField;
	@FXML
	JFXComboBox<String> userBox;
	@FXML
	JFXButton createBtn;
	@FXML
	JFXButton addUserBtn;
	@FXML
	Label userVerifyLabel;
	@FXML
	StackPane dotMatrixPane;
	@FXML
	ImageView userIcon;

	private Tile gitHubTile;
	private Tile directoryTile;
	private Tile calendarTile;
	private Tile storageTile;
	private Tile homeTile;
	private Tile addVersionTile;
	private Tile numberTile;
	private Tile clockTile;
	private Tile nameTile;
	private Tile authorTile;
	private Tile xdpTile;
	private Tile matrixTile;
	private Tile leaderBoardTile;
	private Tile quoteTile;
	private Tile changesTile;

	private DotMatrix dotMatrix;

	private final double TILE_WIDTH = 640;
	private final double TILE_HEIGHT = 640;
	FlowGridPane pane;

	private long lastTimerCall;
	private AnimationTimer timer;
	private String[] quotes = new String[] { "Coding will be done", "To code or not to code...",
			"Readying for the Code", "Adding the I to the DE", "Get some coding done", "Click, start, code...",
			"On your mark, get set, CODE!", "Release the coding beast", "Cool Developers use only $product",
			"Code with your brain, debug with your heart", "\"Insert cool phrase about coding HERE\"",
			"Are you ready for some Dev fun?", "Keep it up, keep it in style",
			"Modern coding has never looked so modern", "You only live once, so get coding!",
			"Developers will develop... Oh! And they eat too.", "Don't judge a code by its bugs",
			"Every developer is an artist", "Code never lies", "To debug or not to debug... meh, just System.out",
			"Develop like your life is paid by it", "Your worst code is another coder's best",
			"Algorithms are made for the weak", "Coding at night brings morning delight",
			"Coding to commence in 3, 2, ...", "Coding tunnel approaching... duck your head",
			"Why code when you can code()", "All computers wait() at the same speed",
			"Chuck Norris counted to infinity... twice", "Code makes very fast, very accurate mistakes",
			"\"It works on my machine\"", "There's no test like production",
			"To err is human, but for a real disaster you need a computer",
			"Weeks of coding can save you hours of planning", "One man's constant is another man's variable",
			"ASCII stupid question, get a stupid ANSI", "A coder looks both ways before crossing a one-way street",
			"Hey! It compiles! Ship it!", "Experience is the name everyone gives to their mistakes",
			"Beta is Latin for still doesn't work", "There is no place like 127.0.0.1",
			"There is nothing quite so permanent as a quick fix", "/* TODO: Remove this comment */",
			"One man's prototype is a manager's product", "Truth can only be found in one place: the code",
			"It is what it is because you let it be so",
			"Programming isn't what you know; it's what you can figure out", "Think twice, code once",
			"First solve the problem - then, write the code",
			"Tests are stories we tell the next generation of programmers",
			"Coding is breaking one impossible task into many small possible tasks",
			"Premature optimization is the root of all evil", "Don't get distracted by the noise",
			"Simplicity is prerequisite for reliability", "Programming is the art of doing one thing at a time",
			"They don't make bugs like Bunny anymore", "Talk is cheap - show me the code",
			"'Programming' is a four-letter word", "Marketing is the observe() of programming",
			"Controlling complexity is the essence of computer programming",
			"The function of good software is to make the complex appear simple",
			"There are only two industries that refer to their customers as 'users'",
			"Good code is its own best documentation", "There are two ways to code bug-free; only the third one works",
			"640K ought to be enough for anybody", "Before software can be reusable it first has to be usable",
			"It's not a bug – it's an undocumented feature", "Deleted code is debugged code",
			"In order to understand recursion, one must first understand recursion", "Blame doesn't fix bugs",
			"Programming languages are all the same; you just need logic",
			"When the budget is low, go after the low hanging fruit",
			"All software boils down to pure binary - it works or it doesn't",
			"Document what you know when you know it", "\"Given enough time, I can meet any software deadline\"",
			"Computer Science: solving today's problems tomorrow", "If at first you don't succeed, call it version 1.0",
			"You are making progress if each mistake is a new one",
			"Compatible: Gracefully accepts erroneous data from any source",
			"programmer, n. an organism that can turn caffeine into code",
			"Any technology distinguishable from magic is insufficiently advanced",
			"The thing about UDP jokes is I don't care if you get them or not", "All generalizations are bad",
			"The longer a bug takes to fix, the smaller the fix is",
			"A bug in the code is worth two in the documentation",
			"All programmers are playwrights and all computers are lousy actors",
			"If the code and the comments disagree, then both are probably wrong", "For Tech support : CTRL+ALT+DEL",
			"The code is dark and full of terrors -Dave K" };

	JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
	ArrayList<String> words = new ArrayList<String>(); // Used to populate auto-completion search bar;

	File file;
	HashSet<String> possibleWordsSet = new HashSet<String>();
	HashSet<String> allNodesInVersion = new HashSet<String>();
	HashSet<String> userSet = new HashSet<String>();
	HashMap<String, Integer> leaderMap = new HashMap<>();

	String versionPath;
	String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();

	final Clipboard clipboard = Clipboard.getSystemClipboard();
	final ClipboardContent content = new ClipboardContent();
	static DataWarehousing project;
	static String projectPath;
	Element queryingVersionRoot;
	ArrayList<LeaderBoardItem> leaderList = new ArrayList<LeaderBoardItem>();

	@FXML
	public void applySettingsHandle() {
	}

	@FXML
	public void copyHandle() {
	}

	@FXML
	public void initialize() throws IOException {
		long start = System.currentTimeMillis();
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
							versionBox.getSelectionModel().select(data.getVersion() - 1);
							autoCompletePopup.hide();
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
		initializeLeaderBoardTile();
		initializeQuoteTile();
		initializeRadarChart();
		initializeChangesTile();
		initializeDotMatrix();
		aboutText.setEditable(false);
		// getVersionsLastAccessedTime();

		newUserField.setVisible(false);
		addUserBtn.setVisible(false);

		lastTimerCall = System.nanoTime();
		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (now > lastTimerCall + 10_000_000_000L) {
					quoteTile.setDescription(quotes[(int) (Math.random() * quotes.length)]);
					// leaderBoardTile.getLeaderBoardItems().get(1).setValue(10);
					lastTimerCall = now;
				}
			}
		};
		timer.start();

		System.out.println("Initialization: " + (System.currentTimeMillis() - start) + "ms");

//		TileBuilder.create().skinType(SkinType.PERCENTAGE).prefSize(TILE_WIDTH, TILE_HEIGHT).title("Percentage Tile")
//				.unit("%").description("Test").maxValue(60).build();

		clockTile = TileBuilder.create().skinType(SkinType.CLOCK).textSize(TextSize.BIGGER)
				.prefSize(TILE_WIDTH, TILE_HEIGHT).title("Clock Tile").running(true).build();

		pane = new FlowGridPane(6, 3, clockTile, numberTile, storageTile, directoryTile, addVersionTile, changesTile,
				calendarTile, matrixTile, nameTile, authorTile, leaderBoardTile, quoteTile, xdpTile, gitHubTile,
				homeTile);
		pane.setPrefSize(homeTab.getPrefWidth(), homeTab.getPrefHeight());
		pane.setAlignment(Pos.CENTER);
		pane.setCenterShape(true);
		pane.setHgap(5);
		pane.setVgap(5);
		pane.setPadding(new Insets(5, 5, 5, 5));
		pane.setBackground(new Background(new BackgroundFill(Color.web("#101214"), CornerRadii.EMPTY, Insets.EMPTY)));
		homeTab.getChildren().add(pane);
		dotMatrixPane.getChildren().add(dotMatrix);

		initializeTilePressed();
		initializeBindings();
		populateUserBox();
		initializeContribution();

		Platform.runLater(() -> {
			authorTile.widthProperty().addListener((obs, oldVal, newVal) -> {
				if (matrixTile != null) {
					matrixTile.setChartData(getWeeklyData());
				}
				if (storageTile != null) {
					storageTile.setValue(getSavedSpace());
				}
			});

			authorTile.heightProperty().addListener((obs, oldVal, newVal) -> {
				if (matrixTile != null) {
					matrixTile.setChartData(getWeeklyData());
				}
				if (storageTile != null) {
					storageTile.setValue(getSavedSpace());
				}
			});
		});

	}

	private void populateUserBox() {
		userSet.add(project.getOwner());
		for (int i = 0; i < project.versions.size(); i++) {
			userSet.add(project.versions.get(i).getOwner());
		}
		userBox.getItems().addAll(userSet);
		userBox.getSelectionModel().select(project.getOwner());
	}

	@FXML
	public void createUserHandle() {
		if (createBtn.getText().equals("Go Back")) {
			userBox.setVisible(true);
			newUserField.setVisible(false);
			addUserBtn.setVisible(false);
			createBtn.setText("Create New User");
			userVerifyLabel.setText("");
		} else {
			userBox.setVisible(false);
			newUserField.setVisible(true);
			addUserBtn.setVisible(true);
			createBtn.setText("Go Back");
		}

	}

	@FXML
	public void userBoxHandle() {
		tooltip.hide();
		service.cancel();
		initializeContribution();
//		updateContributionData();
	}

	@FXML
	public void addUserHandle() {
		String newUser = newUserField.getText().trim();
		if (newUser.length() != 0) {
			userSet.add(newUser);
			userBox.getItems().clear();
			userBox.getItems().addAll(userSet);
			userBox.setVisible(true);
			newUserField.setVisible(false);
			addUserBtn.setVisible(false);
			createBtn.setText("Create New User");
			userBox.getSelectionModel().select(newUser);
		} else {
			userVerifyLabel.setText("User Field is empty. Enter User.");
		}
		newUserField.clear();
	}

	private void initializeDotMatrix() {
		dotMatrix = DotMatrixBuilder.create().prefSize(TILE_WIDTH, TILE_HEIGHT).colsAndRows(53, 7)
				.dotOnColor(Color.BLUE).dotOffColor(Color.GRAY).dotShape(DotShape.ROUNDED_RECT).build();
	}

	private void initializeChangesTile() {
		Indicator leftGraphics = new Indicator(Tile.RED);
		leftGraphics.setOn(true);

		Indicator middleGraphics = new Indicator(Tile.YELLOW);
		middleGraphics.setOn(true);

		Indicator rightGraphics = new Indicator(Tile.GREEN);
		rightGraphics.setOn(true);

		changesTile = TileBuilder.create().skinType(SkinType.STATUS).prefSize(TILE_WIDTH, TILE_HEIGHT)
				.description("Changes").leftText("Deletions").middleText("Updates").rightText("Insertions")
				.leftGraphics(leftGraphics).middleGraphics(middleGraphics).rightGraphics(rightGraphics)
				.text("Based on last version only").build();
		updateChangesTile();

	}

	private void updateChangesTile() {
		if(project.versions.size() > 1) {
		String diffPath = projectPath + File.separator + getDiffRelativePath((int) project.versions.size());
		Node reversedDiff = XMLDiffAndPatch.reverseXMLESNode(diffPath);
		reversedDiff = ((Element) reversedDiff).getElementsByTagName("Edit_Script").item(0);

		Node update = null;
		Node delete = null;
		Node insert = null;
		NodeList childs = ((Element) reversedDiff).getChildNodes();
		for (int i = 0; i < childs.getLength(); i++) {
			if (childs.item(i).getNodeName().equals("Update"))
				update = childs.item(i);
			if (childs.item(i).getNodeName().equals("Delete"))
				delete = childs.item(i);
			if (childs.item(i).getNodeName().equals("Insert"))
				insert = childs.item(i);
		}

		if (update != null) {
			changesTile.setMiddleValue(update.getChildNodes().getLength());
		}
		if (delete != null) {
			changesTile.setLeftValue(delete.getChildNodes().getLength());
		}
		if (insert != null) {
			changesTile.setRightValue(insert.getChildNodes().getLength());
		}
		}
	}

	private ArrayList<FileTime> getVersionsLastAccessedTime() {
		ArrayList<FileTime> fileTimes = new ArrayList<FileTime>();

		try {
			for (int i = 0; i < project.versions.size(); i++) {
				Path file = Paths.get(project.versions.get(i).getRelativePath());
				BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);
				FileTime time = attrs.lastAccessTime();
				fileTimes.add(time);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(fileTimes);
		return fileTimes;
	}

	private void initializeRadarChart() {
		ChartData chartData1 = new ChartData("Item 1", 24.0, Tile.GREEN);
		ChartData chartData2 = new ChartData("Item 2", 10.0, Tile.BLUE);
		ChartData chartData3 = new ChartData("Item 3", 12.0, Tile.RED);
		ChartData chartData4 = new ChartData("Item 4", 13.0, Tile.YELLOW_ORANGE);
		ChartData chartData5 = new ChartData("Item 5", 13.0, Tile.BLUE);
		ChartData chartData6 = new ChartData("Item 6", 13.0, Tile.BLUE);
		ChartData chartData7 = new ChartData("Item 7", 13.0, Tile.BLUE);
		ChartData chartData8 = new ChartData("Item 8", 13.0, Tile.BLUE);
		TileBuilder.create().skinType(SkinType.RADAR_CHART).prefSize(TILE_WIDTH, TILE_HEIGHT).minValue(0).maxValue(50)
				.title("RadarChart Polygon").unit("Unit").radarChartMode(Mode.POLYGON)
				.gradientStops(new Stop(0.00000, Color.TRANSPARENT), new Stop(0.00001, Color.web("#3552a0")),
						new Stop(0.09090, Color.web("#456acf")), new Stop(0.27272, Color.web("#45a1cf")),
						new Stop(0.36363, Color.web("#30c8c9")), new Stop(0.45454, Color.web("#30c9af")),
						new Stop(0.50909, Color.web("#56d483")), new Stop(0.72727, Color.web("#9adb49")),
						new Stop(0.81818, Color.web("#efd750")), new Stop(0.90909, Color.web("#ef9850")),
						new Stop(1.00000, Color.web("#ef6050")))
				.text("Test").chartData(chartData1, chartData2, chartData3, chartData4, chartData5, chartData6,
						chartData7, chartData8)
				.tooltipText("").animated(true).smoothing(true).build();

	}

	private void initializeQuoteTile() {
		quoteTile = TileBuilder.create().skinType(SkinType.TEXT).prefSize(TILE_WIDTH, TILE_HEIGHT).title(" ")
				.textSize(TextSize.BIGGER).text("Click to copy the quote")
				.description(quotes[(int) (Math.random() * quotes.length)]).descriptionAlignment(Pos.TOP_RIGHT)
				.textVisible(true).build();

	}

	private void initializeMatrixTile() {
		matrixTile = TileBuilder.create().skinType(SkinType.MATRIX).prefSize(TILE_WIDTH, TILE_HEIGHT)
				.title("Number of version per day").textSize(TextSize.BIGGER).textVisible(false).animated(true)
				.matrixSize(8, 20).chartData(getWeeklyData()).maxValue(20).build();

	}

	private void initializeLeaderBoardTile() {
		leaderBoardTile = TileBuilder.create().skinType(SkinType.LEADER_BOARD).prefSize(TILE_WIDTH, TILE_HEIGHT)
				.title("LeaderBoard").text("Based on number of versions added").leaderBoardItems().build();
		updateLeaderBoard();
	}

	private void updateLeaderBoard() {
		leaderMap = new HashMap<String, Integer>();
		leaderMap.put(project.getOwner(), 0);
		for (Version v : project.versions) {
			if (leaderMap.containsKey(v.getOwner()))
				leaderMap.put(v.getOwner(), leaderMap.get(v.getOwner()) + 1);
			else
				leaderMap.put(v.getOwner(), 1);
		}
		ArrayList<LeaderBoardItem> leaderArl = new ArrayList<LeaderBoardItem>();
		Iterator it = leaderMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Integer> pair = (Map.Entry) it.next();
			leaderArl.add(new LeaderBoardItem(pair.getKey(), pair.getValue()));
			it.remove(); // avoids a ConcurrentModificationException
		}
		Collections.sort(leaderArl, new Comparator<LeaderBoardItem>() {

			@Override
			public int compare(LeaderBoardItem o1, LeaderBoardItem o2) {
				return (int) (o2.getValue() - o1.getValue());
			}
		});
		leaderBoardTile.setLeaderBoardItems(leaderArl);

	}

	private void updateLeaderBoard2(String author) {
		for (LeaderBoardItem item : leaderBoardTile.getLeaderBoardItems()) {
			if (item.getName().equals(author)) {
				item.setValue(item.getValue() + 1);
				return;
			}
		}
		// not working
//		leaderBoardTile.getLeaderBoardItems().add(new LeaderBoardItem(userBox.getValue(),1));
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
			LocalDate date1 = LocalDate.now().minusDays(7 - i);
			ChartData newData = new ChartData(date1.getMonth().toString() + ", " + date1.getDayOfMonth(), count[i],
					Tile.BLUE);
			newData.setValue(count[i]);
			weeklyData.add(newData);
		}
		return weeklyData;

	}

	private void initializeDayTile() {
		TileBuilder.create().skinType(SkinType.DATE).textSize(TextSize.BIGGER).prefSize(TILE_WIDTH, TILE_HEIGHT)
				.build();

	}

	private void initializeXDPTile() {
		xdpTile = TileBuilder.create().skinType(SkinType.IMAGE).prefSize(TILE_WIDTH, TILE_HEIGHT)
				.title("XML Diff and Patch App").textSize(TextSize.BIGGER).image(new Image(("images/iconN.png")))
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
		
		userIcon.setLayoutX(0);
		userIcon.translateXProperty().bind(userBox.translateXProperty().add(-150));

		userBox.setLayoutX(0);
		userBox.translateXProperty().bind(anchorUser.widthProperty().multiply(0.5).add(userBox.widthProperty().divide(2).negate()));

		userVerifyLabel.setLayoutX(0);
		userVerifyLabel.translateXProperty().bind(anchorUser.widthProperty().multiply(0.5).add(userVerifyLabel.widthProperty().divide(2).negate()));

		newUserField.setLayoutX(0);
		newUserField.translateXProperty().bind(anchorUser.widthProperty().multiply(0.5).add(newUserField.widthProperty().divide(2).negate()));

		createBtn.setLayoutX(0);
		createBtn.translateXProperty().bind(anchorUser.widthProperty().multiply(0.5).add(createBtn.widthProperty().divide(2).negate()));
		
		addUserBtn.setLayoutX(0);
		addUserBtn.translateXProperty().bind(userBox.translateXProperty().add(232));

		dotMatrixPane.setLayoutX(0);
		dotMatrixPane.translateXProperty().bind(anchorUser.widthProperty().multiply(0.5).add(dotMatrixPane.widthProperty().divide(2).negate()));
		
		aboutTab.prefWidthProperty().bind(mainTabPane.widthProperty());
		aboutTab.prefHeightProperty().bind(mainTabPane.heightProperty().subtract(60));
		
		aboutTitle.setLayoutX(0);
		aboutTitle.translateXProperty().bind((anchorUser.widthProperty()).add(208).divide(2).add(aboutTitle.widthProperty().divide(2).negate()));
		aboutText.prefWidthProperty().bind(anchorUser.widthProperty().add(-260));
//		aboutText.setLayoutX(0);
//		aboutText.translateXProperty().bind((anchorUser.widthProperty().add(-208)).divide(2).add(aboutText.widthProperty().divide(2).negate()));
		
	}
	@FXML Label aboutTitle;
	@FXML JFXTextArea aboutText;
	@FXML AnchorPane aboutTab;
	
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
				.title("Return to HomePage").textSize(TextSize.BIGGER).image(new Image("images/home.png"))
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
				window.setMaximized(false);
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
				Stage window = new Stage();
				window.setTitle("XDP: XML Diff and Patch");
				window.getIcons().add(new Image("icon-main@3x.png"));
				window.setResizable(false);
				window.setScene(scene);
				window.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		quoteTile.setOnMouseClicked(event -> {
			content.putString(quoteTile.getDescription());
			clipboard.setContent(content);
		});

	}

	private void initializeInfo() {
		nameTile = TileBuilder.create().skinType(SkinType.TEXT).prefSize(TILE_WIDTH, TILE_HEIGHT).title("Project Name")
				.description(project.getName()).textSize(TextSize.BIGGER).descriptionAlignment(Pos.CENTER)
				.backgroundImage(new Image("images/project.png")).backgroundImageOpacity(0.5).textVisible(true).build();
		authorTile = TileBuilder.create().skinType(SkinType.TEXT).prefSize(TILE_WIDTH, TILE_HEIGHT)
				.title("Project Author").description(project.getOwner()).textSize(TextSize.BIGGER)
				.backgroundImage(new Image("images/user.png")).backgroundImageOpacity(0.5)
				.descriptionAlignment(Pos.CENTER).customFont(new Font(100)).customFontEnabled(true).textVisible(true)
				.build();
	}

	private void initializeStoragePane() {

		double spaceSaved = getSavedSpace();
		storageTile = TileBuilder.create().skinType(SkinType.BAR_GAUGE).prefSize(TILE_WIDTH, TILE_HEIGHT).minValue(0)
				.maxValue(100).startFromZero(true).title("Space Saved").textSize(TextSize.BIGGER).unit("%")
				.gradientStops(new Stop(0, Bright.RED), new Stop(0.1, Bright.RED), new Stop(0.2, Bright.ORANGE_RED),
						new Stop(0.3, Bright.ORANGE), new Stop(0.4, Bright.YELLOW_ORANGE), new Stop(0.5, Bright.YELLOW),
						new Stop(0.6, Bright.GREEN_YELLOW), new Stop(0.7, Bright.GREEN),
						new Stop(0.8, Bright.BLUE_GREEN), new Stop(1.0, Dark.BLUE))
				.strokeWithGradient(true).animated(true).build();
		if(spaceSaved>0)
			storageTile.setValue(spaceSaved);
		// System.out.println(spaceSaved);
	}

	private double getSavedSpace() {
		long originalSize = 0;
		long directorySize = FileUtils.sizeOfDirectory(new File(projectPath))
				- FileUtils.sizeOfDirectory(new File(projectPath + File.separator + "output"));
		for (Version v : project.versions)
			originalSize += v.getSizeInBytes();
		double spaceSaved = (1 - (directorySize / (double) originalSize)) * 100;
		if (spaceSaved == Double.NEGATIVE_INFINITY)
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
				.title("Open GitHub").textSize(TextSize.BIGGER).image(new Image("images/githubw.png"))
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
			autoCompletePopup.show(searchBar);
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
		if(!searchBar.getText().isEmpty()) {
			searchBar.clear();
			autoCompletePopup.show(searchBar);
		}
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
					// getTreeView(xmlString);

				} catch (Exception e) {
					e.printStackTrace();
					queryResult.setText("Query Invalid!");
				}
			} else {
				// handle querying all the version

				Document newXmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

				Element root = (Element) (project
						.getThisVersionAsNode(project.versions.get((int) versionBox.getValue() - 1)));
				newXmlDocument.appendChild(newXmlDocument.importNode(root,true));

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
			NodeList nodeList = node.getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				if (nodeList.item(i).isEqualNode(stack.peek())) {
					node = stack.pop();
					sb.append("." + (i + 1));
					break;
				}
			}
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
					String owner = userBox.getValue();
					if (project.addNewVersion(owner, file.getAbsolutePath())) {
						System.out.println(System.currentTimeMillis() - t1 + "ms");
						Platform.runLater(() -> {
							saveVersion();
							updateInfo();
							updateChangesTile();
							updateLeaderBoard2(owner);

							calendarTile.setChartData(getCalendarData());
							numberTile.setValue(project.getNumberOfVersions());
							storageTile.setValue(getSavedSpace());
							matrixTile.setChartData(getWeeklyData());
							tooltip.hide();
							service.cancel();
							initializeContribution();
						});
					}
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

	private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MMM");
	private static final DateTimeFormatter WEEKDAY_FORMATTER = DateTimeFormatter.ofPattern("E");
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.YYYY");
	private static final long TOOLTIP_TIMEOUT = 1500;
	private GridPane months;
	private GridPane weekdays;
	private Tooltip tooltip;
	private DotMatrix matrix;
	private HBox legend;
	private Map<Double, Color> colorCoding;
	private Service service;
	private List<DataC> dataList;
	LocalDate now;
	int year;
	int month;
	int lastDay;
	int daysInMonth;

	LocalDate endDate;
	LocalDate startDate;
	long daysInRange;
	int weeks;

	public void initializeContribution() {
		
		
		service = new ProcessService();
		service.setOnSucceeded(e -> {
			tooltip.hide();
			service.reset();
		});

		colorCoding = new ConcurrentSkipListMap<>(Comparator.reverseOrder()); // reverse natural order
		colorCoding.put(3.9, Color.web("#1F6823"));
		colorCoding.put(2.9, Color.web("#45A340"));
		colorCoding.put(1.9, Color.web("#8CC665"));
		colorCoding.put(0.9, Color.web("#D6E685"));
		colorCoding.put(0.0, Color.web("#EEEEEE"));

		now = LocalDate.now();
		year = now.getYear();
		month = now.getMonthValue();
		lastDay = (int) now.getDayOfWeek().getValue();
		daysInMonth = YearMonth.of(year - 1, month).lengthOfMonth();

		endDate = LocalDate.now().plusDays(7 - LocalDate.now().getDayOfWeek().getValue());
		startDate = endDate.minusYears(1).plusDays(7 - endDate.minusYears(1).getDayOfWeek().getValue());
		daysInRange = DAYS.between(startDate, endDate);
		weeks = (int) Math.ceil(daysInRange / 7.0);

		months = new GridPane();
		for (int i = 0; i < 13; i++) {
			Label monthLabel = new Label(MONTH_FORMATTER.format(startDate.plusMonths(i)));
			months.add(monthLabel, i, 0);
			GridPane.setHgrow(monthLabel, Priority.ALWAYS);
			GridPane.setHalignment(monthLabel, HPos.CENTER);
		}

		weekdays = new GridPane();
		for (int i = 0; i < 7; i++) {
			if(i%2 != 0) {
			Label dayLabel = new Label(WEEKDAY_FORMATTER.format(startDate.plusDays(i)));
			dayLabel.setFont(Font.font(9));
			weekdays.add(dayLabel, 0, i);
			GridPane.setVgrow(dayLabel, Priority.ALWAYS);
			GridPane.setValignment(dayLabel, VPos.CENTER);
			}
			else {
				Label dayLabel = new Label("");
				if(i==0)
					dayLabel.setFont(Font.font(10));
				if(i==2)
					dayLabel.setFont(Font.font(4));
				if(i==4)
					dayLabel.setFont(Font.font(2.5));
				weekdays.add(dayLabel, 0, i);
				GridPane.setVgrow(dayLabel, Priority.ALWAYS);
				GridPane.setValignment(dayLabel, VPos.CENTER);
			}
		}

		

		matrix = DotMatrixBuilder.create().prefSize(600, 80).colsAndRows(weeks, 7).useSpacer(true)
				// .spacerSizeFactor(0.1)
				.dotShape(DotShape.SQUARE).dotOffColor(Color.web("#EEEEEE")).build();
		
		tooltip = new Tooltip("");
		Tooltip.install(matrix, tooltip);
	
		
		matrix.setOnDotMatrixEvent(e -> {

			DataC selectedData = dataList.stream().filter(data -> data.x == e.getX() && data.y == e.getY()).findFirst()
					.orElse(null);
			if (selectedData != null && selectedData.getDate() != null
					&& selectedData.getDate().isBefore(now.plusDays(1))) {
				StringBuilder tooltipText = new StringBuilder();
				tooltipText.append("Date : ").append(DATE_FORMATTER.format(selectedData.getDate())).append("\n")
						.append("Contributions : ")
						.append(null == selectedData ? "-" :  ""+(int)selectedData.getValue());
				tooltip.setText(tooltipText.toString());
				tooltip.setX(e.getMouseScreenX());
				tooltip.setY(e.getMouseScreenY());
				tooltip.show(matrix.getScene().getWindow());
				if (service.isRunning()) {
					service.cancel();
					service.reset();
				}
				service.start();
			}
		});
		
		legend = new HBox(5, new Text("Less"), new Rectangle(10, 10, colorCoding.get(0.0)),
				new Rectangle(10, 10, colorCoding.get(0.9)), new Rectangle(10, 10, colorCoding.get(1.9)),
				new Rectangle(10, 10, colorCoding.get(2.9)), new Rectangle(10, 10, colorCoding.get(3.9)),
				new Text("More"));
		legend.setAlignment(Pos.CENTER);

		// Set matrix to random data
		updateContributionData();

		AnchorPane pane2 = new AnchorPane(months, weekdays, matrix, legend);
		pane2.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

		pane2.setTopAnchor(months, 10d);
		pane2.setRightAnchor(months, 10d);
		pane2.setLeftAnchor(months, 10d);

		pane2.setTopAnchor(weekdays, 30d);
		pane2.setBottomAnchor(weekdays, 30d);
		pane2.setLeftAnchor(weekdays, 10d);

		pane2.setTopAnchor(matrix, 30d);
		pane2.setRightAnchor(matrix, 10d);
		pane2.setBottomAnchor(matrix, 30d);
		pane2.setLeftAnchor(matrix, 30d);

		pane2.setRightAnchor(legend, 10d);
		pane2.setBottomAnchor(legend, 10d);

		dotMatrixPane.getChildren().add(pane2);
//		dotMatrixPane.setStyle("-fx-background-radius : 4px");

	}

	private void updateContributionData() {
		HashMap<LocalDate, Integer> contrMap = new HashMap<LocalDate, Integer>();

		for (Version version : project.versions) {
			if (version.getOwner().equals(userBox.getValue())) {
				LocalDate date = LocalDate.of((version.getDateCreated().getYear() + 1900),
						(version.getDateCreated().getMonth() + 1), version.getDateCreated().getDate());
				if (contrMap.containsKey(date))
					contrMap.put(date, contrMap.get(date) + 1);
				else
					contrMap.put(date, 1);
			}

		}
		dataList = new ArrayList<>(weeks * 7);
		for (int x = 0; x < weeks; x++) {
			for (int y = 0; y < 7; y++) {
				LocalDate date = startDate.plusDays(x * 7 + y);
				double value = 0;
				if (contrMap.containsKey(date))
					value = contrMap.get(date);
				if (date.isAfter(now)) {
					matrix.setPixel(x, y, Color.WHITE);
				} else {
					dataList.add(new DataC(x, y, value, date));
					matrix.setPixel(x, y, getColor(value));
				}

			}
		}

	}

	private Color getColor(final double VALUE) {
		return colorCoding
				.get(colorCoding.keySet().stream().filter(threshold -> VALUE > threshold).findFirst().orElse(0.0));
	}

//	private void recalcSize(final Scene SCENE) {
//		double width = SCENE.getWidth();
//		double height = SCENE.getHeight();
//		double offsetX = (width - matrix.getWidth()) * 0.5;
//		double offsetY = (height - matrix.getHeight()) * 0.5;
//
//		AnchorPane.setRightAnchor(months, offsetX);
//		AnchorPane.setLeftAnchor(months, offsetX);
//		AnchorPane.setTopAnchor(months, offsetY - 20);
//
//		AnchorPane.setLeftAnchor(weekdays, offsetX - 10);
//		AnchorPane.setTopAnchor(weekdays, offsetY);
//		AnchorPane.setBottomAnchor(weekdays, offsetY);
//
//		AnchorPane.setBottomAnchor(legend, offsetY - 20);
//
//	}

	// Scene scene = new Scene(pane);
	// scene.widthProperty().addListener(o -> recalcSize(scene));
	// scene.heightProperty().addListener(o -> recalcSize(scene));
	//
	// stage.setTitle("JavaFX DotMatrix Demo");
	// stage.setScene(scene);
	// stage.show();

	// ******************** Inner Classes *************************************
	class ProcessService extends Service<Void> {
		@Override
		protected Task<Void> createTask() {
			return new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					Thread.sleep(TOOLTIP_TIMEOUT);
					return null;
				}
			};
		}
	}

	class DataC {
		private int x;
		private int y;
		private double value;
		private LocalDate date;

		// ******************** Constructors **********************************
		public DataC(final int X, final int Y) {
			this(X, Y, 0, LocalDate.now());
		}

		public DataC(final int X, final int Y, final double VALUE) {
			this(X, Y, VALUE, LocalDate.now());
		}

		public DataC(final int X, final int Y, final double VALUE, final LocalDate DATE) {
			x = X;
			y = Y;
			value = VALUE;
			date = DATE;
		}

		// ******************** Methods ***************************************
		public int getX() {
			return x;
		}

		public void setX(final int X) {
			x = X;
		}

		public int getY() {
			return y;
		}

		public void setY(final int Y) {
			y = Y;
		}

		public int[] getXY() {
			return new int[] { x, y };
		}

		public double getValue() {
			return value;
		}

		public void setValue(final double VALUE) {
			value = VALUE;
		}

		public LocalDate getDate() {
			return date;
		}

		public void setDate(final LocalDate DATE) {
			date = DATE;
		}
	}
}
