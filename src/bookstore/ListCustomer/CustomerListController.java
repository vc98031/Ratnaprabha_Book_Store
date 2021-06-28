package bookstore.ListCustomer;

import bookstore.ListBooks.Book_listController;
import bookstore.addCustomer.CustAddController;
import bookstore.addbooks.AddBookController;
import bookstore.database.DatabaseHandler;
import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;


public class CustomerListController implements Initializable 
{
    ObservableList <Customer> list =FXCollections.observableArrayList();
    DatabaseHandler handler;
    Connection conn;
    Statement stmt;
    ResultSet rs;
    
    Notifications notify;
    
    @FXML
    private TableView<Customer> tableview;    
    @FXML
    private TableColumn<Customer,Integer> CustID;
    @FXML
    private TableColumn<Customer,String> CustName;
    @FXML
    private TableColumn<Customer,String> Mbno;
    @FXML
    private TableColumn<Customer,String> EmID;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        notify=Notifications.create();
        initCol();
        loadData();
    }    
    
    
   /*stmt.execute("create table "+TABLE_NAME+"("
                        +"Cust_ID int(5) primary key,"
                        +"Cust_Name char(20) not null,"                                                
                        +"Cust_Mbno int(12)not null,"
                        +"Cust_Email char(20)"
                        +")");*/
    
    
    private void initCol() 
    {
        CustID.setCellValueFactory(new PropertyValueFactory<>("cid"));
        CustName.setCellValueFactory(new PropertyValueFactory<>("cname"));
        Mbno.setCellValueFactory(new PropertyValueFactory<>("cmbno"));
        EmID.setCellValueFactory(new PropertyValueFactory<>("cmail"));        
    }

    private void loadData() 
    {
        try 
        {            
            list.clear();
            handler=DatabaseHandler.getInstance();
            conn=handler.createConnection();        
            stmt=conn.createStatement();
            rs=stmt.executeQuery("Select * from Customer");                       
            while(rs.next())
            {   
                Integer ID=rs.getInt(1);
                String Name=rs.getString(2);
                String Mblno=rs.getString(3);
                String EmlID=rs.getString(4);
                
                list.add(new Customer(ID,Name,Mblno,EmlID));
            }            
        } 
        catch (SQLException ex) 
        {
            System.out.println("Exception in CustomerListController loadData()\n"+ex);
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
                System.out.println("Connection closed Customer loadData()\n"+ex);
            }
        } 
        tableview.setItems(list);
    }

    @FXML
    private void editCustInfo(ActionEvent event) 
    {
        Customer editOption=tableview.getSelectionModel().getSelectedItem();
        if(editOption==null)
        {
            notify.title("Please Select Customer For Deletion.");                    
            notify.hideAfter(Duration.seconds(2));                
            notify.position(Pos.CENTER);                
            notify.darkStyle();
            notify.showError(); 
            return;
        }
        try 
        {        
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/bookstore/addCustomer/CustAdd.fxml"));
            Parent root = loader.load();     
            CustAddController controller=(CustAddController)loader.getController();
            controller.inflatUI(editOption);
            Scene scene = new Scene(root);        
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.setOnCloseRequest((e)->{refresh(new ActionEvent());});
            stage.setTitle("Edit Customer "+editOption.getCid());
            stage.getIcons().add(new Image("bookstore/icons/add.png"));           
            stage.setResizable(false);            
            stage.show();
        }
        catch (IOException ex) 
        {
            System.out.println("Exception in MainUIController addBook()\n"+ex);
        }   
        return;
    }

    @FXML
    private void deleteCustInfo(ActionEvent event) 
    {
        Customer deleteOption=tableview.getSelectionModel().getSelectedItem();
        if(deleteOption==null)
        {
            notify.title(" Please Select Customer For Deletion.");                    
            notify.hideAfter(Duration.seconds(2));                
            notify.position(Pos.CENTER);                
            notify.darkStyle();
            notify.showError(); 
            return;
        }
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleting Customer");
        alert.setContentText("Are You Want to Deleting The Customer "+deleteOption.getCid()+" ?");
        Optional<ButtonType> answer=alert.showAndWait();
        if(answer.get()==ButtonType.OK)
        {
            if(DatabaseHandler.getInstance().deleteCustomer(deleteOption))
                list.remove(deleteOption);
            tableview.refresh();
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

    @FXML
    private void refresh(ActionEvent event) 
    {
        tableview.refresh();
    }
    
    public static class Customer
    {
        private final SimpleIntegerProperty cid;        
        private final SimpleStringProperty cmbno;        
        private final SimpleStringProperty cname;                         
        private final SimpleStringProperty cmail;        
        
        public Customer(Integer ID,String Name,String Mbno,String EmID)
        {            
            this.cid=new SimpleIntegerProperty(ID);            
            this.cmbno=new SimpleStringProperty(Mbno);
            this.cname=new SimpleStringProperty(Name);
            this.cmail=new SimpleStringProperty(EmID);            
        } 

        public Integer getCid() {
            return cid.get();
        }

        public String getCmbno() {
            return cmbno.get();
        }

        public String getCname() {
            return cname.get();
        }

        public String getCmail() {
            return cmail.get();
        }
        
    }    
}
