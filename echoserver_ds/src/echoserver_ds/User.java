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
        if (this.balance.floatValue()>=amountOfMoney.floatValue()){
            boolean user_is_founded=false;
            this.balance=this.balance.floatValue()-amountOfMoney.floatValue();
             int number_of_users=get_number_of_elements(users);
            for (int i=0;i<number_of_users; i++){
                  if(users[i].id.equals(idOfSecondUser)){
                                users[i].balance=users[i].balance.floatValue()+amountOfMoney.floatValue();
                                user_is_founded=true;
                                break;
                            }
            }
            if(user_is_founded)
                return "Money is transferred successfully to the user you specified ,your balance now is"
                        + this.balance.floatValue();
            else
                return "Unfortunately, No user of such id is founded ";
        //if balance is not enough
        }else {
            return "Unfortunately, your balance is not enough";
        }
    }
    
    
}