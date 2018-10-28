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
                    dos.writeUTF("To make a new account, press C."
                            + "\n To login, press L "
                            + "\n To get information about our bank, press I"
                            + "\n To exit, press E \n");
                    
                    String userFirstChoice = dis.readUTF();
                    
                    //------------------------------------------CREATE NEW ACCOUNT---------------------------------------------------
                    
                    if(userFirstChoice.equalsIgnoreCase("C")){
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

                        dos.writeUTF("Congratulations! Here is your account info: Full Name: " + Users[current_user_index].full_name +
                                "\n Password: " + Users[current_user_index].password + "\n Balance: "+ Users[current_user_index].balance + "\n ID: " + Users[current_user_index].id
                                +"\nPress enter to continue\n");
                        
                        dis.readUTF(); //to absorb the user's enter press
                        
                     //------------------------------------------LOGIN---------------------------------------------------
                                    
                     } else if(userFirstChoice.equalsIgnoreCase("L")){
                         
                        boolean login_success=false;
                         
                        dos.writeUTF("Enter your ID: ");
                        String id = dis.readUTF();

                        dos.writeUTF("Enter your password ");
                        String password = dis.readUTF();
                        
                        int number_of_users=get_number_of_elements(Users);

                        for (int i = 0; i < number_of_users; i++){
                            
                            System.out.println(id);
                            System.out.println(password);
                            System.out.println(Users[i].id);
                            System.out.println(Users[i].password);
                            
                            if(Users[i].id.equals(id)&& Users[i].password.equals(password)){
                                login_success=true;
                                current_user_index=i;
                                break;
                            }
                        }
                        
                        if(login_success==true){
                            
                            while(true){
                                dos.writeUTF("Welcome to your account: Full Name: " + Users[current_user_index].full_name +
                                "\n Type details for your account details \n Type D to deposit an amount \n \n Type W to withdraw an amount \n"
                                +"\nPress L to logout\n");
                            
                                 //-------------------------------------------LOGGED IN ACCOUNT DETAILS------------------------------------------------------
                                if(userFirstChoice.equalsIgnoreCase("details")){
                                    
                                    dos.writeUTF("Full Name: " + Users[current_user_index].full_name +
                                    "\n Password: " + Users[current_user_index].password + "\n Balance: "+ Users[current_user_index].balance + "\n ID: " + Users[current_user_index].id
                                    +"\nPress enter to continue\n");

                                 //-------------------------------------------LOGGED IN ACCOUNT DEPOSIT------------------------------------------------------
                                } else if(userFirstChoice.equalsIgnoreCase("D")){

                                    
                                    
                                 //-------------------------------------------LOGGED IN ACCOUNT WITHDRAWAL------------------------------------------------------
                                } else if(userFirstChoice.equalsIgnoreCase("W")){

                                    
                                    
                                //-------------------------------------------LOGGED IN ACCOUNT LOGOUT------------------------------------------------------
                                } else if(userFirstChoice.equalsIgnoreCase("L")){
                                    dos.writeUTF("Logged out successfully, press enter");
                                    break;
                                }
                            }
                            
                            
                        }else {
                            dos.writeUTF("Sorry, wrong ID or password, press enter to try again");
                        }
                        
                        dis.readUTF(); //to absorb the user's enter press

                     //------------------------------------------GET BANK INFORMATION---------------------------------------------------

                    } else if(userFirstChoice.equalsIgnoreCase("I")){
                        int number_of_users=get_number_of_elements(Users);

                        if(number_of_users==0){
                           dos.writeUTF("Now, we have "+number_of_users+" users"
                                   + "\n Press enter to continue");
                        } else {
                            dos.writeUTF("Now, we have "+number_of_users+" users " + " Press Enter to see more info about them");
                            dis.readUTF();
                            String users="";
                            for (int i = 0; i < number_of_users; i++){
                                users=users+"\n"+Users[i].full_name+"\n"
                                        + Users[i].id;
                            }
                            dos.writeUTF(users+"\n \n Press enter to continue");
                        }
                        
                        dis.readUTF(); //to absorb the user's enter press

                    //------------------------------------------EXIT---------------------------------------------------

                    }else if(userFirstChoice.equalsIgnoreCase("E")){
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
