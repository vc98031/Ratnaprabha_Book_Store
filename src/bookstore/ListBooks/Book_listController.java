package bookstore.ListBooks;

import bookstore.MainUI.SoldBookTableController;
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
import javafx.beans.property.*;
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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class Book_listController implements Initializable 
{
    
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Book> tableview;
    @FXML
    private TableColumn<Book,String> TitleCol;
    @FXML
    private TableColumn<Book,String> BookIDCol;
    @FXML
    private TableColumn<Book,String> AuthorCol;
    @FXML
    private TableColumn<Book,String> PublisherCol;
    @FXML
    private TableColumn<Book,String> PriceCol;
    @FXML
    private TableColumn<Book,Integer> AvailableCol;
    
    ObservableList <Book> list =FXCollections.observableArrayList();
    DatabaseHandler handler;
    Connection conn;
    Statement stmt;
    ResultSet rs;
     
    Notifications notify; 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {        
        notify=Notifications.create();
        initCol();
        loadData();
    }        

    private void initCol() 
    {
        TitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        BookIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        AuthorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        PublisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        PriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        AvailableCol.setCellValueFactory(new PropertyValueFactory<>("available"));               
    }

    private void loadData() 
    {
        try 
        {    
            list.clear();
            handler=DatabaseHandler.getInstance();
            conn=handler.createConnection();        
            
            stmt=conn.createStatement();
            rs=stmt.executeQuery("Select * from book");
            
            while(rs.next())
            {   
                String ID=rs.getString(1);
                String Title=rs.getString(2);
                String Author=rs.getString(3);
                String Publisher=rs.getString(4);
                Integer Price=rs.getInt(6);
                Integer Available=rs.getInt(7);
                                                
                list.add(new Book(Title,ID,Author,Publisher,Price,Available));
            }            
        } 
        catch (SQLException ex) 
        {
            System.out.println("Exception in Book_listController loadData()\n"+ex);
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

    @FXML
    private void editFromBook(ActionEvent event) 
    {
        Book editOption=tableview.getSelectionModel().getSelectedItem();
        if(editOption==null)
        {
            notify.title("Please Select Book For Deletion.");                    
            notify.hideAfter(Duration.seconds(2));                
            notify.position(Pos.CENTER);                
            notify.darkStyle();
            notify.showError(); 
            return;
        }
        try 
        {        
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/bookstore/addbooks/AddBook.fxml"));
            Parent root = loader.load();     
            AddBookController controller=(AddBookController)loader.getController();
            controller.inflatUI(editOption);
            Scene scene = new Scene(root,800,600);        
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.setTitle("Edit Book "+editOption.getId());
            stage.setResizable(false);
            stage.setOnCloseRequest((e)->{refresh(new ActionEvent());});
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
    private void deleteFromBook(ActionEvent event) 
    {
        Book deleteOption=tableview.getSelectionModel().getSelectedItem();
        if(deleteOption==null)
        {
            notify.title(" Please Select Book For Deletion.");                    
            notify.hideAfter(Duration.seconds(2));                
            notify.position(Pos.CENTER);                
            notify.darkStyle();
            notify.showError(); 
            return;
        }
        Alert alert =new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleting Book");
        alert.setContentText("Are You Want to Deleting The Book "+deleteOption.getId()+" ?");
        Optional<ButtonType> answer=alert.showAndWait();
        if(answer.get()==ButtonType.OK)
        {
            if(DatabaseHandler.getInstance().deleteBookTable(deleteOption))
                list.remove(deleteOption);
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

    public static class Book
    {
        private final SimpleStringProperty title;        
        private final SimpleStringProperty id;        
        private final SimpleStringProperty author;        
        private final SimpleStringProperty publisher;        
        private final SimpleIntegerProperty price;        
        private final SimpleIntegerProperty available;
        
        public Book(String title,String id,String author,String publisher,Integer price,Integer available)
        {
            this.title=new SimpleStringProperty(title);
            this.id=new SimpleStringProperty(id);
            this.author=new SimpleStringProperty(author);
            this.publisher=new SimpleStringProperty(publisher);
            this.price=new SimpleIntegerProperty(price);
            this.available=new SimpleIntegerProperty(available);
        }       
        
        public String getTitle() {
            return title.get();
        }
        
        public String getId() {
            return id.get();
        }

        public String getAuthor() {
            return author.get();
        }

        public String getPublisher() {
            return publisher.get();
        }

        public Integer  getPrice() {
            return price.get();
        }

        public Integer getAvailable() {
            return available.get();
        }
       
    }
    
}
