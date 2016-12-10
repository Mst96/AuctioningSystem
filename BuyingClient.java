package AuctioningSystem;
/*
    Code: Buying client     BuyingClient.java

    Simple client program that remotely calls a set of
    methods available on the remote auctionimpl object

*/
import java.util.*;
import java.rmi.Naming;         //Import the rmi naming - so you can lookup remote object
import java.rmi.RemoteException;    //Import the RemoteException class so you can catch it
import java.net.MalformedURLException;  //Import the MalformedURLException class so you can catch it
import java.rmi.NotBoundException;  //Import the NotBoundException class so you can catch it
import java.util.Random;
public class BuyingClient extends Client {
    private static BuyingInterface b;
    private final static String[] commandsArray = new String[]{"browse","bid", "quit"};
    private static User user;

    public BuyingClient(){
        super(); //constructs super class and 
        b = (BuyingInterface) frontEndInstance;
        int id = askForID();
        boolean authenticated = authenticateServer(b, id); //authenticates on frontend using server key
        if(authenticated){
            user = authenticateUser(b,id); //authenticates user on front end
        if(user == null){ //if authentication fails close client
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
            new BuyingClient();
            listen();
            //ask for input of name and email
            //a.bid("name", "email");

        }
        // Catch the exceptions that may occur - rubbish URL, Remote exception
    // Not bound exception or the arithmetic exception that may occur in 
    // one of the methods creates an arithmetic error (e.g. divide by zero)
    catch (Exception e) {
            System.out.println(e);
        }
    }
    public static void listAuctions(){
        try{
            Collection<Auction> auctions = b.listAllAuctions();
            System.out.println("Currently active auctions: ");
            System.out.println("ID" +" | " + "Description " + "| " + "Starting Price" + "| " + "Highest Bid" +  "| " + "Seller");
            Iterator<Auction> it = auctions.iterator();
            while ( it.hasNext()){
                Auction auction = (Auction)it.next();
                System.out.println(auction.getId() + " " + auction.getItem().getDescription() + " | " + auction.getItem().getStartingPrice() + " | " + auction.getHighestBid() +" | " + auction.getSellerName());
            }
        }
        catch(Exception e){
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
        switch(action){
            case 1: listAuctions();
                            break;
            case 2: bid();
                    break;
            case 3: System.out.println("Quitting client....");
                    break;
            default: System.out.println("Sorry, you have entered an incorrect command.");
                    break;
        }
        if(action != 3){
            listen();
        }
        

    }
    //Bid method takes in seller details and bid, places bid and prints out message explaining outcome.
    public static void bid(){
        try{
            System.out.println("Please enter ID of the auction you would like to bid in: ");
            Scanner scanner = new Scanner(System.in);
            int id = scanner.nextInt();
            System.out.println("How much would you like to bid?");
            scanner = new Scanner(System.in);
            Float price = scanner.nextFloat();

            int outcome = b.bid(id, price, user.getID());
            if(outcome == 0){
                System.out.println("Unfortunately there was a higher bid!");
            } else if(outcome == 1){
                System.out.println("You have successfully placed a bid!");
            } else if(outcome == 2){
                System.out.println("Sorry, this auction is not currently active.");
            } else if(outcome == 3){
                System.out.println("Sorry, you cannot bid on your own item.");
            }

        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
