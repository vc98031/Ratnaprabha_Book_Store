/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookstore.QRCodes;

import java.io.File;

/**
 *
 * @author jayprasad
 */
public class RemoveQRCode 
{
    public RemoveQRCode()
    {
        if(new File("src\\bookstore\\database\\RPBookStore.db").exists())
            System.out.println("Database exist We can do our work!");
        else
        {
            File[] files =new File("src\\bookstore\\QRCodes\\images").listFiles();
            if(files!=null)
                for(File f:files)
                    f.delete();
            System.out.println("QR Images deleted");
        }
    }
    
    public static void emptyFolder(File file)
    {
        File[] files =file.listFiles();
            if(files!=null)
                for(File f:files)
                    f.delete();
    }
    public static void remove(File file)
    {
        if(file.delete()) 
            System.out.println("QR deleted....");
        else 
            System.out.println("QR not deleted....");
    }
}
