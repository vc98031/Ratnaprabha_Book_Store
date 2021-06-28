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
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author Jayprasad
 */
public class BookDetailsController implements Initializable 
{
    DatabaseHandler handler;
    Connection conn;
    PreparedStatement stmt;
    ResultSet rs;
    
    ObservableList <BookDetails> list =FXCollections.observableArrayList();
    
    @FXML
    private TableView<BookDetails> tableview;
    @FXML
    private TableColumn<BookDetails,Integer> custID;
    @FXML
    private TableColumn<BookDetails,String> custName;
    @FXML
    private TableColumn<BookDetails,String> date;
    @FXML
    private TableColumn<BookDetails,Integer> quantity;
    @FXML
    private TableColumn<BookDetails,Integer> price;

    
    static String bkid;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        // TODO       
        handler=DatabaseHandler.getInstance();
        initCol();       
        loadData();
    }    
     private void initCol() 
    {
        custID.setCellValueFactory(new PropertyValueFactory<>("cid"));
        custName.setCellValueFactory(new PropertyValueFactory<>("name"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));        
    }

    public void loadData() 
    {
        int count=0;
        try 
        {                        
            conn=handler.createConnection();        
            stmt=conn.prepareStatement("Select count(Customer) from SoldBook where bid=?");
            stmt.setString(1,bkid);            
            rs=stmt.executeQuery();
            count=rs.getInt(1);
            System.out.println("Customer count="+count);
            
            stmt=conn.prepareStatement("Select Customer from SoldBook where bid=?");
            stmt.setString(1,bkid);            
            rs=stmt.executeQuery();
            
            String Custname[]=new String[count];
            Integer id[]=new Integer[count];
            
            int i=0;
            while(rs.next())
            {
               id[i]=rs.getInt(1);             
               System.out.println("customer id="+id[i]);
               ++i;
            } 
            System.out.println("ID length:"+id.length);
            
           for(int j=0;j<id.length;j++)
            {
                stmt=conn.prepareStatement("Select cust_name from customer where cust_id=?");
                stmt.setInt(1,id[j]);            
                rs=stmt.executeQuery();
                System.out.println("Customer Name:"+rs.getString(1));
                Custname[j]=rs.getString(1);
                System.out.println("customer Name="+Custname[j]);
            }
            
            stmt=conn.prepareStatement("Select Customer,Date,Quantity,Price from SoldBook where bid=?");
            stmt.setString(1,bkid);            
            rs=stmt.executeQuery();
            int j=0;
            while(rs.next())
            {                        
                System.out.println(rs.getInt(1)+" "+rs.getString(2)+" "+rs.getInt(3)+" "+rs.getInt(4));
                list.add(new BookDetails(rs.getInt(1),Custname[j],rs.getString(2),rs.getInt(3),rs.getInt(4)));
                ++j;
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
        //tableview.setItems(list);
        tableview.getItems().addAll(list);
    }

   
    public static class BookDetails
    {
        private final SimpleIntegerProperty cid;        
        private final SimpleStringProperty name;                
        private final SimpleStringProperty date;        
        private final SimpleIntegerProperty quantity;                
        private final SimpleIntegerProperty price;                
        
        //   list.add(new BookDetails(rs.getInt(1),Custname[i],rs.getString(2),rs.getInt(3),rs.getInt(4)));
        BookDetails(Integer cid,String cname,String Date,Integer Quantity,Integer Price)
        {            
            this.cid=new SimpleIntegerProperty(cid);
            this.name=new SimpleStringProperty(cname);
            this.date=new SimpleStringProperty(Date);
            this.quantity=new SimpleIntegerProperty(Quantity);
            this.price=new SimpleIntegerProperty(Price);
        }

        public Integer getCid() {
            return cid.get();
        }

        public String getName() {
            return name.get();
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
