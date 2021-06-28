package bookstore.QRCodes;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Generator 
{
    public static void generateQRCodeImage(String text,File filepath)
    {
        try 
        {
            QRCodeWriter qrcodewriter=new QRCodeWriter();
            BitMatrix bitmatrix=qrcodewriter.encode(text, BarcodeFormat.QR_CODE,176,144);        
            MatrixToImageWriter.writeToFile(bitmatrix,"PNG",filepath);            
        } 
        catch (WriterException|IOException ex) 
        {
            System.out.println("Exception in Generator generateQRCodeImage()\n"+ex);
        }
    }
    
    
    public static String decodeQRCodeImage(BufferedImage bfimage)
    {
        try 
        {
            //BufferedImage bufferedimage=ImageIO.read(filepath);
            BufferedImage bufferedimage=bfimage;
            LuminanceSource src=new BufferedImageLuminanceSource(bufferedimage);
            BinaryBitmap bitmap=new BinaryBitmap(new HybridBinarizer(src));
            
            Result result=new MultiFormatReader().decode(bitmap);
            return result.getText();
        }
        catch (NotFoundException ex) 
        {
            System.out.println("Exception in Generator decodeQRCodeImage()\n"+ex);
        }
        return null;
    }
}











/****TESTING   CODE******/


/*    Webcam webcam;
    JButton btn1,btn2;
    WebcamPanel panel;
    JPanel pan;
    
    Generator()
    {
            this.setLayout(null);
            
            webcam = Webcam.getDefault(); 
            for(Dimension d:webcam.getViewSizes())
                System.out.println(d);
            
            webcam.setViewSize(new Dimension(176,144));
            webcam.open();           
            
            panel= new WebcamPanel(webcam);
            panel.setImageSizeDisplayed(true);
            panel.setMirrored(true);
            
            btn1=new JButton("Take Photo");
            btn1.setBounds(0,400,300,30);
            btn1.addActionListener(this);     
            
            btn2=new JButton("Close Camera");
            btn2.setBounds(305,400,300,30);
            btn2.addActionListener(this);     
                        
            
            pan=new JPanel();
            pan.setBounds(0,10,320,240);
            pan.add(panel);
            
            this.add(btn1);
            this.add(btn2);
            this.add(pan);
            
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public static void main(String[]args) //throws NotFoundException
    {
        Generator obj=new Generator();
        obj.setSize(640,480);        
        obj.setLocationRelativeTo(null);
        obj.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
       if(e.getSource()==btn2) 
        webcam.close();
       else
            try 
            {
               //ImageIO.write(webcam.getImage(),"PNG",new File("capture.jpg"));
               
               
               Result result = null;
               BufferedImage image = null;
               
               if (webcam.isOpen())             
                {
                   // if ((image = webcam.getImage()) == null) {System.out.println("Image null");}
                    
                    image=ImageIO.read(new File("TEXTQR.png"));
                    
                    LuminanceSource source = new BufferedImageLuminanceSource(image);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                    
                    result = new MultiFormatReader().decode(bitmap);
                    
                    if (result != null) 
                    {
                        System.out.println("QR code data is: " + result.getText());
                    }
                }                                              
            }
            catch (IOException|NotFoundException ex) 
            {
                System.out.println(ex);
            }          
    }*/
