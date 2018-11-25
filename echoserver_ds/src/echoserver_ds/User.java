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
import java.util.Random;
import java.util.Scanner;

public class User {
    String full_name;
    String id;
    String password;
    Number balance;
    String bank;
        
    
    //constructor
    User(String Full_Name, String Password, Number Initial_Balance,String ID ){
        full_name=Full_Name;
        password=Password;
        balance=Initial_Balance;
        this.bank=bank;
        
        if(ID==null)
        {
            //creating a random id
            Random rand = new Random();
            id= Integer.toString(rand.nextInt(10000) + 1);
        }
        else
            id= ID;
    }
    
    //user can transfer money to another account in the same bank
    public String Transfer(String idOfSecondUser,Number amountOfMoney,User[] users){
        //balance is enough
        if (balance.floatValue()>=amountOfMoney.floatValue()&&amountOfMoney.floatValue()>=0){
           boolean user_is_founded=false;
           int number_of_users=get_number_of_elements(users);
           for (int i=0;i<number_of_users; i++){
                  //find the id and do the transfer  
                  if(users[i].id.equals(idOfSecondUser)){
                        user_is_founded=true;
                        balance=balance.floatValue()-amountOfMoney.floatValue();
                        users[i].balance=users[i].balance.floatValue()+amountOfMoney.floatValue();
                       } //end of finding the id if condition 
            } //end of for loop
            //user is founded successfully
            if(user_is_founded)
            {
                return "Money is transferred successfully to th"
                      + "e user you specified ,your balance now is "
                      + this.balance.floatValue();
            }  
            else
                return "Unfortunately, No user of such id is founded ";
        }//end of balance check if condition
        //if balance is not enough
        else {
           if(amountOfMoney.floatValue()<0)
              return "Unfortunately, you can't transfer negative number";
           else
              return "Unfortunately, your balance is not enough";
        }
    }
    private boolean isOkToTransfer(Number amount)
    {
        if(balance.floatValue() >= amount.floatValue() )
        {
            return true;
        }
        else 
        {
            return false;
        }
    }
    public String TramsferToAnotherBank(String clientID,Number amount) throws IOException
    {
        
        
        if(isOkToTransfer(amount))
        {
           //creating socket with the other bank...
        // Scanner sc = new Scanner(System.in);
        Socket s =new Socket("127.0.0.1",5678);
                 
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        DataInputStream dis = new DataInputStream(s.getInputStream());
        dis.readUTF();
                dos.writeUTF("sending id");//hey other bank i'm about to send you an id to check if it's in your DB
                 String servermsg = dis.readUTF();//reading server response
                if(servermsg.equalsIgnoreCase("ok"))//making sure the server is waiting for id
                dos.writeUTF(clientID);//sending id to the server
                servermsg=dis.readUTF();//reading server response
                if(servermsg.equalsIgnoreCase("found"))//the other bank says we have that id
                {
                    dos.writeUTF("sending money");//hey i'm about to send you amount of money
                    servermsg=dis.readUTF();
                    if(servermsg.equalsIgnoreCase("ok"))//making sure it's waiting for money transfer
                    dos.writeUTF(amount.toString());//sending money
                    servermsg=dis.readUTF();
                    if(servermsg.equalsIgnoreCase("done"))//transaction has been successfully completed
                    {
                        balance=balance.floatValue()- amount.floatValue();//update current user balance
                        s.close();
                        return "Bank response : Transaction success\n your new Balance is "+balance+"\n";
                    }
                    else{
                        s.close();
                        return "transaction error ";
                    }
                }
                else
                {
                    s.close();
                    return "Bank response : we don't have such id in our system";
                }
               // System.out.println(servermsg);
                
        }
            
                return "your current balance is not enough to perform such transaction";
        
    }
    
    
}
