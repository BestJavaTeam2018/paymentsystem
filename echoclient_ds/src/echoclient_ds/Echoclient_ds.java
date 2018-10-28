/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoclient_ds;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Echoclient_ds {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try
        {
            //1. Connect
            Socket c = new Socket("127.0.0.1", 1234);
            //3. Create socket (I/O) with server
            DataOutputStream dos=new DataOutputStream(c.getOutputStream());
            DataInputStream dis = new DataInputStream(c.getInputStream());
            
            //4. Perform main operations with server
            
            
            //5. Close communication with server
            dos.close();
            dis.close();
            c.close();
            
        } catch(IOException ex)
        {
            System.out.println("An error has occured" );
        }
    }
    
}
