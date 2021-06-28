/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstore.MainUI;

import bookstore.database.DatabaseHandler;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author Jayprasad
 */
public class SoldBookTableController implements Initializable 
{
    ObservableList <SoldBook> list =FXCollections.observableArrayList();
    
    @FXML
    private TableView<SoldBook> soldBookTable;
    @FXML
    private TableColumn<SoldBook,Integer> no_Column;
    @FXML
    private TableColumn<SoldBook,Integer> Custid_Column;
    @FXML
    private TableColumn<SoldBook,String> bookid_Column;
    @FXML
    private TableColumn<SoldBook,String> date_Column;
    @FXML
    private TableColumn<SoldBook,Integer> quantityCol;
    @FXML
    private TableColumn<SoldBook,Integer> price_Column;

    
    DatabaseHandler handler;
    Connection conn;
    Statement stmt;
    ResultSet rs;
    
    Notifications notify;
   
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        notify=Notifications.create();
        initCol();
        loadData();
    }    

    
     private void initCol() 
    {
        no_Column.setCellValueFactory(new PropertyValueFactory<>("no"));
        Custid_Column.setCellValueFactory(new PropertyValueFactory<>("custid"));
        bookid_Column.setCellValueFactory(new PropertyValueFactory<>("bookid"));
        date_Column.setCellValueFactory(new PropertyValueFactory<>("date"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        price_Column.setCellValueFactory(new PropertyValueFactory<>("price"));        
    }

    private void loadData() 
    {
        try 
        {            
            handler=DatabaseHandler.getInstance();
            conn=handler.createConnection();        
            stmt=conn.createStatement();
            rs=stmt.executeQuery("Select No,BID,Customer,Date,Quantity,Price from SoldBook");
            
            while(rs.next())
            {   
                Integer No=rs.getInt(1);
                String Bkid=rs.getString(2);                
                Integer Cid=rs.getInt(3);
                String Date=rs.getString(4);
                Integer Quantity=rs.getInt(5);                
                Integer Price=rs.getInt(5);                
                                
                list.add(new SoldBook(No,Bkid,Cid,Date,Quantity,Price));
            }            
        } 
        catch (SQLException ex) 
        {
            System.out.println("Exception in SoldBookTableController loadData()\n"+ex);
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
                System.out.println("Connection closed loadData()\n"+ex);
            }
        }  
        soldBookTable.setItems(list);
    }

    @FXML
    private void handleBookOptions(ActionEvent event) 
    {
        SoldBook option=soldBookTable.getSelectionModel().getSelectedItem();
        if(option==null)
        {
            notify.title("\t No Book Selected ! \t Please Select Book For Deletion.");                    
            notify.hideAfter(Duration.seconds(2));                
            notify.position(Pos.CENTER);                
            notify.darkStyle();
            notify.showError(); 
            return;
        }
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleting Book");
        alert.setContentText("Are You Want to Deleting The Book "+option.getBookid()+" ?");
        Optional<ButtonType> answer=alert.showAndWait();
        if(answer.get()==ButtonType.OK)
        {
            if(DatabaseHandler.getInstance().deleteSoldBook(option))
                list.remove(option);
        }
        else
        {
            notify.title("Deletion Cancelled !");                    
            notify.hideAfter(Duration.seconds(2));                
            notify.position(Pos.CENTER);                
            notify.darkStyle();
            notify.showError();                         
        }
    }

    public static class SoldBook
    {
        private final SimpleIntegerProperty no;        
        private final SimpleIntegerProperty custid;        
        private final SimpleStringProperty bookid;        
        private final SimpleStringProperty date;        
        private final SimpleIntegerProperty quantity;        
        private final SimpleIntegerProperty price;                
        
        //   list.add(new Book(No,Bkid,Btitle,Cid,Date,Price));
        SoldBook(Integer No,String Bkid,Integer Cid,String Date,Integer Quantity,Integer Price)
        {            
            this.no=new SimpleIntegerProperty(No);            
            this.bookid=new SimpleStringProperty(Bkid);
            this.custid=new SimpleIntegerProperty(Cid);
            this.date=new SimpleStringProperty(Date);
            this.quantity=new SimpleIntegerProperty(Quantity);
            this.price=new SimpleIntegerProperty(Price);
        }
        

        public Integer getNo() {
            return no.get();
        }

        public Integer getCustid() {
            return custid.get();
        }

        public String getBookid() {
            return bookid.get();
        }

        public String getDate() {
            return date.get();
        }

        public Integer getQuantity() {
            return quantity.get();
        }
        
        public Integer getPrice() {
            return price.get();
        }        
    }      
}
