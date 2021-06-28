/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstore.MainUI;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author SATISH
 */
public class AddBCWindowController implements Initializable {

    @FXML
    private JFXButton addBook;
    @FXML
    private JFXButton addCust;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        // TODO
    }    

    @FXML
    private void addBookEvent(ActionEvent event) 
    {
         try              
        {    
            Stage stage1=(Stage)addBook.getScene().getWindow();
            stage1.close();            
            Parent root = FXMLLoader.load(getClass().getResource("/bookstore/addbooks/AddBook.fxml"));        
            Scene scene = new Scene(root,800,600);        
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.getIcons().add(new Image("/bookstore/icons/book.png"));        
            stage.show();
        }
        catch (IOException ex) 
        {
            System.out.println("Exception in MainUIController addBook()\n"+ex);
        }   
        return;
    }

    @FXML
    private void addCustomer(ActionEvent event) 
    {
         try 
         {
            Stage stage=(Stage)addCust.getScene().getWindow();
            stage.close();
            Parent root=FXMLLoader.load(getClass().getResource("/bookstore/addCustomer/CustAdd.fxml"));
            Scene scene=new Scene(root);
            Stage stage2=new Stage();
            stage2.setScene(scene);
            stage2.setResizable(false);
            stage2.getIcons().add(new Image("bookstore/icons/add.png"));
            stage2.show();
        } catch (IOException ex) {
            System.out.println("Exception in MainUIController addCustomer()\n"+ex);
        }
        return;
    }
    
}
