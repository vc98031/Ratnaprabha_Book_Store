package bookstore.MainUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;


public class VBoxMenuController implements Initializable 
{
    Notifications notify;
    
    @FXML
    private Button fileBtn;
    @FXML
    private Button addBC;
    @FXML
    private Button viewBtn;
    @FXML
    private Button helpBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
         notify=Notifications.create();            
    }    

    @FXML
    public void fileBtnEvent(ActionEvent event) 
    {
        
         notify.title("File Option for Change the Username & Password\n\nPlease Log In For Change The Password !");                    
                    notify.hideAfter(Duration.seconds(7));                
                    notify.position(Pos.CENTER);                
                    notify.darkStyle();
                    notify.showInformation();
     try 
        {
            Parent root=FXMLLoader.load(getClass().getResource("/bookstore/MainUI/Authentication.fxml"));
            Scene scene=new Scene(root);
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.getIcons().add(new Image("bookstore/icons/clipboard.png"));
            stage.show();
        }
        catch (IOException ex) 
        {
            System.out.println("Exception in MainUIController searchCustomer()\n"+ex);
        }
        return;
    }

    @FXML
    public void addBkCustEvent(ActionEvent event) 
    {
    try 
        {
            Parent root=FXMLLoader.load(getClass().getResource("/bookstore/MainUI/AddBCWindow.fxml"));
            Scene scene=new Scene(root);
            Stage stage=new Stage();
            stage.setScene(scene);            
            stage.setResizable(false);
            stage.getIcons().add(new Image("bookstore/icons/couple-users.png"));
            stage.show();
        }
        catch (IOException ex) 
        {
            System.out.println("Exception in MainUIController searchCustomer()\n"+ex);
        }
        return;
            
    }

    @FXML
    public void viewEvent(ActionEvent event) 
    {
        try 
        {
            Parent root=FXMLLoader.load(getClass().getResource("/bookstore/MainUI/viewBC.fxml"));
            Scene scene=new Scene(root);
            Stage stage=new Stage();
            stage.setScene(scene);            
            stage.setResizable(false);
            stage.getIcons().add(new Image("bookstore/icons/data.png"));
            stage.show();
        }
        catch (IOException ex) 
        {
            System.out.println("Exception in MainUIController searchCustomer()\n"+ex);
        }
        return;
            
    }

    @FXML
    public void helpEvent(ActionEvent event) 
    {
        try 
        {
            Parent root=FXMLLoader.load(getClass().getResource("/bookstore/MainUI/HelpWindow.fxml"));
            Scene scene=new Scene(root);
            Stage stage=new Stage();
            stage.setScene(scene);            
            stage.setTitle("Help");
            stage.setResizable(false);
            stage.getIcons().add(new Image("bookstore/icons/social-care.png"));
            stage.show();
        }
        catch (IOException ex) 
        {
            System.out.println("Exception in MainUIController searchCustomer()\n"+ex);
        }
        return;
    }
    
}
