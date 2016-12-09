package AuctioningSystem;
/*
	Code: auction server		AuctionServer.java

	Server code for hosting the auctionImpl object
*/


import java.rmi.Naming;	//Import naming classes to bind to rmiregistry
import java.rmi.Remote;

public class AuctionServer {

   //auctionserver constructor
   public AuctionServer() {
     
     //Construct a new auctionImpl object and bind it to the local rmiregistry
     //N.b. it is possible to host multiple objects on a server by repeating the
     //following method. 

     try {
       	AuctionImpl a = new AuctionImpl();
       	//Naming.rebind("rmi://localhost/AuctionServer", a);
     } 
     catch (Exception e) {
       System.out.println("Server Error: " + e);
     }
   }

   public static void main(String args[]) {
     	//Create the new auction server
	new AuctionServer();
        System.out.println("Auction server is running");
   }
}