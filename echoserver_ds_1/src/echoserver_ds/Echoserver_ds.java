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
import java.util.concurrent.atomic.AtomicInteger;

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
            //atomic integer means we only have one shared value of it between all threads 
            AtomicInteger next_empty_index=new AtomicInteger(0); //refers to the array index to which we will add a new user
            int current_user_index=0; //refers to the index of the user who is logged in now

            //1.Listen to any client that may arrive
            ServerSocket s = new ServerSocket(5678);

            while (true) //because the server is always waiting for a new client
            {
                //2.accept
                Socket c = s.accept();
                System.out.println("Client Arrived");
                clientHandler ch =  new clientHandler(c,Users,next_empty_index,current_user_index);
                ch.start();

               
            }

        } catch(IOException ex)
        {
            System.out.println("An error has occured" );
        }


    }

}
