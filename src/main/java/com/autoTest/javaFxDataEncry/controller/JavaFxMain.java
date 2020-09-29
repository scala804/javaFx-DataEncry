package com.autoTest.javaFxDataEncry.controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.autoTest.javaFxDataEncry.base.ConsField.*;

public  class JavaFxMain extends Application {
    private static final Logger logger= LoggerFactory.getLogger(JavaFxMain.class);
    private Stage primaryStage;
    private BorderPane rootLayout;


    /**
     * Constructor
     */
    public JavaFxMain() {

    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("数据生成工具");
        initRootLayout();
        showFieldBatchOverview();


    }

    public void showFieldBatchOverview(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(vieFieldBatchOverview));
            AnchorPane fieldBatchOverview = (AnchorPane) loader.load();

            JavaFxController javaFxController = loader.getController();
            javaFxController.setDialogStage(primaryStage);
            rootLayout.setCenter(fieldBatchOverview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(viewRootLayout));
            rootLayout = (BorderPane) loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Opens a dialog to edit details for the specified person. If the user
     * clicks OK, the changes are saved into the provided person object and true
     * is returned.
     *
     * @param field the person object to be edited
     * @return true if the user clicked OK, false otherwise.
     */
    /**
     * 编辑字段功能，根据字段类型选择弹出框类型
     * @param field
     * @return
     */



    /**
     * Returns the main stage.
     * @return
     */
   public Stage getPrimaryStage() {
        return primaryStage;
    }

  /*  public static void main(String[] args) {
        launch(args);
    }*/

    @FXML
    private void initialize() {
      /*  ChoiceBoxEncryType.getItems().addAll("SHA256", "MD5", "SM3");
        ChoiceBoxEncryType.getSelectionModel().selectFirst();*/
    }




}