package bookstore.addCustomer;

import bookstore.ListCustomer.CustomerListController;
import bookstore.ListCustomer.CustomerListController.Customer;
import bookstore.database.DatabaseHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.TextFields;

public class CustAddController implements Initializable 
{
    @FXML
    private Pane pane;
    @FXML
    private JFXTextField cust_name;
    @FXML
    private JFXTextField cust_id;
    @FXML
    private JFXTextField mb_no;
    @FXML
    private JFXTextField email_id;
    @FXML
    private JFXButton save;
    @FXML
    private JFXButton cancel;
    
    DatabaseHandler databasehandler;
    PreparedStatement stmt;        
    Connection conn;
    ResultSet rs;
    Statement stmts;
    Notifications notify;
    
    boolean firstCust=true;
    boolean isInEditMode=false;
    int count;
                
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {         
        cust_id.setText("101");
        try 
        {
            databasehandler =DatabaseHandler.getInstance();
            conn=databasehandler.createConnection();
            notify=Notifications.create();                            
            count=0;
            int i=0;
            if(conn!=null)
            {
                System.out.println("DB connected succesfully...");              
                stmts=conn.createStatement();
                
                rs=stmts.executeQuery("select count(cust_id) from Customer");
                while(rs.next())
                    count=rs.getInt(1);
                                
                Integer IDList[]=new Integer[count];
                String NameList[]=new String[count];                
                String MbnoList[]=new String[count];
                String EmIdList[]=new String[count];                                
                                                
                
                rs=stmts.executeQuery("select * from Customer");                
                while(rs.next()&&i<count)
                {
                    System.out.print(rs.getInt(1)+"  ");
                    System.out.print(rs.getString(2)+"  ");
                    System.out.print(rs.getString(3)+"  ");
                    System.out.print(rs.getString(4)+"  ");                    
                    
                    IDList[i]=rs.getInt(1);
                    NameList[i]=rs.getString(2);                    
                    MbnoList[i]=rs.getString(3);
                    EmIdList[i]=rs.getString(4);
                    
                    ++i;
                    firstCust=false;
                }                                 
                //TextFields.bindAutoCompletion(cust_id,IDList);
                TextFields.bindAutoCompletion(cust_name,NameList);
                TextFields.bindAutoCompletion(mb_no,MbnoList);
                TextFields.bindAutoCompletion(email_id,EmIdList);                
                setter(count);
            }
            else
                System.out.println("DB not connected...");            
        }
        catch (SQLException ex) 
        {
            setter(0);
            System.out.println("Exception raised in initialize Controller\n"+ex);            
        } 
        finally
        {
            try 
            {
                
                conn.close();
            } 
            catch (SQLException ex) 
            {
                System.out.println("Exception in initialize "+ex);
            }
        }
    }    

    private void setter(int upcount) 
    {
      /*  if(firstCust)            
        {
            System.out.println("firstCust");
            cust_id.setText("101");
            cust_name.setText("");
            mb_no.setText("");
            email_id.setText("");        
        }
        else*/
        {
            System.out.println("setter count="+count);
            cust_id.setText(Integer.toString(Integer.parseInt(cust_id.getText())+upcount));                
            cust_name.setText("");
            mb_no.setText("");
            email_id.setText("");        
        }
    }
    
