/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstore.login;

import bookstore.MainUI.MainUIController;
import bookstore.QRCodes.RemoveQRCode;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Admin
 */
public class LoginLoader extends Application {
    
    public static boolean isSplashLoaded=false;
    public static boolean isMainUILoaded=false;
    @Override
    public void start(Stage stage) throws Exception 
    {
        Parent root=FXMLLoader.load(getClass().getResource("Login.fxml"));                
        Scene scene=new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED); 
        // stage.getIcons().add(new Image("bookstore/icons/admin.png"));
        stage.show();        
        
    }       
    public static void main(String[]args)
    {        
        launch(args);
    }
}
