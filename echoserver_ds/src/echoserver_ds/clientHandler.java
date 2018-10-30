/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver_ds;

import static echoserver_ds.Echoserver_ds.get_number_of_elements;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author muhammad
 */
public class clientHandler extends Thread {
    
    Socket c;
    //volatile to make change visible to all threads  
    volatile User[] Users ;
    //only one shared next_empty_index
    AtomicInteger  next_empty_index; //refers to the array index to which we will add a new user
    int current_user_index;

    public clientHandler(Socket c,User[] Users,AtomicInteger next_empty_index,Integer current_user_index) {
        this.c = c;
        this.Users=Users;
        this.next_empty_index=next_empty_index;
        this.current_user_index=current_user_index;
        
    }
    
    public synchronized void newUser(String full_name, String password, int initial_balance){
         User new_user=new User(full_name, password, initial_balance);
         Users[next_empty_index.get()]=new_user;
         //addAndGet is a function to add new value to the original value of the atomic variable
         next_empty_index.addAndGet(1); //to be able to create the next user in the future
         current_user_index=next_empty_index.get()-1;
    }  

  @Override
    public void run() {
        try{
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

                    newUser(full_name, password, initial_balance);
                    

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
                            +"Type TS to transfer money to another account in the same bank"
                                    + "\nPress L to logout\n");

                            // to take the choice of the user after log in
                            userFirstChoice=dis.readUTF() ;

                             //-------------------------------------------LOGGED IN ACCOUNT DETAILS------------------------------------------------------
                            if(userFirstChoice.equalsIgnoreCase("details")){

                                dos.writeUTF("Full Name: " + Users[current_user_index].full_name +
                                "\n Password: " + Users[current_user_index].password + "\n Balance: "+ Users[current_user_index].balance + "\n ID: " + Users[current_user_index].id
                                +"\nPress enter to continue\n");

                                //-------------------------------------------LOGGED IN ACCOUNT DEPOSIT------------------------------------------------------
                            } else if(userFirstChoice.equalsIgnoreCase("D")){



                             //-------------------------------------------LOGGED IN ACCOUNT WITHDRAWAL------------------------------------------------------
                            } else if(userFirstChoice.equalsIgnoreCase("W")){



                            //-------------------------------------------LOGGED IN ACCOUNT WITHDRAWAL------------------------------------------------------
                           } else if(userFirstChoice.equalsIgnoreCase("TS")){
                               String idOfSecondUser="";
                               try{
                               dos.writeUTF("please write the id of the user you want to tansfer money to ");
                               idOfSecondUser=dis.readUTF();

                               } catch (Exception e){
                                  dos.writeUTF("the id you entered is incorrect");
                              }
                               
                               try{
                                      dos.writeUTF("please write the amount of money you want to transfer");
                                      Float amountOfMoney =Float.parseFloat(dis.readUTF()); 
                                       /*state is a string that holds information about 
                                       how successful the transfer was
                                      */
                                      String state=Users[current_user_index].Transfer(idOfSecondUser,amountOfMoney,Users);
                                      dos.writeUTF(state+" press enter to continue");
                                }
                               catch (Exception e){
                                      dos.writeUTF("the amount you entered is incorrect , press enter to continue");
                                 }
                           

                            //-------------------------------------------LOGGED IN ACCOUNT LOGOUT------------------------------------------------------
                            } else if(userFirstChoice.equalsIgnoreCase("L")){
                                dos.writeUTF("Logged out successfully, press enter");
                                break;
                            }
                            dis.readUTF(); //to absorb user's enter press
                        }//end of while


                    }//end of if login success
                    else {
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
        catch(IOException ex)
        {
            System.out.println("Something went wrong with the client" );
        }
        
}

    
}