    @FXML
    private void addCust(ActionEvent event) 
    {   
        
        //String cid=cust_id.getText();        
        String cname=cust_name.getText();
        String cmbno=mb_no.getText();
        String emid=email_id.getText();        
                        
        if(cname.isEmpty()||cmbno.isEmpty()||emid.isEmpty())
        {
                    notify.title("Please Input All The Fields.");                    
                    notify.hideAfter(Duration.seconds(1));                
                    notify.position(Pos.CENTER);                
                    notify.darkStyle();
                    notify.showError();
        }
        else
        {            
            try 
            {   if(isInEditMode)
                {
                    handleEditOperation();
                    return;
                }                
                if(!isAvail(cname,cmbno,emid))
                {
                    conn=databasehandler.createConnection();                
                    stmt=conn.prepareStatement("insert into Customer values(?,?,?,?)");                                                                                   
                    stmt.setInt(1,Integer.parseInt(cust_id.getText()));
                    stmt.setString(2,cname);
                    stmt.setString(3,cmbno);
                    stmt.setString(4,emid);
                    
                    System.out.println(stmt.executeUpdate()+" customer added row affected..");
                    setter(1);
                    notify.title("Customer "+cname+" added...");                    
                    notify.hideAfter(Duration.seconds(1));                
                    notify.position(Pos.CENTER);                
                    notify.darkStyle();
                    notify.showInformation();                                       
                                        
                }                
                else
                {
                    notify.title("Customer "+cname+" already exist!");                    
                    notify.hideAfter(Duration.seconds(1));                
                    notify.position(Pos.CENTER);                
                    notify.darkStyle();
                    notify.showWarning();                    
                }
            } 
            catch (Exception ex) 
            {
                notify.title("Please Input Valid Fields !");                    
                notify.hideAfter(Duration.seconds(1));                
                notify.position(Pos.CENTER);                
                notify.showError();
                notify.darkStyle();
                System.out.println("Exception occured in addCust() event :\n"+ex);
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
                    System.out.println("connection not closed in bookAdd :\n"+ex);                    
                }                
            }
        }                   
    }
            
    public boolean isAvail(String cname,String cmbno,String emid)
    {
        try 
        {
                boolean check=false;
                conn=databasehandler.createConnection();
                stmts=conn.createStatement();
                rs=stmts.executeQuery("select cust_name,cust_mbno,cust_Email from Customer");                
                while(rs.next())
                {
                    if((rs.getString(1).equals(cname))&&(rs.getString(2).equals(cmbno)) && rs.getString(3).equals(emid))                    
                    {      
                        System.out.println("In db"+rs.getInt(1));
                        System.out.println("In db"+rs.getString(2));
                        System.out.println("In db"+rs.getString(3));
                        System.out.println("In db"+rs.getString(4));
                        
                        check=true;           
                    }
                }                
                System.out.println(check+" in isAvail() for add Customer");
                return check;
        }
        catch (SQLException ex) 
        {        
            System.out.println("Exception in check()\n"+ex);
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
                System.out.println("connection not closed in check() :\n"+ex);
            }
        }
        return false;
    }
        
    @FXML
    private void cancel(ActionEvent event) 
    {
        cust_id.setText("");
        cust_name.setText("");
        mb_no.setText("");
        email_id.setText("");                
    }    
    
    public void inflatUI(Customer cust)
    {
        cust_id.setText(Integer.toString(cust.getCid()));                       
        cust_name.setText(cust.getCname());
        mb_no.setText(cust.getCmbno());
        email_id.setText(cust.getCmail());     
        
        isInEditMode=true;
    }

    public void handleEditOperation() 
    {
        /*
        String cname=cust_name.getText();
        String cmbno=mb_no.getText();
        String emid=email_id.getText();        
        
        Integer ID,String Name,String Mbno,String EmID)
        */        
        CustomerListController.Customer cust=new CustomerListController.Customer(new Integer(Integer.parseInt(cust_id.getText())),cust_name.getText(),mb_no.getText(),email_id.getText());
        if(DatabaseHandler.getInstance().updateCustomer(cust))
        {
             notify.title("Customer Updated !");                    
                        notify.darkStyle();
                        notify.hideAfter(Duration.seconds(2));                
                        notify.position(Pos.CENTER);                
                        notify.showInformation();     
        }
        else
        {
             notify.title("Customer not Updated !");                    
                        notify.darkStyle();
                        notify.hideAfter(Duration.seconds(2));                
                        notify.position(Pos.CENTER);                
                        notify.showInformation();     
        }
    }
}
