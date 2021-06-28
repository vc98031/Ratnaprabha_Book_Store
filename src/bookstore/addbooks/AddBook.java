package bookstore.addbooks;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class AddBook extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {                
        Parent root = FXMLLoader.load(getClass().getResource("/bookstore/addbooks/AddBook.fxml"));        
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
