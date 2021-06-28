/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstore.login;

import bookstore.MainUI.MainUIController;
import bookstore.QRCodes.RemoveQRCode;
import bookstore.database.DatabaseHandler;
import com.itextpdf.text.Anchor;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author Admin
 */
public class LoginController implements Initializable {

    @FXML
    private JFXTextField userName;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXButton help;
    @FXML
    private AnchorPane root;
    @FXML
    private JFXButton signInBtn;
    
    
    DatabaseHandler handler;            
    Connection conn;
    PreparedStatement stmt;
    ResultSet rs;
    
    
    
    Notifications notify;    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        // TODO
        new RemoveQRCode();       
        if(!LoginLoader.isSplashLoaded)
            splashScreen();
        handler=DatabaseHandler.getInstance();    
        
        if(new MainUIController().emptyGenerateBill())
            System.out.println("bill history cleared!");
        else 
            System.out.println("bill history not cleared!");

        notify=Notifications.create();
    }    

    @FXML
    private void signIn(ActionEvent event) 
    {
        try 
        {
            conn=handler.createConnection();
            stmt=conn.prepareStatement("select username,password from Login where no=1");
            rs=stmt.executeQuery();
            if(userName.getText().equals(rs.getString(1)) && password.getText().equals(rs.getString(2)))
            {
                LoginLoader.isMainUILoaded=true;
                
                AnchorPane pane=FXMLLoader.load(getClass().getResource("/bookstore/MainUI/MainUI.fxml"));                       
                Scene scene=signInBtn.getScene();
                pane.translateYProperty().set(scene.getHeight());
                root.getChildren().add(pane);
                //stage.initStyle(StageStyle.DECORATED); 
                
                Timeline  timeline=new Timeline();
                KeyValue key=new KeyValue(pane.translateYProperty(),0,Interpolator.EASE_IN);
                KeyFrame kf=new KeyFrame(Duration.seconds(1),key);
                timeline.getKeyFrames().add(kf);
                timeline.play();
            }
            else if(userName.getText().isEmpty() && password.getText().isEmpty())
            {
                    notify.title("Please Input All The Fields\nClick on infomation icon if forgot password");                    
                    notify.hideAfter(Duration.seconds(2));                
                    notify.position(Pos.CENTER);  
                    notify.darkStyle();
                    notify.showError();
                    return;      
            }
            else
            {
                    notify.title("Incorrect Password");                    
                    notify.hideAfter(Duration.seconds(2));                
                    notify.position(Pos.CENTER);  
                    notify.darkStyle();
                    notify.showError();
                    return;      
            }
        }
        catch (SQLException ex) 
        {
            System.out.println("Exception in LoginController SignIn()\n"+ex);            
        }
        finally
            {                                
                if(conn!=null)
                try
                {
                    conn.close();                    
                }
                catch (SQLException ex) 
                {
                    System.out.println("connection not closed in  LoginController class -  SignIn() :\n"+ex);
                }
                return;
            }
    }

    @FXML
    private void closeWindow(ActionEvent event) 
    {
        System.exit(0);
    }
    
    private void splashScreen()
    {
        try 
        {
            //StackPane pane1=FXMLLoader.load(getClass().getResource("/bookstore/splashScreen/Splash1.fxml"));            
            //root.getChildren().setAll(pane1);
            
            LoginLoader.isSplashLoaded=true;
                    
            AnchorPane pane2=FXMLLoader.load(getClass().getResource("/bookstore/splashScreen/Splash2.fxml"));                       
            root.getChildren().setAll(pane2);
            
            FadeTransition fadein=new FadeTransition(Duration.seconds(5),pane2);
            fadein.setFromValue(0);
            fadein.setToValue(1);
            fadein.setCycleCount(1);
                        
            
            FadeTransition fadeout=new FadeTransition(Duration.seconds(3),pane2);
            fadeout.setFromValue(1);
            fadeout.setToValue(0);
            fadeout.setCycleCount(1);
                        
            fadein.play();
            
            fadein.setOnFinished((e)->{ fadeout.play();});            
            fadeout.setOnFinished((e)->{
                try 
                {
                    AnchorPane pane=FXMLLoader.load(getClass().getResource("/bookstore/login/Login.fxml"));
                    root.getChildren().setAll(pane);
                }
                catch (IOException ex) 
                {
                    System.out.println("Exception in LoginController "+ex);
                }
            
            });            
        }
        catch (IOException ex) 
        {
            System.out.println("Exception in LoginController "+ex);
        }
    }

    @FXML
    private void helpWindow(ActionEvent event) 
    {
        try 
        {
            Parent root=FXMLLoader.load(getClass().getResource("/bookstore/MainUI/HelpWindow.fxml"));
            Scene scene=new Scene(root);
            Stage stage=new Stage();
            stage.setScene(scene);            
            stage.setTitle("Information");
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
