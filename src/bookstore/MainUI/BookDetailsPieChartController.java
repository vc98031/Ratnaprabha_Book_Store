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
public class BookDetailsPieChartController implements Initializable {

    @FXML
    private StackPane stackPane;

    
    private PieChart piechart;
    
   static ObservableList<PieChart.Data> list;

    DatabaseHandler handler;
    Connection conn;
    PreparedStatement stmt;
    ResultSet rs;
    
    
    static String bookname="",quantity="";
             
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        // TODO
        handler=DatabaseHandler.getInstance();
        getDataFromDB();
        piechart=new PieChart(new BookDetailsPieChartController().getPieChartData());
        stackPane.getChildren().add(piechart);          
    }  
    ObservableList<PieChart.Data> getPieChartData()
    {
        list=FXCollections.observableArrayList();
        //getDataFromDB();
       // System.out.println(bookname+"::"+quantity);
        System.out.println(bookname+"::"+quantity);
        String bname[]=bookname.split(",");
        String quant[]=quantity.split(",");
        
        for(int i=0;i<bname.length;i++)
            System.out.println(bname[i]+"::"+quant[i]);
        
        
        for(int i=0;i<bname.length;i++)
            list.add(new PieChart.Data(bname[i],Integer.parseInt(quant[i])));
        return list;
    }
    public void getDataFromDB()
    {
        try 
        {
            conn=handler.createConnection();
            stmt=conn.prepareStatement("select  bid,quantity from soldbook");
            rs=stmt.executeQuery();            
            while(rs.next())
            {
                bookname=rs.getString(1)+",";
                quantity=Integer.toString(rs.getInt(2))+",";
                System.out.println(rs.getString(1)+"::"+rs.getInt(2));                
               // list.add(new PieChart.Data(rs.getString(1),rs.getInt(2)));
            }
            System.out.println(bookname+"::"+quantity);
        }
        catch (SQLException ex) 
        {
            System.out.println("Exception in BookDetailsPieChartController\n"+ex);
        }
        finally
        {
               if(conn!=null)
                try {
                    conn.close();                
                
            } 
                catch (SQLException ex) 
                {
             System.out.println("Exception in finally BookDetailPieChartController\n"+ex);
            }                    
        }   
    }
}
