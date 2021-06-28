 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstore.Invoice;

import bookstore.MainUI.MainUIController;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.javafx.font.FontFactory;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Jayprasad
 */
public class Bill_Generator 
{   
    final String store_name="RatnaPrabha Book Store";        
    Document document=new Document();
    String Cust_Name;
    int Cust_Id;
    public  void generateBill(String custname,int custId,String id[],String title[],String author[],int Quantity[],int price[])
    {
        String FileName="src\\bookstore\\Invoice\\Bills\\"+custId+"_"+"Bill_"+DateTimeFormatter.ofPattern("dd_mm_yyyy_hh_mm_ss").format(LocalDateTime.now())+".pdf";
         int sumPrice=0;
            
            for(int i=0;i<price.length;i++)                
               sumPrice+=price[i];
                       
       try 
        {                        
            PdfWriter writer=PdfWriter.getInstance(document,new FileOutputStream(FileName));
            document.open();            
            
            //Heading 
            Font font=new Font(Font.FontFamily.HELVETICA, 45, 0, BaseColor.BLUE);
            Paragraph header=new Paragraph(store_name,font);
            header.setFont(font);
            header.setAlignment(Element.ALIGN_CENTER);           
            document.add(header);
            
            document.add(new Paragraph("=========================================================================="));
            
            for(int i=0;i<1;i++)
                  document.add(Chunk.NEWLINE);           

            //Customer Name & ID
            document.add(new Paragraph("Date :  "+DateTimeFormatter.ofPattern("dd/mm/yy hh:mm").format(LocalDateTime.now())));
            document.add(Chunk.NEWLINE);       
            document.add(new Paragraph("Customer Name :  "+custname));
            document.add(Chunk.NEWLINE);       
            document.add(new Paragraph("Customer ID :  "+custId));                                    
            
            document.add(Chunk.NEWLINE);                       
            document.add(Chunk.NEWLINE);                       
            
            //Creating Table
            PdfPTable table =new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(11f);
            table.setSpacingAfter(11f);           
            
            float colWidth[]={1f,2f,4f,4f,2f,2f};
            table.setWidths(colWidth);
            
            PdfPCell cell_1=new PdfPCell(new Paragraph("No."));
            PdfPCell cell_2=new PdfPCell(new Paragraph("Book ID"));
            PdfPCell cell_3=new PdfPCell(new Paragraph("Book Name"));
            PdfPCell cell_4=new PdfPCell(new Paragraph("Author"));
            PdfPCell cell_5=new PdfPCell(new Paragraph("Quantity"));
            PdfPCell cell_6=new PdfPCell(new Paragraph("Price"));
            
            cell_1.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_3.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_4.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_5.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell_6.setHorizontalAlignment(Element.ALIGN_CENTER);
                     
            cell_1.setBackgroundColor(BaseColor.GRAY);
            cell_2.setBackgroundColor(BaseColor.GRAY);
            cell_3.setBackgroundColor(BaseColor.GRAY);
            cell_4.setBackgroundColor(BaseColor.GRAY);
            cell_5.setBackgroundColor(BaseColor.GRAY);
            cell_6.setBackgroundColor(BaseColor.GRAY);
            
            table.addCell(cell_1);
            table.addCell(cell_2);
            table.addCell(cell_3);
            table.addCell(cell_4);
            table.addCell(cell_5);
            table.addCell(cell_6);
            
            for(int i=0;i<id.length;i++)
            {
                table.addCell(Integer.toString(i+1));
                table.addCell(id[i]);
                table.addCell(title[i]);
                table.addCell(author[i]);
                table.addCell(Integer.toString(Quantity[i]));
                table.addCell(Integer.toString(price[i]));
            }            
            
            document.add(table);           
                        
            document.add(Chunk.NEWLINE);            
            
            PdfPTable TotalPrice =new PdfPTable(2);
            TotalPrice.setWidthPercentage(100);
            TotalPrice.setSpacingBefore(11f);
            TotalPrice.setSpacingAfter(11f);           
            
            float colWidth2[]={4f,4f};
            TotalPrice.setWidths(colWidth2);
            
            PdfPCell priceHead=new PdfPCell(new Paragraph("Total Price"));
            PdfPCell Price=new PdfPCell(new Paragraph("₹"+Integer.toString(sumPrice)+"/-"));
            
            priceHead.setHorizontalAlignment(Element.ALIGN_CENTER);
            Price.setHorizontalAlignment(Element.ALIGN_CENTER);
            
            priceHead.setBackgroundColor(BaseColor.LIGHT_GRAY);
            Price.setBackgroundColor(BaseColor.LIGHT_GRAY);
            
            TotalPrice.addCell(priceHead);
            TotalPrice.addCell(Price);
            
            document.add(TotalPrice);
            
            document.close();
            writer.close();            
            System.out.println("pdf created...");
            if(Desktop.isDesktopSupported())
                try {
                    Desktop.getDesktop().open(new File(FileName));
                    if(new MainUIController().emptyGenerateBill())
                        System.out.println("bill history cleared!");
            } catch (IOException ex) {
                System.out.println("Exception in Bill_geerator "+ex);
            }
            
        }
        catch (FileNotFoundException| DocumentException ex) 
        {
            System.out.println("Exception in Bill_Generator\n"+ex);
        }
    }      
}
