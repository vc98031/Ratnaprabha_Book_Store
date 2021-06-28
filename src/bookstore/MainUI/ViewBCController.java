/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstore.MainUI;

import bookstore.database.DatabaseHandler;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author SATISH
 */
public class ViewBCController implements Initializable 
{

    
    
    DatabaseHandler handler;
    Connection conn;
    PreparedStatement stmt;        
    ResultSet rs;
    
    
    String Book_ID;    
    String Book_Name;    
    String Cust_Name;
    String Cust_Id;
    
    Notifications notify;
    
    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXTabPane tabPane;
    @FXML
    private Tab allBookTab;
    @FXML
    private Tab allCustTab;
    @FXML
    private AnchorPane allBookPane;
    @FXML
    private AnchorPane allCustPane;
    @FXML
    private Tab bDetailsTab;
    @FXML
    private AnchorPane bDetailsPane;
    @FXML
    private Tab custDetailsTab;
    @FXML
    private AnchorPane cDetailsPane;  
    @FXML
    private TextField bookid;
    @FXML
    private JFXTextField bookname;
    @FXML
    private StackPane stackPane;
    @FXML
    private TextField custID;
    private JFXTextField CustName;
    @FXML
    private StackPane custStackPane;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        handler=DatabaseHandler.getInstance();
        tabSetter();
        notify=Notifications.create();           
    }        

    private void tabSetter()
    {
          if(tabPane.getSelectionModel().getSelectedIndex()==1)
            try 
            {
                    allCustTab.setContent((Node)FXMLLoader.load(getClass().getResource("/bookstore/ListCustomer/CustomerList.fxml")));
            }
            catch (IOException ex) 
            {
                System.out.println("Exception in View BCController initialize() 2\n"+ex);
            }
            if(tabPane.getSelectionModel().getSelectedIndex()==0)
            try 
            {
                    allBookTab.setContent((Node)FXMLLoader.load(getClass().getResource("/bookstore/ListBooks/book_list.fxml")));
            }
            catch (IOException ex) 
            {
                System.out.println("Exception in View BCController initialize() 1\n"+ex);
            }
    }
    
    @FXML
    private void moveLeft(ActionEvent event) {
         int idx=tabPane.getSelectionModel().getSelectedIndex();        
            if((idx-1) >= 0  && --idx >= 0); // in second condition there is no any special 
            //meaning of ">0" we just want to --idx but in java if condition requires
            //only condition that generate boolean thats why we take ">0" to generate 
            //boolean of expression --idx 
            tabPane.getSelectionModel().select(idx);
            //tabpane.getSelectionModel().select(0);       */
        //System.out.println(tabpane.getSelectionModel().getSelectedIndex());
            tabSetter();
    }

    @FXML
    private void moveRight(ActionEvent event) 
    {
         int idx=tabPane.getSelectionModel().getSelectedIndex();             
            if((idx+1) <= 5  && ++idx >= 0); // in second condition there is no any special 
            //meaning of ">0" we just want to ++idx but in java if condition requires
            //only condition that generate boolean thats why we take ">0" to generate 
            //boolean of expression ++idx            
            tabPane.getSelectionModel().select(idx);
        // tabpane.getSelectionModel().select(2);  
            tabSetter();
    }
   
    @FXML
    private void searchBk(ActionEvent event) 
    {        
        
        Book_Name=bookname.getText();
        Book_ID=bookid.getText();                                        
        
        boolean valid=false;
        
        if(Book_ID.isEmpty() && !Book_Name.isEmpty())
            try 
            {
                conn=handler.createConnection();
                stmt=conn.prepareStatement("select id from book where title=?");
                stmt.setString(1,Book_Name);
                rs=stmt.executeQuery();
                bookid.setText(rs.getString(1));
                valid=true;
            }
            catch (SQLException ex) 
            {
                notify.title("Please Input Valid Book Name");                    
                notify.hideAfter(Duration.seconds(2));                
                notify.position(Pos.CENTER);                
                notify.darkStyle();
                notify.showError(); 
                return;
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
                        System.out.println("Exception in DatabaseHadler setBookTable()\n"+ex);
                    }
            }
        else if(!Book_ID.isEmpty() && Book_Name.isEmpty())
            try 
            {
                conn=handler.createConnection();
                stmt=conn.prepareStatement("select title from book where id=?");
                stmt.setString(1,Book_ID);
                rs=stmt.executeQuery();
                bookname.setText(rs.getString(1));
                valid=true;
            }
            catch (SQLException ex) 
            {
                notify.title("Please Input Valid Book ID");                    
                notify.hideAfter(Duration.seconds(2));                
                notify.position(Pos.CENTER);                
                notify.darkStyle();
                notify.showError(); 
                return;
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
                        System.out.println("Exception in DatabaseHadler setBookTable()\n"+ex);
                    }
            }
        else
        {
                notify.title("Please Input Only One Field");                    
                notify.hideAfter(Duration.seconds(2));                
                notify.position(Pos.CENTER);                
                notify.darkStyle();
                notify.showError(); 
                return;
        }
                
        if(valid)
        {
           BookDetailsController.bkid=Book_ID;
            try {                       
                 AnchorPane pane=FXMLLoader.load(getClass().getResource("/bookstore/MainUI/BookDetails.fxml"));
                 stackPane.getChildren().add(pane);
            } catch (IOException ex) {
                System.out.println("Exception in VeiewBCController \n"+ex);
            }
        }
    }              

    @FXML
    private void showPieChart(ActionEvent event) 
    {
          try 
        {        
            Parent root = FXMLLoader.load(getClass().getResource("/bookstore/MainUI/BookDetailsPieChart.fxml"));        
            Scene scene = new Scene(root,600,600);   
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.getIcons().add(new Image("/bookstore/icons/pie-chart.png"));        
            stage.show();
            
            
            /* Parent root = FXMLLoader.load(getClass().getResource("/bookstore/addbooks/AddBook.fxml"));        
            Scene scene = new Scene(root,800,600);        
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.getIcons().add(new Image("/bookstore/icons/book.png"));        
            stage.show();
*/
        }
        catch (IOException ex) 
        {
            System.out.println("Exception in MainUIController showPieChart()\n"+ex);
        }   
        return;       
    }

    @FXML
    private void searchCust(ActionEvent event) 
    {      
        Cust_Id=custID.getText();                                                        
        
        if(Cust_Id.isEmpty())
        {
                notify.title("Please Input Valid Customer ID");                    
                notify.hideAfter(Duration.seconds(2));                
                notify.position(Pos.CENTER);                
                notify.darkStyle();
                notify.showError(); 
                return;                          
        }
        else 
        {
           CustomerDetailsController.custid=Cust_Id;
            try {                       
                 AnchorPane pane=FXMLLoader.load(getClass().getResource("/bookstore/MainUI/CustomerDetails.fxml"));
                 custStackPane.getChildren().add(pane);
            } catch (IOException ex) {
                System.out.println("Exception in VeiewBCController \n"+ex);
            }
        }
    }

    @FXML
    private void showPieChartCust(ActionEvent event) 
    {
        
    }
   
}
