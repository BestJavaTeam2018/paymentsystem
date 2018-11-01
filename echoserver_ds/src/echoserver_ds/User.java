/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver_ds;
import static echoserver_ds.Echoserver_ds.get_number_of_elements;
import java.util.Random;

public class User {
    String full_name;
    String id;
    String password;
    Number balance;
        
    
    //constructor
    User(String Full_Name, String Password, Number Initial_Balance){
        full_name=Full_Name;
        password=Password;
        balance=Initial_Balance;
        
        //creating a random id
        Random rand = new Random();
        id= Integer.toString(rand.nextInt(10000) + 1);
    }
    
    //user can transfer money to another account in the same bank
    public String Transfer(String idOfSecondUser,Number amountOfMoney,User[] users){
        //balance is enough
        if (balance.floatValue()>=amountOfMoney.floatValue()){
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
            return "Unfortunately, your balance is not enough";
        }
    }
    
    
}