/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstore.MainUI;

import bookstore.QRCodes.Generator;
import bookstore.database.DatabaseHandler;
import com.jfoenix.controls.JFXButton;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Jayprasad
 */
public class SearchBookController implements Initializable {
    @FXML
    private TextField searchBook;
    @FXML
    private ImageView searchBookBtn;
    @FXML
    private ImageView QRImage;
    @FXML
    private Text btitle;
    @FXML
    private Text bauthor;
    @FXML
    private Text bpublisher;
    @FXML
    private Text bprice;

    
    Notifications notify;
    
    DatabaseHandler handler;
    Connection conn;
    Statement stmt;
    ResultSet rs;
       
    
    @FXML
    private JFXButton searchBtn;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button pieChartBtn;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        notify=Notifications.create();        
        try 
        {
            handler=DatabaseHandler.getInstance();
            conn=handler.createConnection();
            stmt=conn.createStatement();
            rs=stmt.executeQuery("select count(id) from book");
            int column=rs.getInt(1);
            int i=0;
            
            String [] ID=new String[column];                        
            
            rs=stmt.executeQuery("select ID from book");                  
                while(rs.next()&& i<column)
                {
                    ID[i]=rs.getString(1);
                    ++i;
                }           
                
                TextFields.bindAutoCompletion(searchBook,ID);
        } 
        catch (SQLException ex) 
        {
            System.out.println("Exception in SearchBookController initialize()\n"+ex);            
        }
        finally
        {
            if(conn!=null)
                try {
                    conn.close();
            } catch (SQLException ex) {
                System.out.println("Exception in connection not closed SearchBookController initialize()\n"+ex);            
            }
        }
    }    
        

    @FXML
    private void searchBooks(ActionEvent event)
    {
        try 
        {
            handler=DatabaseHandler.getInstance();
            conn=handler.createConnection();
            stmt=conn.createStatement();
            rs=stmt.executeQuery("select Title,Author,Publisher,Price from Book where id='"+searchBook.getText()+"'");            
            btitle.setText(rs.getString(1));
            bauthor.setText(rs.getString(2));
            bpublisher.setText(rs.getString(3));
            bprice.setText(Integer.toString(rs.getInt(4)));
            
            String text="Book ID :'"+searchBook.getText()+"' ,Title : '"+rs.getString(1)+"' , Author :'"+rs.getString(2)+"' , Publisher : '"+rs.getString(3)+"' , Price : "+Integer.toString(rs.getInt(4));
            Generator.generateQRCodeImage(text,new File("src\\bookstore\\QRCodes\\search_images\\"+"SearchBook_'"+searchBook.getText()+"'.png"));                    
            QRImage.setImage(new Image(new FileInputStream("src\\bookstore\\QRCodes\\search_images\\"+"SearchBook_'"+searchBook.getText()+"'.png")));
                    notify.title("Search Completed !");                                        
                    notify.hideAfter(Duration.seconds(2));                
                    notify.position(Pos.CENTER);  
                    notify.darkStyle();
                    notify.showInformation();
                    return;              
        }
        catch (SQLException  ex) 
        {
                    notify.title("Book Not Found !");                                        
                    notify.hideAfter(Duration.seconds(2));                
                    notify.position(Pos.CENTER);  
                    notify.darkStyle();
                    notify.showError();
                    
            System.out.println("Exception in SearchBooksController searchBooks()\n"+ex);            
        }
        catch (FileNotFoundException ex) 
        {
                    notify.title("QR Not Generated !");                                        
                    notify.hideAfter(Duration.seconds(2));                
                    notify.position(Pos.CENTER);  
                    notify.darkStyle();
                    notify.showError();
                    
            System.out.println("Exception in SearchBooksController searchBooks()\n"+ex);            
        }
        finally
        {
            if(conn!=null)
                try {
                    conn.close();
            } catch (SQLException ex) {
                System.out.println("Exception in connection not closed SearchBooksController searchBooks()\n"+ex);            
            }
        }
    } 

    @FXML
    private void showPieChart(ActionEvent event) 
    {
        //new BookPiechartController(searchBook.getText());
        BookPiechartController.bkid=searchBook.getText();
        try 
        {        
            Parent root = FXMLLoader.load(getClass().getResource("/bookstore/MainUI/BookPiechart.fxml"));        
            Scene scene = new Scene(root,400,400);   
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.getIcons().add(new Image("/bookstore/icons/book.png"));        
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
  
}
