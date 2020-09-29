package com.autoTest.javaFxDataEncry;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

import static com.autoTest.javaFxDataEncry.base.ConsField.vieFieldBatchOverview;


@SpringBootApplication
public class JavaFxDataEncryApplication extends Application {

	@Override
	public void start(Stage stage) throws IOException {
		stage.setTitle("测试工具");
		Parent root = FXMLLoader.load(getClass().getResource(vieFieldBatchOverview));
		stage.setScene(new Scene(root, 750, 500));
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
