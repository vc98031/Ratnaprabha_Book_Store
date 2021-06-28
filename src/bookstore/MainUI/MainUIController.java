package bookstore.MainUI;

import bookstore.Invoice.Bill_Generator;
import bookstore.QRCodes.Generator;
import bookstore.QRCodes.RemoveQRCode;
import bookstore.database.DatabaseHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.imageio.ImageIO;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.TextFields;

public class MainUIController implements Initializable 
{
    
    String CUST_ID;   
    
    String results[]=new String[6];
    String ids[]=new String[2];
    
    String bookName;
    String bookAuthor;
    String bookPublisher;
    String bookPrice;
    int bookAvail;
    
    String c_name;
    String c_mbno;
    String c_mail;
    
    String Current_Date="";
    
    boolean bookset=false;
    boolean customerkset=false;
    boolean checkPayment=false;
    
    boolean hammer_clicked=false;
    
    DatabaseHandler handler;
    PreparedStatement stmt;
    Statement stmts;
    Connection conn;
    ResultSet rs;
    
    Notifications notify;    
    FileChooser fileChooser;
    BufferedImage bfimage;
        
    public static int BookSelect=0;
    static int price=0;
    
    HamburgerSlideCloseTransition transition;
    
    @FXML
    private JFXButton addBook;
    @FXML
    private JFXButton searchBook;
    @FXML
    private JFXButton searchCust;
    @FXML
    private JFXButton soldBooks;
    @FXML
    private Tab payment;
    @FXML
    private TextField searchBox;       
    @FXML
    private JFXTabPane tabpane;    
    @FXML
    private Text btitle;
    @FXML
    private Text bauthor;
    @FXML
    private Text bpublisher;
    @FXML
    private Text bprice;
    @FXML
    private TextField custid;
    @FXML
    private Text custname;
    @FXML
    private Text custmno;
    @FXML
    private Text custmail;
    @FXML
    private Button newCustomer;        
    @FXML
    private Button pay;
    @FXML
    private JFXButton addCustomer;
    @FXML
    private ImageView QRImage;
    @FXML
    private Button chooseQRBtn;
    @FXML
    private JFXHamburger hamburger;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private AnchorPane anchor;
    @FXML
    private JFXButton searchBtn;
    @FXML
    private Button billBtn;
    @FXML
    private Text chooseBkCounter;
    @FXML
    private Text totalPrice;

    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {        
        BookSelect=0;
        String Menu[]={"Add Book","Add Customer","Search Book","Search Customer","Sold Book","Menu"};        
        
        TextFields.bindAutoCompletion(searchBox,Menu);
        
                new RemoveQRCode();
                handler=DatabaseHandler.getInstance();
                hamburgerLoader();
                notify=Notifications.create();                    
                chooseQRBtn.setOnAction(btnLoadEventListner);                        
                        
               pay.addEventHandler(MouseEvent.MOUSE_ENTERED,new  EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent e)
                    {
                        setCustInfo();
                    }
                }
                );
         
         handler=DatabaseHandler.getInstance();
         //emptyGenerateBill();
    }   
    
    EventHandler<ActionEvent> btnLoadEventListner =new EventHandler<ActionEvent>() 
    {
            @Override
            public void handle(ActionEvent event) 
            {
                try
                {
                    fileChooser=new FileChooser();
                    FileChooser.ExtensionFilter ext_png=new FileChooser.ExtensionFilter("PNG files (*.png)","*.png");
                    fileChooser.getExtensionFilters().add(ext_png);
                    File file=fileChooser.showOpenDialog(null);
                    
                    bfimage=ImageIO.read(file);
                    Image image=SwingFXUtils.toFXImage(bfimage,null);
                    QRImage.setImage(image);   
                    if(QRImage.getImage()!=null)
                    {
                        ++BookSelect;
                        chooseBkCounter.setText(Integer.toString(BookSelect));
                        setBookInfo(bfimage);
                    }
                }
                catch (IOException ex) 
                {
                    System.out.println("Exception in MainUIController EventHandler<>\n"+ex);
                }
                
            }
    };
    
    
    public void hamburgerLoader() 
    {
        try 
        {
            VBox box=FXMLLoader.load(getClass().getResource("/bookstore/MainUI/VBoxMenu.fxml"));
            drawer.setSidePane(box);
            transition=new HamburgerSlideCloseTransition(hamburger);
            transition.setRate(-1);
            System.out.println("rate="+transition.getRate());
            hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED,(e)->{                                                    
                
                if(drawer.isOpened())
                {                    
                    transition.setRate(-1);
                    transition.play();                                                                    
                    drawer.close();                    
                }
                else
                {
                    hammer_clicked=true;
                    transition.setRate(transition.getRate()*-1);                
                    transition.play();                                                                   
                    drawer.open();                
                }
            });
            
            if(searchBox.getText().equals("Menu"))
            searchBtn.addEventHandler(MouseEvent.MOUSE_CLICKED,(e)->{                                                    
                
                if(drawer.isOpened())
                {                    
                    transition.setRate(-1);
                    transition.play();                                                                    
                    drawer.close();                    
                }
                else
                {
                    hammer_clicked=true;
                    transition.setRate(transition.getRate()*-1);                
                    transition.play();                                                                   
                    drawer.open();                
                }
            });
        }
        catch (IOException ex) 
        {            
            System.out.println("Exception in hamburgerLoader()\n"+ex);
        }
    }



    private void setBookInfo(BufferedImage image) 
    {
        if(image!=null)
        try 
        {                                                            
            String result[]=Generator.decodeQRCodeImage(image).split(",",0);
            String id[]=result[0].split("_");
            
            
            for(int i=0;i<result.length;++i)
                results[i]=result[i];
            
            for(int i=0;i<id.length;++i)
                ids[i]=id[i];
    
                btitle.setText(result[1]);                
                bauthor.setText(result[2]);
                bpublisher.setText(result[3]);
                bprice.setText(result[4]);                                     
                
                bookName=result[1];
                bookAuthor=result[2];
                bookPublisher=result[3];
                bookPrice=result[4];                
                bookset=true;
                       
                price+=Integer.parseInt(bookPrice);
                System.out.println("In Condition3");
                updateGenerateBill(id[0],bookName,bookAuthor,Integer.parseInt(bookPrice));      
                conn=handler.createConnection();
                //stmt=conn.prepareStatement("select sum(price) from GenerateBill");
                //rs=stmt.executeQuery();                                
                if(Integer.parseInt(chooseBkCounter.getText())>1)                
                    totalPrice.setText("₹ "+price+" /-");    
                else
                    totalPrice.setText("₹ "+result[4]+" /-");
        }
        catch (Exception ex) 
        {
                   notify.title("Without QR Code Payment cannot be done !");                    
                    notify.hideAfter(Duration.seconds(1));                
                    notify.position(Pos.BASELINE_LEFT);    
                    notify.darkStyle();
                    notify.showError();                                
                   System.out.println("Exception in MainUIController setBookInfo()\n"+ex);
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
                System.out.println("connection not closed in MainUIController setBookInfo() :\n"+ex);
            }
        }
        else
        {           
                    notify.title("Without QR Code Payment cannot be done !");                    
                    notify.hideAfter(Duration.seconds(1));                
                    notify.position(Pos.BASELINE_LEFT);    
                    notify.darkStyle();
                    notify.showError();                                
                               
        }
    }

    private void setCustInfo() 
    {
        CUST_ID=custid.getText();
        try 
        {
            conn=handler.createConnection();
            
            stmt=conn.prepareStatement("select cust_id,cust_name,cust_mbno,cust_email from customer where cust_id=?");
            stmt.setInt(1,Integer.parseInt(CUST_ID));
            rs=stmt.executeQuery();
            
            
            if(!bookset)
            {
                    notify.title("Please First Enter The Book ID !\n");                    
                    notify.hideAfter(Duration.seconds(1));                
                    notify.position(Pos.BASELINE_CENTER);    
                    notify.darkStyle();
                    notify.showError();                                       
            }
            
            else if(Integer.parseInt(CUST_ID)==rs.getInt(1))
            {
                c_name=rs.getString(2);
                c_mbno=rs.getString(3);
                c_mail=rs.getString(4);
                
                custname.setText(c_name);
                custmno.setText(c_mbno);
                custmail.setText(c_mail);                
                customerkset=true;
            }            
        }
        catch (Exception ex) 
        {
            if(custid.getText()!=null & !bookset)
            {
                    notify.title("Please Enter Valid Customer ID !");                    
                    notify.hideAfter(Duration.seconds(1));                
                    notify.position(Pos.BASELINE_CENTER);    
                    notify.darkStyle();
                    notify.showError();                    
                   System.out.println("Exception in MainUIController setBookInfo()\n"+ex);
            }
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
                System.out.println("connection not closed in MainUIController setBookInfo() :\n"+ex);
            }
        }                              
    } 
       
    @FXML
    private void doPayment(ActionEvent event) 
    {                                       
       try
       {
           
            if(bookset && customerkset)
            {
                
                    updateBookTable(ids[0]);       
                    updateSoldBookTable(ids[0],results[1],results[3],Integer.parseInt(CUST_ID),Integer.parseInt(results[4]));                                                                               
                    updateSoldBookQRIDTable(ids[0],Integer.parseInt(ids[1]));                                        
                    RemoveQRCode.remove(new File("src\\bookstore\\QRCodes\\images\\"+results[0]+".png"));
                    
                    System.out.println("checkPayment="+checkPayment);
                    if(checkPayment)
                    {                                               
                        notify.title("Payment Successful !\n");                    
                        notify.hideAfter(Duration.seconds(3));                
                        notify.position(Pos.CENTER);                
                        notify.darkStyle();
                        notify.showInformation();
                    } 
                    refresh();
            }
            else if(bookAvail<=0)
            {
                    notify.title("Sorry  book not available !\nPleas buy another book!\n");                    
                    notify.hideAfter(Duration.seconds(1));                
                    notify.position(Pos.CENTER);                
                    notify.darkStyle();
                    notify.showError();                               
            }            
       }
        catch (NumberFormatException ex) 
        {
                    notify.title("Please Enter Valid Input !\n");                    
                    notify.hideAfter(Duration.seconds(1));                
                    notify.position(Pos.CENTER);                
                    notify.darkStyle();
                    notify.showError();                  
        }    
       System.out.println("show():\n");
       show();
    }
   
    
    
    private void refresh()
    {
        try
        {
            AnchorPane pane =FXMLLoader.load(getClass().getResource("/bookstore/MainUI/MainUI.fxml"));
            anchor.getChildren().setAll(pane);
        }
        catch (IOException ex) {
            System.out.println("Exception in MainUIController refresh()\n"+ex);
        }
    }

    
    private void updateSoldBookQRIDTable(String bid,int qrid) 
    {        
            try 
            {   
                
                if(conn!=null)
                    try 
                    {
                        conn.close();
                    }
                    catch (SQLException ex) {
                     System.out.println("Exception in MainUIController updateSoldBookTable\n"+ex);
                    }
                
                conn=handler.createConnection();                
                stmt=conn.prepareStatement("insert into SoldBookQRID values(?,?)");
                stmt.setString(1,bid);
                stmt.setInt(2,qrid);                
                
                System.out.println("SoldBookQRIDTable value updated!");
                                                
                if(stmt.executeUpdate()>0) checkPayment=true;                               
            }
            catch (SQLException ex) 
            {
                System.out.println("Exception MainUIController updateSoldBookQRIDTable() :\n"+ex);
            }
             finally
            {
                if(conn!=null)
                    try 
                    {
                        conn.close();
                    }
                    catch (SQLException ex) {
                     System.out.println("connection not closed MainUIController updateSoldBookQRIDTable\n"+ex);
                    }
            }
    }
    
    private void updateSoldBookTable(String bid,String btitle,String bauthor,int cid,int price) 
    {        
            try 
            {   
                
                
                if(conn!=null)
                    try 
                    {
                        conn.close();
                    }
                    catch (SQLException ex) {
                     System.out.println("Exception in MainUIController updateSoldBookTable\n"+ex);
                    }
                if(!isAvail(bid,price))
                {
                conn=handler.createConnection();
                stmts=conn.createStatement();
                
                rs=stmts.executeQuery("select max(No) from PaymentCounter");                
                System.out.println("paymentcounter="+rs.getInt(1));
                //stmts.close();
                stmt=conn.prepareStatement("insert into SoldBook values(?,?,?,?,?,?,?,?)");
                stmt.setInt(1,rs.getInt(1)+1);
                stmt.setString(2,bid);
                stmt.setString(3,btitle);
                stmt.setString(4,bauthor);
                stmt.setInt(5,cid);
                stmt.setString(6,DateTimeFormatter.ofPattern("dd/MM/yy  hh:mm").format(LocalDateTime.now()));                                
                Current_Date=DateTimeFormatter.ofPattern("dd/MM/yy  hh:mm").format(LocalDateTime.now());                
                stmt.setInt(7,1);
                stmt.setInt(8,price);
                System.out.println("updated row in Soldbook table= "+stmt.executeUpdate());
                System.out.println("paymentCounterTable value="+rs.getInt(1));
                
                updatePaymentCounter(rs.getInt(1)+1);
                
                if(stmt.executeUpdate()>0) checkPayment=true;           
                }
            }
            catch (SQLException ex) 
            {
                System.out.println("Exception in MainUIController updateBookTable() :\n"+ex);
            }
             finally
            {
                if(conn!=null)
                    try 
                    {
                        conn.close();
                    }
                    catch (SQLException ex) {
                     System.out.println("Exception in MainUIController updateSoldBookTable\n"+ex);
                    }
            }
            System.out.println("in soldbooktable show():\n");
            show();
    }
    
    
    private int getSoldBookQuantity(String bkid) 
    {
        try 
        {            
            conn=handler.createConnection();            
            stmt=conn.prepareStatement("select quantity from SoldBook where bid=?");
            stmt.setString(1,bkid);
            rs=stmt.executeQuery();    
            if(rs!=null)
                return rs.getInt(1);
        }
        catch (SQLException ex) 
        {                       
            System.out.println("Exception in MainUIController getSoldBookQuantity()\n"+ex);            
        }
        finally
        {
            try 
            {
                conn.close();                
                stmt.close();
                System.out.println("stmt conn closed !");            
            }
            catch (SQLException ex) 
            {
                System.out.println("Connection not closed in MainUIController getBookQuantity()\n"+ex);
            }
        }
        return 0;
    }    

    
     public boolean isAvail(String ID,int price)
    {
        try 
        {                                
                conn=handler.createConnection();
                stmt=conn.prepareStatement("select no from soldbook where bid=?");
                stmt.setString(1,ID);
                rs=stmt.executeQuery();
                if(rs.getInt(1)>0)
                {
                    stmt=conn.prepareStatement("upadate soldbook set Quantity=?,price=? where bid=?");
                    stmt.setInt(1,(getSoldBookQuantity(ID)+1));                
                    stmt.setInt(1,(getSoldBookQuantity(ID)+1)*price);
                    stmt.setString(3,ID);
                    System.out.println("SoldBook updated ="+stmt.executeUpdate());
                }                                    
        }
        catch (SQLException ex) 
        {        
            System.out.println("Exception in MainUIController isAvail()\n"+ex);            
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
                System.out.println("connection not closed in MainUIController isAvail() :\n"+ex);
            }
        }
        return false;     
    }
    
    
    
    
    private void show()
    {
        try {
            
             if(conn!=null)
                    try 
                    {
                        conn.close();
                    }
                    catch (SQLException ex) {
                     System.out.println("Exception in MainUIController updateSoldBookTable\n"+ex);
                    }
            
            conn=handler.createConnection();
            stmts=conn.createStatement();                
            rs=stmts.executeQuery("select * from SoldBook");
            while(rs.next())
            {
                System.out.print(rs.getInt(1)+" ");   
                System.out.print(rs.getString(2)+" ");   
                System.out.print(rs.getString(3)+" ");   
                System.out.print(rs.getString(4)+" ");   
                System.out.print(rs.getString(5)+" ");   
                System.out.print(rs.getString(6)+" ");   
                System.out.println(rs.getInt(7));                  
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
                
         finally
            {
                if(conn!=null)
                    try 
                    {
                        conn.close();
                    }
                    catch (SQLException ex) {
                     System.out.println("Exception in MainUIController updateSoldBookTable\n"+ex);
                    }
            }
    }
    
    private void updatePaymentCounter(int n) 
    {
        try 
        {          
            
            if(conn!=null)
                    try 
                    {
                        conn.close();
                    }
                    catch (SQLException ex) {
                     System.out.println("Exception in MainUIController updateSoldBookTable\n"+ex);
                    }
            conn=handler.createConnection();
            stmt=conn.prepareStatement("insert into PaymentCounter values(?)");
            stmt.setInt(1,n);            
            System.out.println("paymentCounterTable updated");
        }
        catch (SQLException ex) 
        {
            System.out.println("Exception in MainUIController updatePaymentCounter()\n"+ex);
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
                System.out.println("connction not closed in MainUIController updatePaymentCounter()\n"+ex);
                }
        }
    }
    
    
    private void updateBookTable(String book_id)
    {
            try                                 
            {   
                
                if(conn!=null)
                    try 
                    {
                        conn.close();
                    }
                    catch (SQLException ex) {
                     System.out.println("Exception in MainUIController updateSoldBookTable\n"+ex);
                    }
                conn=handler.createConnection();
                stmt=conn.prepareStatement("select avail from book where id=?");
                stmt.setString(1,book_id);
                bookAvail=rs.getInt(1);
                System.out.println("book Available : "+bookAvail);
                if(bookAvail>=0){
                    
                conn=handler.createConnection();
                stmt=conn.prepareStatement("update book set Avail=? where ID=?");
                stmt.setInt(1,bookAvail-1);
                stmt.setString(2,book_id);
                System.out.println(stmt.executeUpdate()+" row updated...");
                checkPayment=true;
                }
            }
            catch (SQLException ex) 
            {
                System.out.println("connection not closed in MainUIController updateBookTable() :\n"+ex);
            }
             finally
            {
                if(conn!=null)
                    try 
                    {
                        conn.close();
                    }
                    catch (SQLException ex) {
                     System.out.println("Exception in MainUIController updateSoldBookTable\n"+ex);
                    }            
            }    
    }
    
   @FXML
    void anchorMouseClicked(MouseEvent event) 
    {
                    if(drawer.isOpened() && hammer_clicked)
                    {
                            transition.setRate(-1);
                            transition.play();                                                                    
                            drawer.close();                    
                    }
    }

    @FXML
    private void addBook(ActionEvent event) 
    {
        loadAddBook();
    }
    
    public void loadAddBook()
    {
        try 
        {        
            Parent root = FXMLLoader.load(getClass().getResource("/bookstore/addbooks/AddBook.fxml"));        
            Scene scene = new Scene(root,800,600);        
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Add Book");
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
        loadAddCustomer();
    }
    
    public void loadAddCustomer()
    {        
        try 
        {
            Parent root=FXMLLoader.load(getClass().getResource("/bookstore/addCustomer/CustAdd.fxml"));
            Scene scene=new Scene(root);
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Add Customer");
            stage.getIcons().add(new Image("bookstore/icons/add.png"));
            stage.show();
        } catch (IOException ex) {
            System.out.println("Exception in MainUIController addCustomer()\n"+ex);
        }
        return;
    }

    @FXML
    private void searchBooks(ActionEvent event) 
    {
        loadSearchBook();
    }
    
    public void loadSearchBook()
    {
        try {
            Parent root=FXMLLoader.load(getClass().getResource("/bookstore/MainUI/SearchBook.fxml"));
            Scene scene=new Scene(root);
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Search Book");
            stage.setOnCloseRequest((e->{RemoveQRCode.emptyFolder(new File("src\\bookstore\\QRCodes\\search_images"));}));
            stage.getIcons().add(new Image("bookstore/icons/analytics.png"));
            stage.show();
        } catch (IOException ex) {
            System.out.println("Exception in MainUIController searchBooks()\n"+ex);
        }
        return;        
    }
    @FXML
    private void searchCustomer(ActionEvent event) 
    {
        loadSearchCustomer();
    }
    
    public void loadSearchCustomer()
    {
        try {
            Parent root=FXMLLoader.load(getClass().getResource("/bookstore/MainUI/searchCustomer.fxml"));
            Scene scene=new Scene(root);
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Search Customer");
            stage.getIcons().add(new Image("bookstore/icons/kyc.png"));
            stage.show();
        } catch (IOException ex) {
            System.out.println("Exception in MainUIController searchCustomer()\n"+ex);
        }
        return;    
    }
    
    @FXML
    private void soldBooks(ActionEvent event) 
    {
        loadSoldBoolks();
    }
    public void loadSoldBoolks()
    {
        try {
            Parent root=FXMLLoader.load(getClass().getResource("/bookstore/MainUI/SoldBookTable.fxml"));
            Scene scene=new Scene(root);
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Sold Book Table");
            stage.getIcons().add(new Image("bookstore/icons/sold.png"));
            stage.show();
             } catch (IOException ex) {
            System.out.println("Exception in MainUIController searchCustomer()\n"+ex);
        }
        return;

    }

    @FXML
    private void quickSearch(ActionEvent event) 
    {
        if(searchBox.getText().equalsIgnoreCase("Add Book"))
            loadAddBook();
        else if(searchBox.getText().equalsIgnoreCase("Add Customer"))
            loadAddCustomer();
        else if(searchBox.getText().equalsIgnoreCase("Search Book"))
            loadSearchBook();
        else if(searchBox.getText().equalsIgnoreCase("Search Customer"))
            loadSearchCustomer();
        else if(searchBox.getText().equalsIgnoreCase("Sold Book"))
            loadSoldBoolks();
        else if(searchBox.getText().equalsIgnoreCase("Menu"))
            hamburgerLoader();
        else if(searchBox.getText().equalsIgnoreCase("File"))
            new VBoxMenuController().fileBtnEvent(new ActionEvent());
        else if(searchBox.getText().equalsIgnoreCase("Add"))
            new VBoxMenuController().addBkCustEvent(new ActionEvent());
        else if(searchBox.getText().equalsIgnoreCase("View"))
            new VBoxMenuController().viewEvent(new ActionEvent());
        else if(searchBox.getText().equalsIgnoreCase("Help"))
            new VBoxMenuController().helpEvent(new ActionEvent());
   
   
    }

    @FXML
    private void closeMainUI(ActionEvent event) 
    {
        System.exit(0);
    }
/*
                           stmt.execute("create table "+TABLE_NAME+"("                                     
                        +"BTitle char(20) not null,"
                        +"Author char(20) not null,"                                                
                        +"Quantity int(2)not null,"
                        +"Price int(4)not null"
                        +")");
                

           */
    private void updateGenerateBill(String bookId,String title, String author, int price) 
    {
        
        if(conn!=null) try {
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Before Connection closed !"+ex);
        }
        try 
        {                                    
            int quantity=getBookQuantity(bookId);              
            if(quantity==0)
            {
                System.out.println("Quantity temp==-1:"+quantity);
                conn=handler.createConnection();
                if(conn!=null) System.out.println("Connection estabiished");
                stmt=conn.prepareStatement("insert into GenerateBill values(?,?,?,?,?)");
                stmt.setString(1,bookId);
                stmt.setString(2,title);                            
                stmt.setString(3,author);
                stmt.setInt(4,1);
                stmt.setInt(5,price);
                System.out.println("inserted into generateBill table :"+stmt.executeUpdate());
                stmt.close();                                   
            }
            else if(quantity>0) 
            {   
                System.out.println("Quantity temp>0:"+quantity);
                conn=handler.createConnection();
        //        if(conn!=null) System.out.println("Connection estabiished");                
                stmt=conn.prepareStatement("update GenerateBill set quantity=?,price=? where id=?");
                stmt.setInt(1,(quantity+1));                                                         
                stmt.setInt(2,((quantity+1)*price));
                stmt.setString(3,bookId);
                System.out.println("inserted into generateBill table :"+stmt.executeUpdate());
                stmt.close();                            
            }                
        }
        catch (SQLException ex) 
        {
            System.out.println("Excepton in MainUIController updateGenerateBill()\n"+ex);
        }
        finally
        {
            if(conn!=null)
            try
            {
                conn.close ();
            } catch (SQLException ex) {
                 System.out.println("Connection not closed in MainUIController updateGenerateBill()\n"+ex);
            }
        }
    }

    private int getBookQuantity(String bkid) 
    {
        try 
        {            
            conn=handler.createConnection();            
            stmt=conn.prepareStatement("select quantity from GenerateBill where id=?");
            stmt.setString(1,bkid);
            rs=stmt.executeQuery();    
            if(rs!=null)
                return rs.getInt(1);
        }
        catch (SQLException ex) 
        {                       
            System.out.println("Exception in MainUIController getBookQuantity()\n"+ex);            
        }
        finally
        {
            try 
            {
                conn.close();                
                stmt.close();
                System.out.println("stmt conn closed !");            
            }
            catch (SQLException ex) 
            {
                System.out.println("Connection not closed in MainUIController getBookQuantity()\n"+ex);
            }
        }
        return 0;
    }    

    public boolean emptyGenerateBill() 
    {
        try 
        {     
            handler=DatabaseHandler.getInstance();
            conn=handler.createConnection();
            stmt=conn.prepareStatement("delete from GenerateBill where 1=1");            
            return stmt.executeUpdate()>0;
        }
        catch (SQLException ex) 
        {
            System.out.println("Exception in MainUIController emptyGenerateBill()\n"+ex);
        }
        finally
        {
            try 
            {
                conn.close();
            }
            catch (SQLException ex) 
            {
                System.out.println("Connection not closed in MainUIController emptyGenerateBill()\n"+ex);
            }
        }
        return false;
    }
    @FXML
    private void billGenerate(ActionEvent event) 
    {        
        try 
        {            
            conn=handler.createConnection();
            stmt=conn.prepareStatement("select count(id) from GenerateBill");                         
                rs=stmt.executeQuery();
            int count=rs.getInt(1);
            System.out.println("IDs :"+count);
            
            stmt=conn.prepareStatement("select * from GenerateBill");                         
            rs=stmt.executeQuery();
            
            String id[]=new String[count];
            String title[]=new String[count];
            String author[]=new String[count];
            int quantity[]=new int[count];
            int price[]=new int[count];
            
            System.out.println("In Generate Bill:");
            int i=0;
            while(rs.next())
            {                
               id[i]=rs.getString(1);                
               title[i]=rs.getString(2);                
               author[i]=rs.getString(3);                
               quantity[i]=rs.getInt(4);                
               price[i]=rs.getInt(5);                
               ++i;              
               System.out.print(rs.getString(1)+" ");
               System.out.print(rs.getString(2)+" ");
               System.out.print(rs.getString(3)+" ");
               System.out.print(rs.getInt(4)+" ");
               System.out.println(rs.getInt(5));
            }
            new Bill_Generator().generateBill(c_name,Integer.parseInt(custid.getText()),id, title, author, quantity, price);
            //emptyGenerateBill();
        }
        catch (SQLException ex) 
        {
            System.out.println("Exception in MainUIController getBookQuantity()\n"+ex);
        }
        finally
        {
            try 
            {
                conn.close();
            }
            catch (SQLException ex) 
            {
                System.out.println("Connection not closed in MainUIController getBookQuantity()\n"+ex);
            }
        }        
        if(emptyGenerateBill())
            System.out.println("All cleared");
    }

    @FXML
    public void clearAll(ActionEvent event) 
    {
        QRImage.setImage(null);
        
        MainUIController.BookSelect=0;
        chooseBkCounter.setText(Integer.toString(MainUIController.BookSelect));
        
        btitle.setText("Book Title");
        bauthor.setText("Book Author");
        bpublisher.setText("Book Publisher");
        bprice.setText("Book Price");
        
        custname.setText("Customer Name");        
        custmno.setText("Customer  Mobile No");        
        custmail.setText("Customer Email ID");        
        custid.setText("");        
        
        price=0;
        totalPrice.setText("₹ Total Price /-");
        
       if(emptyGenerateBill())
            System.out.println("All Cleared");
        else
            System.out.println(" not Cleared");        
    }
}