/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import animations.FadeInLeftTransition;
import animations.FadeInRightTransition;
import animations.FadeInTransition;

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.layout.AnchorPane;


public class controllLoading {
    @FXML
    private Text lblWelcome;
    @FXML
    private Text lblRudy;
    @FXML
    private VBox vboxBottom;
    @FXML
    private Label lblClose;
    static Stage stage;
    Parent root;
    @FXML
    private ImageView imgLoading;
    static DataWarehousing project;
    static String projectDir; 
    static Scene scene;
	@FXML AnchorPane mainAnchor;
	@FXML Text projectName;
	@FXML Label Minimise;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
	private double xOffset, yOffset;

    public void initialize() {
    	projectName.setText(project.getName()+ " - "+ project.getOwner());
        longStart();
        mainAnchor.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        mainAnchor.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	((Stage) (vboxBottom.getScene().getWindow())).setX(event.getScreenX() - xOffset);
            	((Stage) (vboxBottom.getScene().getWindow())).setY(event.getScreenY() - yOffset);
            }
        });
        lblClose.setOnMouseClicked((MouseEvent event) -> {
        	
            Platform.exit();
            System.exit(0);
        });
        Minimise.setOnMouseClicked((MouseEvent event) -> {
        	((Stage) (vboxBottom.getScene().getWindow())).setIconified(true);
        });
    }   
    
    private void longStart() {
        dataWarehousingController.project = project;
		dataWarehousingController.projectPath = projectDir;
        Service service = new Service() {
        	@Override
    		protected Task createTask() {
    			return new Task<Void>() {
    				@Override
    				public Void call() throws Exception {
    					System.out.println("started");
    					root = FXMLLoader.load(getClass().getResource("dataWarehousing.fxml"));    			
    					return null;
    					}
    				};
    			}
        };

        service.start();
        service.setOnRunning((event) -> {
        	new FadeInLeftTransition(lblWelcome).play();
	        new FadeInRightTransition(lblRudy).play();
	        new FadeInTransition(vboxBottom).play();
        });
        service.setOnSucceeded((event) -> {
    		Scene scene = new Scene(root, 800, 450);
			stage.setScene(scene);
			Stage window = (Stage) (vboxBottom.getScene().getWindow());
			stage.setOnHidden(event2 -> Platform.exit());
			stage.show();
			stage.setResizable(true);
			stage.setMinHeight(350);
			stage.setMinWidth(530);
			window.hide();
        });
            
    } 
}
