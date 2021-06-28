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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author a
 */
public class BookPiechartController implements Initializable {

    @FXML
    private StackPane stackPane;
    
    private PieChart piechart;
    
    ObservableList<PieChart.Data> list;

    DatabaseHandler handler;
    Connection conn;
    PreparedStatement stmt;
    ResultSet rs;
    
    
    int avail=0,sold=0;
        
    static String bkid;

  /*  public BookPiechartController(String bkid) 
    {
        BookPiechartController.bkid=bkid;
        System.out.println("In BookPiecharController="+BookPiechartController.bkid);
    }
*/
    public BookPiechartController() 
    {
        handler=DatabaseHandler.getInstance();
    }
        
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {        
        piechart=new PieChart(new BookPiechartController().getChartData());
        stackPane.getChildren().add(piechart);          
    }
    
    
    private void getDataFromDB(Connection conn)
    {
        try 
        {
            conn=handler.createConnection();
            stmt=conn.prepareStatement("select * from book where id=?");
            stmt.setString(1, bkid);
            rs=stmt.executeQuery();            
            avail=rs.getInt(7);   
            System.out.println(rs.getString(1));   
            System.out.println(rs.getString(2));   
            System.out.println(rs.getString(3));   
            System.out.println(rs.getString(4));   
            System.out.println(rs.getString(5));   
            System.out.println(rs.getInt(6));   
            System.out.println(rs.getInt(7));   
            System.out.println("in getDataFromDB()="+avail);
            stmt.close();
            stmt=conn.prepareStatement("select count(*) from SoldBookQRID where bid=?");
            stmt.setString(1,bkid);
            rs=stmt.executeQuery();            
            sold=rs.getInt(1);
        }
        catch (SQLException ex) 
        {
            System.out.println("Exception in BookPiechartController getChartFromDB()\n"+ex);
        }
         finally
        {
               if(conn!=null)
                try {
                    conn.close();                        
                    } 
                catch (SQLException ex) 
                {
                    System.out.println("Exception in finally BookPieChartController \n"+ex);
                }
                    
        }
    }
    
    ObservableList<PieChart.Data> getChartData() 
    {        
            conn=handler.createConnection();
            
            list = FXCollections.observableArrayList();                       
            
            getDataFromDB(conn);          
            
             System.out.println("Piechart\navail="+avail);
            System.out.println("sold="+sold);
            
            list.addAll( new PieChart.Data("Available Books",avail),new PieChart.Data("Sold Books", sold) );                
            
            return list;       
    }     
}
