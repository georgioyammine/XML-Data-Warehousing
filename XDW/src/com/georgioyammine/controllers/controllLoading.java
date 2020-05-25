/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.georgioyammine.controllers;

import java.io.IOException;

import com.georgioyammine.XDP_Launcher;
import com.georgioyammine.XDW_Launcher;
import com.georgioyammine.classes.DataWarehousing;

import animations.FadeInLeftTransition;
import animations.FadeInRightTransition;
import animations.FadeInTransition;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


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
    public static DataWarehousing project;
    public static String projectDir;
    public static Scene scene;
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

    @SuppressWarnings("unchecked")
	private void longStart() {
        dataWarehousingController.project = project;
		dataWarehousingController.projectPath = projectDir;
        @SuppressWarnings("rawtypes")
		Service service = new Service() {
        	@Override
    		protected Task createTask() {
    			return new Task<Void>() {
    				@Override
    				public Void call() throws Exception {
    					System.out.println("started");
    					long t1 = System.currentTimeMillis();
    					try {
    					root = FXMLLoader.load(XDW_Launcher.class.getResource("fxml/"+"dataWarehousing.fxml"));
    					}catch(Exception e) {
//    						e.printStackTrace();
    						Platform.runLater(new Runnable() {

								@Override
								public void run() {
									try {
										root = FXMLLoader.load(XDW_Launcher.class.getResource("fxml/"+"dataWarehousing.fxml"));
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}


								}
							});
    					}
    					if(System.currentTimeMillis()-t1<2000)
    						Thread.sleep(1850 - (System.currentTimeMillis()-t1));
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
    		Scene scene = new Scene(root,850,450);
    		if(stage==null) {
    			stage = new Stage();
//    			stage.getIcons().add(new Image("icon-main@3x.png"));
    			stage.getIcons().add(new Image(XDP_Launcher.class.getResourceAsStream("images/icon-main@3x.png")));
    			stage.setTitle("XDW - XML Data Warehousing ");
    			// stage.getIcons().add(new Image("iconN (1).png"));
//    			stage.getIcons().add(new Image("icon-main@3x.png"));
    		}
			stage.setScene(scene);
			Stage window = (Stage) (vboxBottom.getScene().getWindow());
//			stage.setOnHidden(event2 -> Platform.exit());
			stage.setResizable(true);
			stage.show();
			stage.setWidth(850);
			stage.setMinHeight(350);
			stage.setMinWidth(530);
			window.hide();

        });

    }
}
