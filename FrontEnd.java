package AuctioningSystem;
/*
	Code: auction server		AuctionServer.java

	Server code for hosting the auctionImpl object
*/


import java.rmi.Naming;	//Import naming classes to bind to rmiregistry
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class FrontEnd {

   //auctionserver constructor
   public FrontEnd() {
     
     //Construct a new auctionImpl object and bind it to the local rmiregistry
     //N.b. it is possible to host multiple objects on a server by repeating the
     //following method. 

     try {
       	FrontEndImpl f = new FrontEndImpl();
        LocateRegistry.createRegistry(1099);
       	Naming.bind("rmi://localhost/FrontEnd", f);
        
     } 
     catch (Exception e) {
         e.printStackTrace();
     }
   }

   public static void main(String args[]) {
     	//Create the new auction server
        new FrontEnd();
        System.out.println("Front end server is running");
   }
}