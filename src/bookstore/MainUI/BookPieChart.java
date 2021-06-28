/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstore.MainUI;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author a
 */
public class BookPieChart extends Application{
    public void start(Stage stage) throws Exception {                
        Parent root = FXMLLoader.load(getClass().getResource("/bookstore/MainUI/BookPiechart.fxml"));        
        Scene scene = new Scene(root,800,600);        
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(new Image("/bookstore/icons/book.png"));        
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
