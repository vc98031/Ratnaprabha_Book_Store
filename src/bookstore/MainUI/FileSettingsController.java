/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstore.MainUI;

import bookstore.database.DatabaseHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author Jayprasad
 */
public class FileSettingsController implements Initializable {
    @FXML
    private JFXButton changeBtn;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;

    
    DatabaseHandler handler;
    Connection conn;
    Statement stmt;
    ResultSet rs;
    
       
    Notifications notify;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        handler=DatabaseHandler.getInstance();        
        notify=Notifications.create();
    }    

   

    @FXML
    private void changeUP(ActionEvent event) 
    {
        try {
            Stage stage=(Stage)changeBtn.getScene().getWindow();
            stage.close();             
            conn=handler.createConnection();
            stmt=conn.createStatement();
            stmt.executeUpdate("update login set username='"+username.getText()+"' and password='"+password.getText()+"' where No=1");
             notify.title("UserName & Password Successfully Changed !");                    
             notify.hideAfter(Duration.seconds(7));                
             notify.position(Pos.CENTER);                
             notify.darkStyle();
             notify.showInformation();
                           
        } catch (SQLException ex) {
            Logger.getLogger(FileSettingsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
