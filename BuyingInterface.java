package AuctioningSystem;
/*
	Code: Buying Interface	buyinginterface.java

	The buying interface provides a description of the 3 remote 
	methods available as part of the service provided
	by the remote object auctionimpl. The interface
	extends remote and each methods throws a remote exception.
*/	
import java.util.*;

public interface BuyingInterface 
          extends java.rmi.Remote {	

    public Float getHighestBid(int auctionId)
        throws java.rmi.RemoteException;  

    public Collection<Auction> listAllAuctions()
        throws java.rmi.RemoteException;
    
    public int bid(int auctionID, Float bid, int bidderId)
        throws java.rmi.RemoteException;
    public byte[] sign(byte[] b) throws java.rmi.RemoteException;

    public byte[] authUser(int uid) throws java.rmi.RemoteException;
    public boolean returnSignedNumber(byte[] b, int uid) throws java.rmi.RemoteException;

}
