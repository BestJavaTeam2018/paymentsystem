/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoclient_ds;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Echoclient_ds {

    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) {
        try
        {
            Scanner sc = new Scanner(System.in);
            
            //1. connect to server
            Socket c = new Socket("127.0.0.1", 1234);
            
            //2. create socket
            DataOutputStream dos = new DataOutputStream(c.getOutputStream());
            DataInputStream dis = new DataInputStream(c.getInputStream());

            //3. IO communication with server (main program)
            while (true)
            {
                String servermsg = dis.readUTF();
                System.out.println(servermsg);
                String userresp = sc.nextLine();
                dos.writeUTF(userresp);
                if(userresp.equalsIgnoreCase("N"))
                    break;
            }

            //4.close comm
            dos.close();
            dis.close();
            c.close();
            
        } catch(IOException ex)
        {
            System.out.println("An error has occured" );
        }
    }
    
}
