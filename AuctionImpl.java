package AuctioningSystem;
/*
	Code: AuctionImpl remote object	Auctionimpl.java

	Contains the arithmetic methods that can be remotley invoked
*/

import java.util.*;
import java.lang.Integer;
import java.io.*;
import java.security.*;
import org.jgroups.*;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.blocks.RpcDispatcher;

// The implementation Class must implement the rmi interface (Auction)
// and be set as a Remote object on a server

public class AuctionImpl
    extends java.rmi.server.UnicastRemoteObject
    implements BuyingInterface, SellerInterface {

    private Map<Integer, Auction> auctions;
    private Map<Integer, byte[]> randomNumbers;
    private JChannel channel;

    // Implementations must have an explicit constructor
    // in order to declare the RemoteException exception

    public AuctionImpl()
        throws java.rmi.RemoteException, Exception {
        super();
        auctions = new HashMap<Integer, Auction>(); //Hashmap that holds all auctions
        randomNumbers = new HashMap<Integer, byte[]>();

        channel=new JChannel();

        channel.connect("AuctionCluster");
    }

    public byte[] sign(byte[] b) 
        throws java.rmi.RemoteException{
            byte[] sig = null;
            try{
                File fileIn = new File("serverprivate.key");
                FileInputStream fis = new FileInputStream(fileIn);
                ObjectInputStream ois = new ObjectInputStream(fis);
                PrivateKey priv = (PrivateKey)ois.readObject();
                Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
                dsa.initSign(priv);
                dsa.update(b);
                sig = dsa.sign();
                
            } catch(Exception e){
                System.out.println(e.toString());
            }
            return sig;
        
    }

    public boolean returnSignedNumber(byte[] b, int uid){
        boolean outcome = false;
        try{
            File fileIn = new File("public" + uid + ".key");
            FileInputStream fis = new FileInputStream(fileIn);
            ObjectInputStream ois = new ObjectInputStream(fis);
            PublicKey pub = (PublicKey)ois.readObject();
            Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
            sig.initVerify(pub);
            sig.update(randomNumbers.get(uid));
            outcome = sig.verify(b);
        } catch(Exception e){
            System.out.println(e.toString());
        }
        return outcome;
    }


    public byte[] authUser(int uid) 
        throws java.rmi.RemoteException{
            Random rand = new Random();
            byte[] nonse = new byte[10];
            rand.nextBytes(nonse);
            randomNumbers.put(uid,nonse);
            return nonse;
    }


    public Collection<Auction> listAllAuctions()
        throws java.rmi.RemoteException{
            return new ArrayList(auctions.values()); //Returns a list of all currently active auctions
        }
    
    public User getUser(int uid){
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

    public int bid(int auctionId, Float bid, int uid)
        throws java.rmi.RemoteException{
            if(auctions.containsKey(auctionId)){
                Auction auction = auctions.get(auctionId); //returns the auction depending on ID
                User bidder = getUser(uid);
                if(auction.getSellerID() == uid){ //if seller is bidder
                    return 3;
                }
                else{
                int outcome = auction.setHighestBid(bid, bidder);   
                return outcome; //0 - too low, 1 - success.
                }
                
            }
            else{
                return 2; //this auction is no longer active
            }
        }

    //Method that creates a new auction and returns the ID
    public int newAuction(Item item)
        throws java.rmi.RemoteException{
            Auction a = new Auction(item); //creates an auction
            a.setId(auctions.size() + 1); //sets the ID
            auctions.put(auctions.size()+1, a); //puts auction on hashmap
            System.out.println("Auction | ID");
            System.out.println(auctions.values() + " | " + a.getId());
            return a.getId();
        }

    public int removeItem(int auctionId)
        throws java.rmi.RemoteException{
            if(auctions.containsKey(auctionId)){
                auctions.remove(auctionId); 
                return 1;
            }
            else{
                System.out.println("Sorry, this item is not up for auction");
                return 0;
            }
        }

    //Method that returns highestbid
    public Float getHighestBid(int auctionId)
        throws java.rmi.RemoteException{
            Float bid;
            if(auctions.containsKey(auctionId)){
                Auction auction = auctions.get(auctionId); 
                bid = auction.getHighestBid(); 
                return bid;
            }
            else{
                bid = new Float(0.00);
                return bid;
            }
        }
    //Method that takes the id and returns a string depending on the state the auction is at when its closed.
    public String closeAuction(int auctionId, int userID)
        throws java.rmi.RemoteException{
            String message;
            if(auctions.containsKey(auctionId)){
                Auction auction = auctions.get(auctionId);
                if(auction.getSellerID() == userID){
                    if(auction.getHighestBid() < auction.getItem().getReservePrice()){
                    message = "The reserve price set by the seller was not reached in this auction. Therefore there is no winner.";
                } else{
                    message ="The winner of this auction is " + auction.getHighestBidder().getName() + ". Email: " + auction.getHighestBidder().getEmail();
                }
                    auctions.remove(auctionId);
                    return message;
                }
                else{
                    return "Sorry, you don't have permission to close this auction";
                }
                
            }
            else{
                return "Sorry, this auction is not active";
            }
        }
}