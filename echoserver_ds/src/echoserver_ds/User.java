/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver_ds;
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
    
}