/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver_ds;

import static echoserver_ds.Echoserver_ds.get_number_of_elements;
import static echoserver_ds.Transaction.count;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author muhammad
 */
public class clientHandler extends Thread {
    
    Socket c;
    //volatile to make change visible to all threads  
    volatile User[] Users ;
    volatile Transaction[] transactions;
    //only one shared next_empty_index
    AtomicInteger  next_empty_index; //refers to the array index to which we will add a new user
    int current_user_index;
    AtomicInteger next_transaction_index;

     public clientHandler(Socket c,User[] Users,Transaction[] Transactions,AtomicInteger next_empty_index,Integer current_user_index,AtomicInteger next_transaction_index) {
        this.c = c;
        this.Users=Users;
        this.transactions = Transactions ;
        this.next_empty_index=next_empty_index;
        this.current_user_index=current_user_index;
        this.next_transaction_index=next_transaction_index;
        
    }
    
    
    public synchronized void newUser(String full_name, String password, int initial_balance){
         User new_user=new User(full_name, password, initial_balance);
         Users[next_empty_index.get()]=new_user;
         //addAndGet is a function to add new value to the original value of the atomic variable
         next_empty_index.addAndGet(1); //to be able to create the next user in the future
         current_user_index=next_empty_index.get()-1;
    }  
 public synchronized void newTransaction(String User_id,String Type, Number Amount){
    Transaction new_transaction = new Transaction(User_id,Type,Amount);
    transactions[next_transaction_index.get()]=new_transaction ;
    next_transaction_index.addAndGet(1);
}
  @Override
    public void run() {
        try{
         //3.create socket (I/O) with client
         DataOutputStream dos = new DataOutputStream(c.getOutputStream());
         DataInputStream dis = new DataInputStream(c.getInputStream());

            //4.IO communication with client (main program)
            int targetClientIndex=0;//the id of the client that will receive money from another bank
         while (true)
            {
                 dos.writeUTF("To make a new account, press C."
                        + "\n To login, press L "
                        + "\n To get information about our bank, press I"
                        + "\n To exit, press E \n");
                 
                String userFirstChoice = dis.readUTF();
                 if (userFirstChoice.equalsIgnoreCase("sending id")){
                        dos.writeUTF("ok");
                        userFirstChoice=dis.readUTF();
                        System.out.println(userFirstChoice);
                        String clientId=userFirstChoice;
                        System.out.println(clientId);
                        for (int i=0;i<get_number_of_elements(Users);i++)
                        {
                            if(Users[i].id.equalsIgnoreCase(clientId))
                            {
                                targetClientIndex=i;
                                dos.writeUTF("found");
                                System.out.println("found");
                              userFirstChoice=dis.readUTF();
                              if(userFirstChoice.equalsIgnoreCase("sending money"))
                              {
                                  dos.writeUTF("ok");
                                  System.out.println("sending money");
                                  userFirstChoice=dis.readUTF();
                                  System.out.println(userFirstChoice);
                                  Number receivedMoney= Float.parseFloat(userFirstChoice);
             Users[targetClientIndex].balance = Users[targetClientIndex].balance.floatValue() + receivedMoney.floatValue();
                        dos.writeUTF("done");
                                  
                              }
                                
                                
                                
                                break;
                            }
                        }
                     
                        }
                  
                      
                
                
   
                
                
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

                }
                      //------------------------------------------LOGIN---------------------------------------------------
                 
                  else if(userFirstChoice.equalsIgnoreCase("L")){

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
                              dos.writeUTF("\nWelcome to your account: Full Name: " + Users[current_user_index].full_name +
                            "\n Type details for your account details \nType D to deposit an amount \nType W to withdraw an amount \n"
                            +"Type TS to transfer money to another account in the same bank\n"+"Type TS+ to transfer money to another account in other bank"
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

                                dos.writeUTF("Enter the Amount to be deposited.");
                                Number deposited_amount = Float.parseFloat(dis.readUTF());

                                if(deposited_amount.floatValue() > 0) { // no negative numbers 
                                        Users[current_user_index].balance = Users[current_user_index].balance.floatValue() + deposited_amount.floatValue();
                                        dos.writeUTF("your new balance is: " + Users[current_user_index].balance + " \n press enter to continue.");
                                }
                                else {
                                        dos.writeUTF("ERROR: You entered a negative number or a zero. \n press enter to continue.");
                                }

                             //-------------------------------------------LOGGED IN ACCOUNT WITHDRAWAL------------------------------------------------------
                            } else if(userFirstChoice.equalsIgnoreCase("W")){

                                dos.writeUTF("Enter the Amount to be withdrawn.");
                                Number withdrawn_amount = Float.parseFloat(dis.readUTF());

                                if(withdrawn_amount.floatValue() > 0) {
                                    if(withdrawn_amount.floatValue() <= Users[current_user_index].balance.floatValue()){
                                            Users[current_user_index].balance = Users[current_user_index].balance.floatValue() - withdrawn_amount.floatValue();
                                            dos.writeUTF("your new balance is: " + Users[current_user_index].balance + " \n press enter to continue.");
                                    }
                                    else {
                                            dos.writeUTF("ERROR: Withdrawn amount is bigger than the current balance. \n press enter to continue.");
                                    }
                                }
                                else {
                                        dos.writeUTF("ERROR: You entered a negative number or a zero. \n press enter to continue.");
                                }
                                   
                            //-------------------------------------------LOGGED IN ACCOUNT TRASFER------------------------------------------------------
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
                                      newTransaction(Users[current_user_index].id,"Transfer",amountOfMoney);
                                      dos.writeUTF(state+" press enter to continue");
                                }
                               catch (Exception e){
                                      dos.writeUTF("the amount you entered is incorrect , press enter to continue");
                                 }
                           

                            //-------------------------------------------LOGGED IN ACCOUNT LOGOUT------------------------------------------------------
                            }
                            else if(userFirstChoice.equalsIgnoreCase("TS+"))
                                       {
                                           String idOfClient="";
               
                                        try {
                                       dos.writeUTF("Please provide the id of the client you want to transfer money to ");
                                        idOfClient =dis.readUTF();
                                       
                                        
                                        } catch (Exception e) {
                                        dos.writeUTF("sorry , you entered wrong id");
                                       }
                                       try {
                                        dos.writeUTF("Please, Enter the amount of money");
                                       float  amount =Float.parseFloat(dis.readUTF());
                                       
                                        String response=Users[current_user_index].TramsferToAnotherBank(idOfClient,amount);
                                        newTransaction(Users[current_user_index].id,"Transfer",amount);
                                        dos.writeUTF(response+"\npress Enter to continue");
                                       }
                                       catch (Exception e) {
                                       dos.writeUTF("something went wrong\npress Enter to continue");
                                       }
                                       }
                                  //-------------------------------------------LOGGED IN ACCOUNT TRANSACTION HISTORY------------------------------------------------------
                             else if(userFirstChoice.equalsIgnoreCase("H")){
                                 boolean flag = false;
                                 String trs= new String("");
                                 for (int i = 0; i < next_transaction_index.get(); i++){
                                if(transactions[i].user_id.equals(Users[current_user_index].id)){                        
                                 trs=trs+("Transaction_id: "+transactions[i].id+" "+transactions[i].date+" type: "+transactions[i].type+" amount: "+transactions[i].amount+"\n");      
                                flag=true;
                            }
                            }
                                 if (!flag)
                                    trs=("no transactions yet"+"\n press enter to continue");
                                 dos.writeUTF(trs+"\npress enter to continue");
                             
                             
                                
                                   
                           }
                           else if(userFirstChoice.equalsIgnoreCase("L")){
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
            System.out.println("Client socket closed");
        }
        
}

    
}
