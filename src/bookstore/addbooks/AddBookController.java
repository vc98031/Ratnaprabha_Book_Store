/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstore.addbooks;

import bookstore.ListBooks.Book_listController;
import bookstore.QRCodes.Generator;
import bookstore.database.DatabaseHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Jayprasad
 */
public class AddBookController implements Initializable 
{
    
    
    PreparedStatement stmt;    
    DatabaseHandler databasehandler;
    Connection conn;
    ResultSet rs;
    Statement stmts;
    Notifications notify;
           
    String ID;
    String Title;
    String Author;
    String Publisher;
    String Price;
    String Date;
    String Time;
    String Avbl;
    String Book_Text="";
    int AvailBook;
     
    Boolean isInEditMode=Boolean.FALSE;
    
    @FXML
    private StackPane root;
    @FXML
    private AnchorPane anchor;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField name;
    @FXML
    private JFXDatePicker date;
    @FXML
    private JFXTextField author;
    @FXML
    private JFXTextField price;
    @FXML
    private JFXTextField copies;
    @FXML
    private JFXTimePicker time;    
    @FXML
    private Circle img1;
    @FXML
    private Circle circle;
    @FXML
    private JFXTextField publisher;
    @FXML
    private ImageView imageview;

    /**
     * Initializes the controller class.
     */
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        
        //circle.setFill(new ImagePattern(new Image("/bookstore/icons/file1.png")));        
        id.setText("B");
        try 
        {
            databasehandler =DatabaseHandler.getInstance();
            conn=databasehandler.createConnection();
            notify=Notifications.create();            
            
            int count=0,i=0;
            
            if(conn!=null)
            {
                stmts=conn.createStatement();
                rs=stmts.executeQuery("select count(id) from book");
                while(rs.next())
                    count=rs.getInt(1);
                
                String IDList[]=new String[count];
                String TitleList[]=new String[count];                
                String AuthorList[]=new String[count];
                String PublisherList[]=new String[count];                             
                String PriceList[]=new String[count];
                                                               
                rs=stmts.executeQuery("select DISTINCT ID,Title,Author,Publisher,Price from book");  
                System.out.println("Exist books in Book Table:");
                while(rs.next()&& i<count)
                {
                    System.out.print(rs.getString(1)+"  ");
                    System.out.print(rs.getString(2)+"  ");
                    System.out.print(rs.getString(3)+"  ");
                    System.out.print(rs.getString(4)+"  ");
                    System.out.println(rs.getString(5)+"  ");
                                        
                    IDList[i]=rs.getString(1);
                    TitleList[i]=rs.getString(2);                    
                    AuthorList[i]=rs.getString(3);
                    PublisherList[i]=rs.getString(4);                    
                    PriceList[i]=Integer.toString(rs.getInt(5));                    
                    
                    ++i;
                }           
                
                TextFields.bindAutoCompletion(id,IDList);
                TextFields.bindAutoCompletion(name,TitleList);                
                TextFields.bindAutoCompletion(author,AuthorList);
                TextFields.bindAutoCompletion(publisher,PublisherList);
                TextFields.bindAutoCompletion(price,PriceList);                
                
            }
            else
                System.out.println("DB not connected...");            
        }
        catch (SQLException ex) 
        {
            System.out.println("Exception raised in AddBookController class - initialize ()\n"+ex);            
        } 
        finally
        {
            try 
            {
                conn.close();
            } 
            catch (SQLException ex) 
            {
                System.out.println("Exception in BookStoreController class -  initialize\n "+ex);
            }
        }
        
        
    }    

    @FXML
    private void reset_event(ActionEvent event) 
    {
        refresh();
    }
    
    private void refresh()
    {
        try
        {
            StackPane pane =FXMLLoader.load(getClass().getResource("/bookstore/addbooks/AddBook.fxml"));
            root.getChildren().setAll(pane);
        }
        catch (IOException ex) {
            System.out.println("Exception in AddBookController reset_event()\n"+ex);
        }
    }

    @FXML
    private void add_event(ActionEvent event) 
    {
        
        int Row_Affect=0;                             
              
        ID=id.getText();
        Title=name.getText();
        Author=author.getText();
        Publisher=publisher.getText();
        Price=price.getText();
        Date=date.getValue().toString();
        Time=time.getValue().toString();
        Avbl=copies.getText();
        
        System.out.println("Entered Data:");
        System.out.print(ID+" ");
        System.out.print(Title+" ");
        System.out.print(Author+" ");
        System.out.print(Publisher+" ");
        System.out.print(Price+" ");
        System.out.print(Date+" ");
        System.out.println(Time+" ");
        System.out.println(Avbl+" ");
           
        
        if(ID.isEmpty()||Title.isEmpty()||Author.isEmpty()||Price.isEmpty()||Publisher.isEmpty()||Date.isEmpty()||Time.isEmpty()||Avbl.isEmpty())
        {
                    notify.title("Please Input All The Fields!");                    
                    notify.hideAfter(Duration.seconds(1));                
                    notify.position(Pos.CENTER);  
                    notify.darkStyle();
                    notify.showError();
                    return;                                       
        }
        else if(!ID.startsWith("B"))
        {
                    notify.title("Book ID Must Start With B");                    
                    notify.hideAfter(Duration.seconds(1));                
                    notify.position(Pos.CENTER);  
                    notify.darkStyle();
                    notify.showError();
                    return;                                       
        }       
        else
        {            
            AvailBook=bookQuantityFinder(ID);
            
            System.out.println("Before insert Available Book is: "+AvailBook);
            try 
            {
                if(isInEditMode)
                {
                    handleEditOperation();
                    return;
                }
                if(!isAvail(ID,Title,Author,Publisher,Price,Date,Time,Avbl))
                {
                    conn=databasehandler.createConnection();                
                    stmt=conn.prepareStatement("insert into BOOK values(?,?,?,?,?,?,?)");                                                               
                    stmt.setString(1,ID);
                    stmt.setString(2,Title);
                    stmt.setString(3,Author);
                    stmt.setString(4,Publisher);
                    stmt.setString(5,Date+"  "+Time);
                    stmt.setInt(6,Integer.parseInt(Price));
                    stmt.setInt(7,Integer.parseInt(Avbl));
                    
                    Row_Affect=stmt.executeUpdate();
                    System.out.println(Row_Affect+" row affected..");                                              
                    
                    notify.title("Book "+Title+" added!");                    
                    notify.hideAfter(Duration.seconds(1));                
                    notify.position(Pos.CENTER);                
                    notify.darkStyle();
                    notify.showWarning();
                }                                
            } 
            catch (SQLException | NumberFormatException ex) 
            {
                notify.title("Please Input Valid Fields!");                    
                notify.hideAfter(Duration.seconds(1));                
                notify.position(Pos.CENTER);                
                notify.darkStyle();
                notify.showError();
                
                System.out.println("Exception occured in BookStoreController class -  bookAdd event :\n"+ex);
            }            
            finally
            {                                
                if(conn!=null||stmt!=null)
                try
                {
                    conn.close();
                    stmt.close();
                }
                catch (SQLException ex) 
                {
                    System.out.println("connection not closed in  BookStoreController class -  bookAdd() :\n"+ex);
                }
                return;
            }
        }                 
    }
    
    public boolean isAvail(String ID,String Title,String Author,String Publisher,String Price,String Date,String Time,String Avbl)
    {
        try 
        {
                boolean check=false;
                
                conn=databasehandler.createConnection();
                stmts=conn.createStatement();
                rs=stmts.executeQuery("select * from book");
                
                while(rs.next())
                {
                    if((rs.getString(1).equalsIgnoreCase(ID))&&(rs.getString(2).equalsIgnoreCase(Title))&&
                    (rs.getString(3).equalsIgnoreCase(Author))&&(rs.getString(4).equalsIgnoreCase(Publisher))&&(rs.getInt(6)==Integer.parseInt(Price)))
                    {
                        stmt=conn.prepareStatement("update book set Avail=? where ID=?");
                        stmt.setInt(1,rs.getInt(7)+Integer.parseInt(Avbl));
                        stmt.setString(2,rs.getString(1));
                        
                        System.out.println("1 row updated...");                        
                        notify.title("Book "+Title+" added!");                    
                        notify.darkStyle();
                        notify.hideAfter(Duration.seconds(1));                
                        notify.position(Pos.CENTER);                
                        notify.showWarning();                        
                        
                        check=true;
                    }
                }
                
                System.out.println(check+" in AddBookController isAvail()");
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
                System.out.println("connection not closed in AddBookController isAvail() :\n"+ex);
            }
        }
        return false;     
    }

    private boolean isQRCodeAvailableInFile()
    {
           String qrCodeList[]=new File("src\\bookstore\\QRCodes\\images\\").list();                      
           String qrImageNames;
           for(int j=0;j<qrCodeList.length;++j)
           {
               qrImageNames=qrCodeList[j];               
               return qrImageNames.startsWith(ID);                                 
           }          
           return false;
    }
    
    @FXML
    private void barcode_event(ActionEvent event) 
    {
       try 
       {                       
           String qrCodeList[]=new File("src\\bookstore\\QRCodes\\images\\").list();
           System.out.println("Avalable QR images:");           
           int qrcodelist[]=new int[qrCodeList.length];
           int counter=0,k=0;
           String qrImageNames;
           for(int j=0;j<qrCodeList.length;++j)
           {
               qrImageNames=qrCodeList[j];               
               if(qrImageNames.startsWith(ID))               
                   qrcodelist[k]=Integer.parseInt(qrImageNames.substring(qrImageNames.indexOf("_")+1,qrImageNames.indexOf(".")));                                  
               ++k;
           }
           
           int qrid[]=generateQRID();
            int i=0;            
            System.out.println("In QRCodeGenerator:\n\n");
            
            if(qrid.length==0 & qrcodelist.length==0)
            {
                System.out.println("1");
                   for(i=1;i<=Integer.parseInt(Avbl);i++)               
                    Generator.generateQRCodeImage(ID+"_"+i+","+Title+","+Author+","+Publisher+","+Price+","+Date+" "+Time,new File("src\\bookstore\\QRCodes\\images\\"+ID+"_"+i+".png"));                                                            

                imageview.setImage(new Image(new FileInputStream("src\\bookstore\\QRCodes\\images\\"+ID+"_"+(i-1)+".png")));            
            }
            else if(Integer.parseInt(Avbl)<=qrid.length)                
            {
                System.out.println("2");
                for(i=0;i<Integer.parseInt(Avbl);i++)               
                    Generator.generateQRCodeImage(ID+"_"+qrid[i]+","+Title+","+Author+","+Publisher+","+Price+","+Date+" "+Time,new File("src\\bookstore\\QRCodes\\images\\"+ID+"_"+(qrid[i])+".png"));                                                            
                
                imageview.setImage(new Image(new FileInputStream("src\\bookstore\\QRCodes\\images\\"+ID+"_"+(qrid[i-1])+".png")));
            }
            else if(Integer.parseInt(Avbl)>=qrid.length &qrcodelist.length>0)                
            {
                System.out.println("3");
                for(i=0;i<qrid.length;i++)               
                    Generator.generateQRCodeImage(ID+"_"+qrid[i]+","+Title+","+Author+","+Publisher+","+Price+","+Date+" "+Time,new File("src\\bookstore\\QRCodes\\images\\"+ID+"_"+(qrid[i])+".png"));                                                            
                
                if(isQRCodeAvailableInFile())
                    for(i=qrcodelist[qrcodelist.length-1]+1;i<(Integer.parseInt(Avbl)-qrid.length);++i)
                        Generator.generateQRCodeImage(ID+"_"+i+","+Title+","+Author+","+Publisher+","+Price+","+Date+" "+Time,new File("src\\bookstore\\QRCodes\\images\\"+ID+"_"+i+".png"));
                else
                    for(i=1;i<=Integer.parseInt(Avbl);i++)               
                        Generator.generateQRCodeImage(ID+"_"+i+","+Title+","+Author+","+Publisher+","+Price+","+Date+" "+Time,new File("src\\bookstore\\QRCodes\\images\\"+ID+"_"+i+".png"));                                                            
    
                imageview.setImage(new Image(new FileInputStream("src\\bookstore\\QRCodes\\images\\"+ID+"_"+(i-1)+".png")));
            }
            else
            {
                System.out.println("4");
                for(i=1;i<=Integer.parseInt(Avbl);i++)               
                    Generator.generateQRCodeImage(ID+"_"+i+","+Title+","+Author+","+Publisher+","+Price+","+Date+" "+Time,new File("src\\bookstore\\QRCodes\\images\\"+ID+"_"+i+".png"));                                                            

                imageview.setImage(new Image(new FileInputStream("src\\bookstore\\QRCodes\\images\\"+ID+"_"+(i-1)+".png")));
            }
                         
        } 
       
        catch (FileNotFoundException ex) 
        {
            System.out.println("Exception in AddBookController barcode_event()\n"+ex);
        }
       finally
        {
                    if(conn!=null)
                            try {
                                conn.close();
                    } catch (SQLException ex) {
                        System.out.println("Connection not closed in AddBookContrller bookQuantityFinder()\n"+ex);
                    }
        }
        return;
    }
    
    
    private int[] generateQRID()
    {
        try 
        {
            conn=databasehandler.createConnection();
            stmt=conn.prepareStatement("select count(QRID),QRID from SoldBookQRID where BID=?");
            stmt.setString(1,ID);
            rs=stmt.executeQuery();
            
            int []qrid=new int[rs.getInt(1)];
            int index=0;
            if(rs.getInt(1)>0)
                while(rs.next())                
                    qrid[index]=rs.getInt(2);
            return qrid;
        }
        catch (SQLException ex) 
        {
           System.out.println("Exception in AddBookController gernerateQRID()\n"+ex);
        }
        finally
        {
                    if(conn!=null)
                            try {
                                conn.close();
                    } catch (SQLException ex) {
                        System.out.println("Connection not closed in AddBookContrller bookQuantityFinder()\n"+ex);
                    }
        }
        return null;                                      
    }
    
    private int bookQuantityFinder(String ID)
    {        
        conn=databasehandler.createConnection();
        try
        {            
            stmt=conn.prepareStatement("select Avail from Book where ID=?");
            stmt.setString(1,ID);
            rs=stmt.executeQuery();
            Object result=rs.getInt(1);
            if(result!=null)
                return Integer.parseInt(result.toString());            
        }
        catch (SQLException ex) 
        {
            System.out.println("Exception in AddBooksController bookQuantityFinder()\n"+ex);
        }
        finally
        {
                    if(conn!=null)
                            try {
                                conn.close();
                    } catch (SQLException ex) {
                        System.out.println("Connection not closed in AddBookContrller bookQuantityFinder()\n"+ex);
                    }
        }
        return 0;
    }
    
    
    @FXML
    private void load_img(MouseEvent event) {}
    
    
    
    public void inflatUI(Book_listController.Book book)
    {
        id.setText(book.getId());                
        id.setEditable(false);
        date.setEditable(false);
        time.setEditable(false);
        copies.setText(Integer.toString(book.getAvailable()));
        copies.setEditable(false);
        
        name.setText(book.getTitle());        
        author.setText(book.getAuthor());        
        publisher.setText(book.getPublisher());        
        price.setText(Integer.toString(book.getPrice()));        
        
        isInEditMode=Boolean.TRUE;
    }
