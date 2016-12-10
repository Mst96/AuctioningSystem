package AuctioningSystem;
/*
	Code: auction server		AuctionServer.java

	Server code for hosting the auctionImpl object
*/




public class AuctionServer {

   //auctionserver constructor
   public AuctionServer() {
     
     //Construct a new auctionImpl object
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