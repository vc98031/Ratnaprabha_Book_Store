/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstore.MainUI;

import bookstore.database.DatabaseHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author Jayprasad
 */
public class AuthenticationController implements Initializable {
    

    DatabaseHandler handler;
    Connection conn;
    PreparedStatement stmt;
    Statement stmts;
    ResultSet rs;
    
    Notifications notify;
    
    @FXML
    private JFXButton login;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        notify=Notifications.create();
        handler=DatabaseHandler.getInstance();
    }    

    @FXML
    private void logIn(ActionEvent event)             
    {
        try 
        {
            conn=handler.createConnection();
            stmts=conn.createStatement();            
            rs=stmts.executeQuery("select username,password from Login where No=2");
            if(username.getText().equals(rs.getString(1)) && password.getText().equals(rs.getString(2))) 
            {                
                Stage stage=(Stage)login.getScene().getWindow();
                stage.close();
               try {
                    Parent root=FXMLLoader.load(getClass().getResource("/bookstore/MainUI/FileSettings.fxml"));
                    Scene scene=new Scene(root);
                    Stage stage2=new Stage();
                    stage2.setScene(scene);
                    stage2.setResizable(false);
                    stage2.getIcons().add(new Image("bookstore/icons/add.png"));
                    stage2.show();
                     } catch (IOException ex) {
                    System.out.println("Exception in MainUIController searchBooks()\n"+ex);
                }
            }
            else
            {
                
                notify.title("Wrong Credentials !");                    
                notify.hideAfter(Duration.seconds(2));                
                notify.position(Pos.CENTER);                
                notify.darkStyle();
                notify.showError();
            }
         
        }
        catch (SQLException ex) 
        {
            System.out.println("Exception in AuthenticationController logIn()\n"+ex);
        }
        finally
        {
            if(conn!=null)
                try {
                    conn.close();
            } catch (SQLException ex) {
                System.out.println("Exception Connection not Closed in AuthenticationController logIn()\n"+ex);
            }
        }
    }
    
}
