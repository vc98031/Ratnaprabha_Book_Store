//import bookstore.QRCodes.RemoveQRCode;
package bookstore.MainUI;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class SearchBook extends Application
{
    @Override
    public void start(Stage stage) throws Exception 
    {
        Parent root=FXMLLoader.load(getClass().getResource("BookPiechart.fxml"));                
        Scene scene=new Scene(root,800,600);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("DashBoard");
        stage.getIcons().add(new Image("bookstore/icons/admin.png"));
        stage.show();        
    }    
    public static void main(String[]args)
    {
        //new RemoveQRCode();
        launch(args);
    }

}
