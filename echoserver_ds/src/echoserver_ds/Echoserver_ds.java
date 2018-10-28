/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver_ds;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Echoserver_ds {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try
        {
            //1.Listen 
            ServerSocket s = new ServerSocket(1234);
            
            while (true) //because the server is always waiting
            {
                //2.accept
                Socket c = s.accept();
                System.out.println("Client Arrived");
                
                //3.create socket (I/O) with client
                DataOutputStream dos = new DataOutputStream(c.getOutputStream());
                DataInputStream dis = new DataInputStream(c.getInputStream());

                //4.IO communication with client
                while (true)
                {
                    String clientmsg = dis.readUTF();
                    dos.writeUTF("Server Says:" + clientmsg);
                    if (clientmsg.equalsIgnoreCase("bye"))
                    {
                        break;
                    }
                }

                //5. close comm with client
                dos.close();
                dis.close();
                c.close();
            }
            
        } catch(IOException ex)
        {
            System.out.println("An error has occured" );
        }
        
        
    }
    
}
