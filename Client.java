package AuctioningSystem;
/*
    Code: Calculator client     calculatorClient.java
    Date: 10th October 2000

    Simple client super class that is used to hold features that are common to both buyer and seller clients.

*/
import java.rmi.Remote;
import java.util.*;
import java.rmi.Naming;         //Import the rmi naming - so you can lookup remote object
import java.rmi.RemoteException;    //Import the RemoteException class so you can catch it
import java.net.MalformedURLException;  //Import the MalformedURLException class so you can catch it
import java.rmi.NotBoundException;  //Import the NotBoundException class so you can catch it
import java.util.Scanner;
import java.io.*;
import java.security.*;

public class Client {
    protected Remote frontEndInstance;

    public Client(){
        
    try {
            // Create the reference to the remote object through the remiregistry 

                frontEndInstance =
                           Naming.lookup("rmi://localhost/FrontEnd");
        }
        // Catch the exceptions that may occur - rubbish URL, Remote exception
    // Not bound exception or the arithmetic exception that may occur in 
    // one of the methods creates an arithmetic error (e.g. divide by zero)
    catch (Exception e) {
            System.out.println(e);
        }
    }

    public static int askForID(){
        System.out.println("Please enter your userID: ");
        Scanner scanner = new Scanner(System.in);
        int id = scanner.nextInt();
        return id;
    }

    public static User authenticateUser(SellerInterface s, int uid){
        byte[] sig = null;
        User authedUser = null;
        try{
            byte[] randomnumber = s.authUser(uid);
            User user = readUser(uid);
            PrivateKey priv = user.getPrivateKey();
            Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
            dsa.initSign(priv);
            dsa.update(randomnumber);
            sig = dsa.sign();
            boolean userAuthenticated = s.returnSignedNumber(sig, uid);

            if(userAuthenticated){
                authedUser = user;
                System.out.println("---- USER VERIFIED ----- ");
                System.out.println("Hey " + user.getName());
            }
        } catch(Exception e){
            System.out.println(e.toString());
        }

        return authedUser;
    }

    public static User authenticateUser(BuyingInterface b, int uid){
        byte[] sig = null;
        User authedUser = null;
        try{
            byte[] randomnumber = b.authUser(uid);
            User user = readUser(uid);
            PrivateKey priv = user.getPrivateKey();
            Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
            dsa.initSign(priv);
            dsa.update(randomnumber);
            sig = dsa.sign();
            boolean userAuthenticated = b.returnSignedNumber(sig, uid);
            if(userAuthenticated){
                authedUser = user;
                System.out.println("---- USER VERIFIED ----- ");
                System.out.println("Hey " + user.getName());
            }
        } catch(Exception e){
            System.out.println(e.toString());
        }
        return authedUser;
        
    }
    public static User readUser(int uid){
        User user = null;
        try{
            FileInputStream fis = new FileInputStream("user" + uid + ".ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            System.out.println(fis);
            user = (User)ois.readObject();
            ois.close();
        } catch(Exception e){
            System.out.println("YO" + e.toString());
        }
        return user;
    }
    public static boolean authenticateServer(SellerInterface s, int uid){
        boolean outcome = false;
        Random rand = new Random();
        byte[] nonse = new byte[10];
        rand.nextBytes(nonse);
        try{
            byte[] returned = s.sign(nonse);
            User user = readUser(uid);
            PublicKey pub = user.getServerPublicKey();
            Signature sig = Signature.getInstance("SHA1withDSA", "SUN"); //instance
            sig.initVerify(pub);
            sig.update(nonse);
            outcome = sig.verify(returned);
        } catch(Exception e) {
            System.out.println(e.toString());
        }
        return outcome;
    }
    public static boolean authenticateServer(BuyingInterface b, int uid){
        boolean outcome = false;
        Random rand = new Random();
        byte[] nonse = new byte[10];
        rand.nextBytes(nonse);
        try{
            byte[] returned = b.sign(nonse);
            User user = readUser(uid);
            PublicKey pub = user.getServerPublicKey();
            Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
            sig.initVerify(pub);
            sig.update(nonse);
            outcome = sig.verify(returned);
        } catch(Exception e) {
            System.out.println(e.toString());
        }
        return outcome;
    }
    //Lists operations that the user has to choose from
    public static int ask(String[] commands){
        System.out.println("Please choose an operation from this list of options: ");
            for (int i = 1; i <= commands.length; i++){
                System.out.println("Press " + i + " to " + commands[i-1]);
            }
        Scanner scanner = new Scanner(System.in);
        int command = scanner.nextInt();
        return command;
    }

}
