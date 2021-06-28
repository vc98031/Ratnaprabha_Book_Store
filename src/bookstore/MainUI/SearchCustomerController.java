
package bookstore.MainUI;

import bookstore.database.DatabaseHandler;
import com.jfoenix.controls.JFXButton;
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
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.TextFields;

public class SearchCustomerController implements Initializable {
    @FXML
    private Text custname;
    @FXML
    private Text custmno;
    @FXML
    private Text custmail;
    @FXML
    private TextField search;
    @FXML
    private JFXButton searchBtn;

    DatabaseHandler handler ;
    Connection conn;
    Statement stmt;
    ResultSet rs;
    
    Notifications notify;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        notify=Notifications.create();
        try 
        {
            handler=DatabaseHandler.getInstance();
            conn=handler.createConnection();
            stmt=conn.createStatement();
            rs=stmt.executeQuery("select count(cust_id) from customer");
            int column=rs.getInt(1);
            int i=0;
            
            String [] ID=new String[column];                        
            
            rs=stmt.executeQuery("select  Cust_ID from Customer");                  
                while(rs.next()&& i<column)
                {
                    ID[i]=Integer.toString(rs.getInt(1));
                    ++i;
                }           
                
                TextFields.bindAutoCompletion(search,ID);
        } 
        catch (SQLException ex) 
        {
            System.out.println("Exception in SearchCustomerController initialize()\n"+ex);            
        }
        finally
        {
            if(conn!=null)
                try {
                    conn.close();
            } catch (SQLException ex) {
                System.out.println("Exception in connection not closed SearchCustomerController initialize()\n"+ex);            
            }
        }
    }    

    @FXML
    private void searchCustID(ActionEvent event) 
    {
        try 
        {
            handler=DatabaseHandler.getInstance();
            conn=handler.createConnection();
            stmt=conn.createStatement();
            rs=stmt.executeQuery("select cust_name,cust_mbno,cust_email from customer where cust_id='"+Integer.parseInt(search.getText())+"'");
            
            custname.setText(rs.getString(1));
            custmno.setText(rs.getString(2));
            custmail.setText(rs.getString(3));
            
                    notify.title("Search Completed !");                                        
                    notify.hideAfter(Duration.seconds(2));                
                    notify.position(Pos.CENTER);  
                    notify.darkStyle();
                    notify.showInformation();
                    return;              
        } catch (SQLException | NumberFormatException ex) {
            notify.title("Customer Not Found !");                                        
                    notify.hideAfter(Duration.seconds(2));                
                    notify.position(Pos.CENTER);  
                    notify.darkStyle();
                    notify.showError();
            
            System.out.println("Exception in SearchCustomerController searchCustID()\n"+ex);            
        }
        finally
        {
            if(conn!=null)
                try {
                    conn.close();
            } catch (SQLException ex) {
                System.out.println("Exception in connection not closed SearchCustomerController searchCustID()\n"+ex);            
            }
        }
        
            
    }
    
}
