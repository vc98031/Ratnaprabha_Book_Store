/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstore.MainUI;

import bookstore.database.DatabaseHandler;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author a
 */
public class CustomerDetailsController implements Initializable {
    
    static String custid;
    
    
    DatabaseHandler handler;
    Connection conn;
    PreparedStatement stmt;
    ResultSet rs;
     
    Notifications notify; 


    ObservableList <Cust> list =FXCollections.observableArrayList();    
    @FXML
    private TableView<Cust> tableview;
    @FXML
    private TableColumn<Cust,String> bookid;
    @FXML
    private TableColumn<Cust,String> bookname;
    @FXML
    private TableColumn<Cust,String> date;
    @FXML
    private TableColumn<Cust,Integer> quantity;
    @FXML
    private TableColumn<Cust,Integer> price;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        // TODO
        initCol();
        loadData();
    }   
     private void initCol() 
    {
        bookid.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookname.setCellValueFactory(new PropertyValueFactory<>("title"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));        
    }

    private void loadData() 
    {
        try 
        {    
            list.clear();
            handler=DatabaseHandler.getInstance();
            conn=handler.createConnection();        
                        
            stmt=conn.prepareStatement("Select bid,btitle,date,quantity,price from soldbook where Customer=?");
            stmt.setInt(1,Integer.parseInt(custid));
            rs=stmt.executeQuery();
                        
            while(rs.next())
            {   
                String ID=rs.getString(1);
                String Title=rs.getString(2);
                String Date=rs.getString(3);
                Integer quant=rs.getInt(4);
                Integer Price=rs.getInt(5);                
                                                
                list.add(new Cust(ID,Title,Date,quant,Price));
            }            
        } 
        catch (SQLException ex) 
        {
            System.out.println("Exception in CustomerDetailsController loadData()\n"+ex);
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
        tableview.setItems(list);
    }

    public static class Cust
    {
       
        private final SimpleStringProperty id;
        private final SimpleStringProperty title;
        private final SimpleStringProperty date;
        private final SimpleIntegerProperty quantity;
        private final SimpleIntegerProperty price;

       public Cust(String ID, String Title, String Date, Integer quant, Integer Price) 
       {
            this.id=new SimpleStringProperty(ID);
            this.title=new SimpleStringProperty(Title);
            this.date=new SimpleStringProperty(Date);
            this.quantity=new SimpleIntegerProperty(quant);
            this.price=new SimpleIntegerProperty(Price);
       }

        public String getId() {
            return id.get();
        }

        public String getTitle() {
            return title.get();
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
