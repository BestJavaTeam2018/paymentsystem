/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver_ds;

import java.util.Date;
import java.util.Random;


/**
 *
 * @author Kareem
 */
public class Transaction {
    Number id;
    Date date;
    String user_id;
    String type;
    Number amount;
    static int count = 0 ;
    
    

Transaction (String User_id,String Type, Number Amount){
user_id = User_id;
type = Type ;
amount = Amount;
date = new Date();
count++;
Random rand= new Random();
int high = 100000*count;
int low = 100000*(count-1);
id = rand.nextInt(high-low)+low ;
}
}