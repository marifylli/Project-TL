package com.unipath.login;




import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class Main extends Application {


   @Override
    public void start(Stage stage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/login-view.fxml")
            );
            Parent root = loader.load();

            Scene scene = new Scene(root, 520, 620);
            scene.getStylesheets().add(
                    getClass().getResource("/css/styles.css").toExternalForm()
            );

            stage.setTitle("Study Plan App - Εύοδος");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            System.err.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

}


