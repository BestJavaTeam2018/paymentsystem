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
    
    //a function to help us get the current number of elements in an array
    public static <T> int get_number_of_elements(T[] arr){
        int count = 0;
        for(T el : arr)
            if (el != null)
                ++count;
        return count;
        }
    
    public static void main(String[] args) {
        
        try{
            //Making the array of users
            User[] Users = new User[1000]; //created an array of Users
            //no need to import the User class before using it, because it is in the same package
            int next_empty_index=0; //refers to the array index to which we will add a new user
            int current_user_index=0; //refers to the index of the user who is logged in now

            //1.Listen to any client that may arrive
            ServerSocket s = new ServerSocket(1234);
            
            while (true) //because the server is always waiting for a new client
            {
                //2.accept
                Socket c = s.accept();
                System.out.println("Client Arrived");
                
                //3.create socket (I/O) with client
                DataOutputStream dos = new DataOutputStream(c.getOutputStream());
                DataInputStream dis = new DataInputStream(c.getInputStream());

                //4.IO communication with client (main program)
                
                while (true)
                {
                    dos.writeUTF("Enter your full name: ");
                    String full_name = dis.readUTF();
                    
                    dos.writeUTF("Enter a password ");
                    String password = dis.readUTF();
                    
                    dos.writeUTF("Enter an initial balance ");
                    int initial_balance = Integer.parseInt(dis.readUTF()); //converted string to number
                    
                    User new_user=new User(full_name, password, initial_balance);
                    Users[next_empty_index]=new_user;
                    
                    next_empty_index++; //to be able to create the next user in the future

                    current_user_index=next_empty_index-1;
                    
                    dos.writeUTF("Here is your account info: Full Name: " + Users[userIndex].full_name +
                            " Password: " + Users[userIndex].password + " Balance: "+ Users[userIndex].balance + " ID: " + Users[userIndex].id);
                    
                    
                    int number_of_users=get_number_of_elements(Users);
                    dos.writeUTF("number of users now are: "+ number_of_users);
                                        
                    System.out.println("Now, we have "+number_of_users+" users");
                    for (int i = 0; i < number_of_users; i++){
                        System.out.println(Users[i].full_name+"\n");
                        System.out.println(Users[i].id+"\n");
                    }
                    
                    
                    String userChoice = dis.readUTF();
                    if(userChoice.equalsIgnoreCase("N"))
                        break;
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
