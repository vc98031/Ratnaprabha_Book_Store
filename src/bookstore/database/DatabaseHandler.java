package bookstore.database;

import bookstore.ListBooks.Book_listController.Book;
import bookstore.ListCustomer.CustomerListController.Customer;
import bookstore.MainUI.SoldBookTableController.SoldBook;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final  class DatabaseHandler
{
    Connection conn;
    Statement stmt; 
    PreparedStatement stmts;
    ResultSet rs;
    //String TABLE_NAME;
    
    static DatabaseHandler handler;
    
    private DatabaseHandler()
    {
        conn=createConnection();        
        setBookTable(conn);
        conn=createConnection();        
        setupCustTable(conn);        
        conn=createConnection();                
        setLoginTable(conn);        
        conn=createConnection();        
        setSoldBooksTable(conn);
        conn=createConnection();        
        setPaymentCounterTable(conn);
        conn=createConnection();        
        setSoldBooksQRIDTable(conn);
        conn=createConnection();
        setGenerateBill(conn);
    }
    
    public static DatabaseHandler getInstance()
    {
        if(handler==null)
            handler=new DatabaseHandler();
        return handler;
    }
    
    public Connection createConnection()
    {
        try 
        {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:src\\bookstore\\database\\RPBookStore.db");
        } 
        catch (ClassNotFoundException | SQLException ex) 
        {
            System.out.println("Exception occured in DatabaseHandler class - createConnection()\n"+ex);
        }
        return null;
    }
    
    
    public void setBookTable(Connection conn)
    {        
        try 
        {
            String TABLE_NAME="BOOK";           
            stmt=conn.createStatement();            
            DatabaseMetaData data=conn.getMetaData();
            ResultSet tables=data.getTables(null,null,TABLE_NAME,null);
            if(tables.next())            
                System.out.println(TABLE_NAME+" table already exist ,READY FOR GO!");                          
            else
            {
                stmt.execute("create table "+TABLE_NAME+"("
                        +"ID char(10) primary key,"                 //1
                        +"Title char(20) not null,"                 //2
                        +"Author char(20),"                         //3
                        +"Publisher char(20) not null,"             //4
                        +"Date char(50)not null,"                   //5
                        +"Price int(4)not null,"                    //6                         
                        +"Avail int(4)not null"                     //7
                        +")");
                
                System.out.println(TABLE_NAME+" table created successfully!");
            }
        }
        catch (SQLException ex) 
        {
            System.out.println("Exception ocurred in DatabaseHandler class -  setBookTable\n"+ex);
        }
        finally
        {
            if(conn!=null)
                try {
                    conn.close();
            } catch (SQLException ex) {
             System.out.println("Exception in DatabaseHadler setBookTable()\n"+ex);
            }
        }
    }
    public  boolean updateBook(Book books)
    {
         try
        {
            handler=DatabaseHandler.getInstance();
            conn=handler.createConnection();
            PreparedStatement stmt=conn.prepareStatement("upadate book set title=?,author=?,publisher=?,price=? where id=?");
            stmt.setString(1,books.getTitle());
            stmt.setString(2,books.getAuthor());
            stmt.setString(3,books.getPublisher());
            stmt.setInt(4,books.getPrice());            
            stmt.setString(5,books.getId());            
            if(stmts.executeUpdate()==1)
                return true;
        }
        catch (SQLException ex) 
        {
            System.out.println("Exception in DatabaseHandler updateBookTable()\n"+ex);
        }
        finally
        {
                    if(conn!=null)
                            try {
                                conn.close();
                    } catch (SQLException ex) {
                        System.out.println("DatabaseHandler updateBookTable()\n"+ex);
                    }
        }
        return false;
    }
    public boolean deleteBookTable(Book books)
    {        
        try 
        {   
            handler=DatabaseHandler.getInstance();
            conn=handler.createConnection();
            stmts=conn.prepareStatement("delete from book where id=?");
            stmts.setString(1,books.getId());
            System.out.println("row deleted....\n");
            if(stmts.executeUpdate()==1)
                return true;
        }
        catch (SQLException ex) 
        {
            System.out.println("Exception in DatabaseHandler deleteBook()\n"+ex);
        }
        finally
        {
            if(conn!=null)
                try {
                    conn.close();
            } catch (SQLException ex) {
                System.out.println("Connection not closed in DatabaseHandler deleteBook()\n"+ex);
            }
        }
        return false;
    }
    
    
    public void setupCustTable(Connection conn)
    {        
        try 
        {
            String TABLE_NAME="Customer";           
            stmt=conn.createStatement();            
            DatabaseMetaData data=conn.getMetaData();
            ResultSet tables=data.getTables(null,null,TABLE_NAME,null);
            if(tables.next())            
                System.out.println(TABLE_NAME+" table already exist ,READY FOR GO!");                          
            else
            {
                stmt.execute("create table "+TABLE_NAME+"("
                        +"Cust_ID int(5) primary key,"
                        +"Cust_Name char(20) not null,"                                                
                        +"Cust_Mbno char(12)not null,"
                        +"Cust_Email char(20)"
                        +")");
                
                System.out.println(TABLE_NAME+" table created successfully!");                
            }
        }
        catch (SQLException ex) 
        {
            System.out.println("Exception ocurred in DatabaseHandler class -  setBookTable\n"+ex);
        }
        finally
        {
            if(conn!=null)
                try {
                    conn.close();
            } catch (SQLException ex) {
             System.out.println("Exception in DatabaseHadler setupCustTable()\n"+ex);
            }
        }    
    }
    
    public  boolean updateCustomer(Customer cust)
    {
         try
        {
            handler=DatabaseHandler.getInstance();
            conn=handler.createConnection();
            PreparedStatement stmt=conn.prepareStatement("upadate Customer set Cust_name=?,Cust_Mbno=?,Cust_Email=? where Cust_id=?");
            stmt.setString(1,cust.getCname());
            stmt.setString(2,cust.getCmbno());
            stmt.setString(3,cust.getCmail());
            stmt.setInt(4,cust.getCid());                        
            if(stmts.executeUpdate()==1)
                return true;
        }
        catch (SQLException ex) 
        {
            System.out.println("Exception in DatabaseHandler updateCustomer()\n"+ex);
        }
        finally
        {
                    if(conn!=null)
                            try {
                                conn.close();
                    } catch (SQLException ex) {
                        System.out.println("Connection not closed in DatabaseHandler updateCustomer()\n"+ex);
                    }
        }
        return false;
    }
    
    
    public boolean deleteCustomer(Customer cust)
    {        
        try 
        {   
            handler=DatabaseHandler.getInstance();
            conn=handler.createConnection();
            stmts=conn.prepareStatement("delete from Customer where Cust_id=?");
            stmts.setInt(1,cust.getCid());
            System.out.println("row deleted....\n");
            if(stmts.executeUpdate()==1)
                return true;
        }
        catch (SQLException ex) 
        {
            System.out.println("Exception in DatabaseHandler deleteCustomer()\n"+ex);
        }
        finally
        {
            if(conn!=null)
                try {
                    conn.close();
            } catch (SQLException ex) {
                System.out.println("Connection not closed in DatabaseHandler deleteCustomer()\n"+ex);
            }
        }
        return false;
    }
    
    
    public void setLoginTable(Connection conn)
    {        
        try 
        {
            String TABLE_NAME="Login";                       
            stmt=conn.createStatement();            
            DatabaseMetaData data=conn.getMetaData();
            ResultSet tables=data.getTables(null,null,TABLE_NAME,null);
            
            if(tables.next())            
                System.out.println(TABLE_NAME+" table already exist ,READY FOR GO!");                          
            else
            {
                                
                stmt.execute("create table "+TABLE_NAME+"("
                        +"No int(2) primary key,"
                        +"username char(10) not null,"
                        +"password char(10) not null"
                        +")");                                                                                                         
                
                System.out.println(TABLE_NAME+" table created successfully!");
                stmt.executeUpdate("insert into Login(No,username,password) values(1,'admin','admin')");                
                stmt.executeUpdate("insert into Login(No,username,password) values(2,'system','password')");                
                stmt.execute("commit");
            }
        }
        catch (SQLException ex) 
        {
            System.out.println("Exception ocurred in DatabaseHandler class -  setLoginTable\n"+ex);
        }

        finally
        {
            if(conn!=null)
                try 
            {
                    conn.close();
                 //conn=handler.createConnection();                   
                
            }
                catch (SQLException ex) {
             System.out.println("Exception in DatabaseHadler setLoginTable()\n"+ex);
            }
                    
        }
    }
    
    public void setSoldBooksTable(Connection conn)
    {        
        try 
        {
            String TABLE_NAME="SoldBook";           
            stmt=conn.createStatement();            
            DatabaseMetaData data=conn.getMetaData();
            ResultSet tables=data.getTables(null,null,TABLE_NAME,null);
            if(tables.next())            
                System.out.println(TABLE_NAME+" table already exist ,READY FOR GO!");                          
            else
            {
                stmt.execute("create table "+TABLE_NAME+"("
                        +"No int(7) primary key,"
                        +"BID char(10) not null,"
                        +"BTitle char(20) not null,"
                        +"Author char(20),"
                        +"Customer int(5) not null,"
                        +"Date char(50)not null," 
                        +"Quantity int(4)not null,"
                        +"Price int(4)not null"
                        +")");
                
                System.out.println(TABLE_NAME+" table created successfully!");
            }
        }
        catch (SQLException ex) 
        {
            System.out.println("Exception ocurred in DatabaseHandler class -  setSoldBookTable\n"+ex);
        }

        finally
        {
               if(conn!=null)
                try {
                    conn.close();
                //    conn=handler.createConnection();
                
            } 
                catch (SQLException ex) 
                {
             System.out.println("Exception in finally DatabaseHadler setSoldBookTable()\n"+ex);
            }
                    
        }
    }
    
    public boolean deleteSoldBook(SoldBook books)
    {        
        try 
        {   
            handler=DatabaseHandler.getInstance();
            conn=handler.createConnection();
            stmts=conn.prepareStatement("delete from soldbook where no=?");
            stmts.setInt(1,books.getNo());
            if(stmts.executeUpdate()==1)
                return true;
        }
        catch (SQLException ex) 
        {
            System.out.println("Exception in DatabaseHandler deleteSoldBook()\n"+ex);
        }
        finally
        {
            if(conn!=null)
                try {
                    conn.close();
                    deletePaymentCounterTable();
            } catch (SQLException ex) {
                System.out.println("Connection not closed in DatabaseHandler deleteSoldBook()\n"+ex);
            }
        }
        return false;
    }
    
    public  void deletePaymentCounterTable()
    {
        try 
        {   
            handler=DatabaseHandler.getInstance();
            conn=handler.createConnection();
            stmts=conn.prepareStatement("select * from PaymentCounter");
            rs=stmts.executeQuery();
            if(rs.getInt(1)>0)
            {
                stmts.close();
                stmts=conn.prepareStatement("insert into PaymentCounter values(?)");                
                stmts.setInt(1,(rs.getInt(1)-1));
                System.out.println("PaymentCounter table -- "+stmts.executeUpdate());
            }
        }
        catch (SQLException ex) 
        {
            System.out.println("Exception in DatabaseHandler deletePaymentCounter()\n"+ex);
        }
        finally
        {
            if(conn!=null)
                try {
                    conn.close();
            } catch (SQLException ex) {
                System.out.println("Connection not closed in DatabaseHandler deletePaymentCounter()\n"+ex);
            }
        }
    }
    
    public void setSoldBooksQRIDTable(Connection conn)
    {        
        try 
        {
            String TABLE_NAME="SoldBookQRID";           
            stmt=conn.createStatement();            
            DatabaseMetaData data=conn.getMetaData();
            ResultSet tables=data.getTables(null,null,TABLE_NAME,null);
            if(tables.next())            
                System.out.println(TABLE_NAME+" table already exist ,READY FOR GO!");                          
            else
            {
                stmt.execute("create table "+TABLE_NAME+"("                        
                        +"BID char(10) not null,"
                        +"QRID int(3) not null"
                        +")");
                
                System.out.println(TABLE_NAME+" table created successfully!");
            }
        }
        catch (SQLException ex) 
        {
            System.out.println("Exception ocurred in DatabaseHandler class -  setSoldBookQRIDTable\n"+ex);
        }

        finally
        {
               if(conn!=null)
                try {
                    conn.close();
                //    conn=handler.createConnection();
                
            } 
                catch (SQLException ex) 
                {
             System.out.println("Exception in finally DatabaseHadler setSoldBookQRIDTable()\n"+ex);
            }
                    
        }
    }
    
    public void setGenerateBill(Connection conn)
    {
        try 
        {
            String TABLE_NAME="GenerateBill";           
            stmt=conn.createStatement();            
            DatabaseMetaData data=conn.getMetaData();
            ResultSet tables=data.getTables(null,null,TABLE_NAME,null);
            if(tables.next())            
                System.out.println(TABLE_NAME+" table already exist ,READY FOR GO!");                          
            else
            {
                stmt.execute("create table "+TABLE_NAME+"("                                     
                        +"id char(20) primary key,"
                        +"BTitle char(20) not null,"
                        +"Author char(20) not null,"                                                
                        +"Quantity int(2)not null,"
                        +"Price int(4)not null"
                        +")");
                
                System.out.println(TABLE_NAME+" table created successfully!");
            }
        }
        catch (SQLException ex) 
        {
            System.out.println("Exception ocurred in DatabaseHandler class -  setGenerateBill\n"+ex);
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
                 System.out.println("Exception in finally DatabaseHadler setGenerateBill()\n"+ex);
                }                    
        }
    }
    
    public void setPaymentCounterTable(Connection conn)
            
    {        
        try 
        {
            String TABLE_NAME="PaymentCounter";     
            stmt=conn.createStatement();
            DatabaseMetaData data=conn.getMetaData();
            ResultSet tables=data.getTables(null,null,TABLE_NAME,null);
            if(tables.next())            
                System.out.println(TABLE_NAME+" table already exist ,READY FOR GO!");                          
            else
            {
                
                stmt.execute("create table "+TABLE_NAME+"("
                        +"No int(2)primary key"
                        +")");
                               
                 stmts=conn.prepareStatement("insert into PaymentCounter values(?)");                            
                    stmts.setInt(1,0);
  
                System.out.println(TABLE_NAME+" table created successfully!");
            }
        }
        catch (SQLException ex) 
        {
            System.out.println("Exception ocurred in DatabaseHandler class -  setPaymentCounterTable()\n"+ex);
        }

        finally
        {
            if(conn!=null)
                try {
                    conn.close();
                   // conn=handler.createConnection();
                   
            } 
                catch (SQLException ex) 
                {
             System.out.println("Exception in DatabaseHadler setPaymentCounterTable()\n"+ex);
            }
                    
        }
    }
    
}