/*
    stmt.execute("create table "+TABLE_NAME+"("
                        +"ID char(10) primary key,"                 //1
                        +"Title char(20) not null,"                 //2
                        +"Author char(20),"                         //3
                        +"Publisher char(20) not null,"             //4
                        +"Date char(50)not null,"                   //5
                        +"Price int(4)not null,"                    //6                         
                        +"Avail int(4)not null"                     //7
                        +")");
    
  */  public void handleEditOperation() 
    {
       //String title,String id,String author,String publisher,Integer price,Integer available)
      /*  
        ID=id.getText();
        Title=name.getText();
        Author=author.getText();
        Publisher=publisher.getText();
        Price=price.getText();
        Date=date.getValue().toString();
        Time=time.getValue().toString();
        Avbl=copies.getText();                
      */  
        
       Book_listController.Book book=new Book_listController.Book(Title,ID,Author,Publisher,new Integer(Integer.parseInt(Price)),new Integer(Integer.parseInt(Avbl)));
       if(DatabaseHandler.getInstance().updateBook(book))
       {
                        notify.title("Book Updated !");                    
                        notify.darkStyle();
                        notify.hideAfter(Duration.seconds(2));                
                        notify.position(Pos.CENTER);                
                        notify.showInformation();                        
       }
       else
       {
                        notify.title("Book not  Updated !");                    
                        notify.darkStyle();
                        notify.hideAfter(Duration.seconds(2));                
                        notify.position(Pos.CENTER);                
                        notify.showInformation();                        
       }
    }
}




