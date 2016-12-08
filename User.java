package AuctioningSystem;
/*
    Code: auction client     auctionClient.java
    Date: 10th October 2000

    Class that creates an user object which holds the user's information.

*/

import java.io.Serializable;
import java.security.*;

public class User implements Serializable {
    private int uid;
    private String name;
    private String email;
    private PublicKey serverkey;
    private PrivateKey key;


    public User(String n, String e){
        name = n;
        email = e;
    }
    public User(PrivateKey k, PublicKey sp, String n, String e, int id){
        name = n;
        email = e;
        serverkey = sp;
        key = k;
        uid = id;
    }

    public PublicKey getServerPublicKey(){
        return serverkey;
    }

    public PrivateKey getPrivateKey(){
        return key;
    }

    public int getID(){
        return uid;
    }

    public String getName(){
        return name;    
    }
    public String getEmail(){
        return email;
    }
}
