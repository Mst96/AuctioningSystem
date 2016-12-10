package AuctioningSystem;
/*
    Code: Calculator client     calculatorClient.java
    Date: 10th October 2000

    Simple client program that remotely calls a set of arithmetic
    methods available on the remote calculatorimpl object

*/

import java.util.*;
import java.rmi.Naming;         //Import the rmi naming - so you can lookup remote object
import java.rmi.RemoteException;    //Import the RemoteException class so you can catch it
import java.net.MalformedURLException;  //Import the MalformedURLException class so you can catch it
import java.rmi.NotBoundException;  //Import the NotBoundException class so you can catch it
import java.util.Scanner;
import java.util.Random;

public class SellerClient extends Client {

    private static SellerInterface s;
    private static final String[] commandsArray = new String[]{"create", "close auction", "quit"};
    private static User user;

    public SellerClient(){
        super();
        s = (SellerInterface) frontEndInstance;
        int id = askForID();
        boolean authenticated = authenticateServer(s,id); //authenticates server
        if(authenticated){
            user = authenticateUser(s,id); //if the server autheticates
            if(user == null){
                System.out.println("User Authentication failed");
                controls(3);
            }
        } else{
            System.out.println("Server Authentication failed");
            controls(3);
            
        }
    }

    public static void main(String[] args) {
        
    try {

        // Create the reference to the remote object through the remiregistry           
            new SellerClient();
            listen();
        }
        // Catch the exceptions that may occur - rubbish URL, Remote exception
    // Not bound exception or the arithmetic exception that may occur in 
    // one of the methods creates an arithmetic error (e.g. divide by zero)
    catch (Exception e) {
            System.out.println(e);
        }
    }
    //Waits for user to input another command
    public static void listen(){
        int action = ask(commandsArray);
        controls(action);
    }
    //Handles controls when a command input is given
    public static void controls(int action){
        try{
            switch(action){
            case 1 : Item item = create();
                int id = s.newAuction(item);
                System.out.println("Started auction.");
                System.out.println("Auction ID is: " + id);
                break;
            case 2: close();
                    break; //close
            case 3: System.out.println("Quitting client....");
                    break;
            default: System.out.println("Sorry, you have entered an incorrect command.");
                    break;
            }
            if(action != 3){
                listen();
            }
            
        }
        catch(Exception e){
            System.out.println(e);
        }
        
    }
    //Runs the closing protocol. Asks for ID and if the user is sure. If yes, then a request is sent to the server to have the auction closed.
    public static void close(){
        System.out.println("Please enter the ID of the auction you would like to close: ");
        Scanner scanner = new Scanner(System.in);
        int id = scanner.nextInt();
        System.out.println("Are you sure you'd like to close this auction? (Y or N)");
        scanner = new Scanner(System.in);
        String yorn = scanner.nextLine();
        yorn.toLowerCase();
        if(yorn.equals("y")){
            try{
                String message = s.closeAuction(id,user.getID());
                System.out.println(message);
            } catch(Exception e){
                System.out.println(e);
            }
        }
        else if(yorn.equals("n")){
            System.out.println("The auction has NOT been closed and is still active.");
        }
        else{
            System.out.println("Sorry didn't quite get that, this auction will not be closed");
        }
    }

    public static Item create(){
        System.out.println("Let's create an auction");
        System.out.println("------------------------");
        System.out.println("Please describe the item: ");
        Scanner scanner = new Scanner(System.in);
        String description = scanner.nextLine();
        System.out.println("Starting price: ");
        scanner = new Scanner(System.in);
        Float sPrice = scanner.nextFloat();
        System.out.println("Reserve price: ");
        scanner = new Scanner(System.in);
        Float rPrice = scanner.nextFloat();
        Item item = new Item(user, sPrice, rPrice, description);
        return item;
    }

}
