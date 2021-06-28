package bookstore.ListCustomer;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class CustomerListLoader extends Application
{

    @Override
    public void start(Stage stage) throws Exception 
    {
        Parent root=FXMLLoader.load(getClass().getResource("CustomerList.fxml"));        
        Scene scene=new Scene(root);
        stage.setScene(scene);
        stage.getIcons().add(new Image("bookstore/icons/clipboard.png"));
        stage.show();
    }    
    public static void main(String[]args)
    {
        launch(args);
    }
}